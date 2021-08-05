package ru.mail.jira.plugins.commons.admin;

import com.atlassian.jira.exception.NotFoundException;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import net.java.ao.Entity;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.jira.plugins.commons.dao.*;
import ru.mail.jira.plugins.commons.dto.DataDTO;
import ru.mail.jira.plugins.commons.dto.DataListDTO;

import javax.ws.rs.*;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Produces("application/json")
@Consumes("application/json")
public abstract class AbstractAdminResource<DTO, T extends Entity> {
  protected final PagingAndSortingRepository<T, DTO> repository;
  protected final GlobalPermissionManager globalPermissionManager;
  protected final JiraAuthenticationContext jiraAuthenticationContext;
  protected final ObjectMapper objectMapper = new ObjectMapper();
  protected final Class<DTO> type;

  protected AbstractAdminResource(
      PagingAndSortingRepository<T, DTO> repository,
      GlobalPermissionManager globalPermissionManager,
      JiraAuthenticationContext jiraAuthenticationContext) {
    this.repository = repository;
    this.globalPermissionManager = globalPermissionManager;
    this.jiraAuthenticationContext = jiraAuthenticationContext;
    this.type =
        (Class<DTO>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @GET
  @Path("{id}")
  @NotNull
  public DataDTO<DTO> getOne(@PathParam("id") int id) throws NotFoundException {
    checkIsAdmin();
    return DataDTO.just(repository.entityToDto(repository.get(id)));
  }

  @POST
  @NotNull
  public DataDTO<DTO> create(@NotNull DTO data) {
    checkIsAdmin();
    return DataDTO.just(repository.entityToDto(repository.create(data)));
  }

  @PUT
  @Path("{id}")
  @NotNull
  public DataDTO<DTO> update(@PathParam("id") int id, @NotNull DTO data) throws NotFoundException {
    checkIsAdmin();
    return DataDTO.just(repository.entityToDto(repository.update(id, data)));
  }

  @GET
  @NotNull
  public DataListDTO<DTO> getList(
      @Nullable @QueryParam("page") Integer page,
      @Nullable @QueryParam("limit") Integer limit,
      @Nullable @QueryParam("sort") String sort,
      @Nullable @QueryParam("order") String order,
      @Nullable @QueryParam("filter") String filter) {
    checkIsAdmin();

    DTO filterDto = null;
    if (StringUtils.isNotEmpty(filter)) {
      try {
        filterDto = objectMapper.readValue(filter, type);
      } catch (IOException e) {
        // ignore filter parsing error
      }
    }

    Page<T> result =
        repository.findAll(
            Pageable.<DTO>builder()
                .page(page)
                .size(limit)
                .sort(Sort.build(sort, order))
                .filter(Filter.just(filterDto))
                .build());

    return DataListDTO.just(
        Arrays.stream(result.getData()).map(repository::entityToDto).collect(Collectors.toList()),
        result.getTotal());
  }

  @GET()
  @Path("many")
  @NotNull
  public DataListDTO<DTO> getMany(@QueryParam("ids[]") @NotNull Set<Integer> ids) {
    checkIsAdmin();

    return DataListDTO.just(
        repository.findAllById(ids).stream()
            .map(repository::entityToDto)
            .collect(Collectors.toList()));
  }

  @GET
  @Path("/refs/{target}/{id}")
  @NotNull
  public DataListDTO<DTO> getManyReference(
      @PathParam("target") String target,
      @PathParam("id") int id,
      @Nullable @QueryParam("page") Integer page,
      @Nullable @QueryParam("limit") Integer limit,
      @Nullable @QueryParam("sort") String sort,
      @Nullable @QueryParam("order") String order) {
    checkIsAdmin();

    Page<T> result =
        repository.findBy(
            target,
            id,
            Pageable.<DTO>builder().page(page).size(limit).sort(Sort.build(sort, order)).build());

    return DataListDTO.just(
        Arrays.stream(result.getData()).map(repository::entityToDto).collect(Collectors.toList()),
        result.getTotal());
  }

  @PUT
  @NotNull
  public DataListDTO<Integer> updateMany(
      @QueryParam("ids[]") @NotNull Set<Integer> ids, @NotNull DTO data) {
    checkIsAdmin();

    return DataListDTO.just(repository.update(ids, data));
  }

  @DELETE
  @Path("{id}")
  @NotNull
  public DataDTO<DTO> delete(@PathParam("id") int id) {
    checkIsAdmin();

    T t = repository.findById(id).orElse(null);
    if (t != null) {
      repository.delete(t);
    }
    return Optional.ofNullable(t)
        .map(repository::entityToDto)
        .map(DataDTO::just)
        .orElse(DataDTO.empty());
  }

  @DELETE
  @NotNull
  public DataListDTO<Integer> deleteMany(@QueryParam("ids[]") @NotNull Set<Integer> ids) {
    checkIsAdmin();

    repository.deleteAllById(ids);

    return DataListDTO.just(ids);
  }

  private boolean isJiraAdmin(ApplicationUser user) {
    return globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, user);
  }

  private void checkIsAdmin() throws SecurityException {
    if (!isJiraAdmin(jiraAuthenticationContext.getLoggedInUser())) {
      throw new SecurityException("You can't access this resource");
    }
  }
}
