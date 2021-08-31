package ru.mail.jira.plugins.commons.admin;

import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.exception.NotFoundException;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.jira.plugins.commons.PluginProperties;
import ru.mail.jira.plugins.commons.dto.DataDTO;
import ru.mail.jira.plugins.commons.dto.DataListDTO;
import ru.mail.jira.plugins.commons.dto.jira.UserDto;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Produces("application/json")
@Consumes("application/json")
@Path("/admin/user")
public class UserAdminResource {
  protected final GlobalPermissionManager globalPermissionManager;
  protected final GroupManager groupManager;
  protected final JiraAuthenticationContext jiraAuthenticationContext;
  protected final PluginProperties pluginProperties;
  private final UserManager userManager;
  private final UserSearchService userSearchService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  protected UserAdminResource(
      UserManager userManager,
      GlobalPermissionManager globalPermissionManager,
      JiraAuthenticationContext jiraAuthenticationContext,
      GroupManager groupManager,
      PluginProperties pluginProperties,
      UserSearchService userSearchService) {
    this.userManager = userManager;
    this.globalPermissionManager = globalPermissionManager;
    this.groupManager = groupManager;
    this.jiraAuthenticationContext = jiraAuthenticationContext;
    this.pluginProperties = pluginProperties;
    this.userSearchService = userSearchService;
  }

  @GET
  @Path("{userKey}")
  @NotNull
  public DataDTO<UserDto> getOne(@PathParam("userKey") String userKey) throws NotFoundException {
    checkPermissions();
    return DataDTO.just(
        Optional.ofNullable(userManager.getUserByKey(userKey))
            .map(UserDto::new)
            .orElseThrow(NotFoundException::new));
  }

  @GET
  @NotNull
  public DataListDTO<UserDto> getList(
      @Nullable @QueryParam("page") Integer page,
      @Nullable @QueryParam("limit") Integer limit,
      @Nullable @QueryParam("filter") String filter) {
    checkPermissions();

    UserDto filterDto = null;
    if (StringUtils.isNotEmpty(filter)) {
      try {
        filterDto = objectMapper.readValue(filter, UserDto.class);
      } catch (IOException e) {
        // ignore filter parsing error
      }
    }
    List<ApplicationUser> result =
        userSearchService.findUsers(
            filterDto != null ? filterDto.getName() : "",
            UserSearchParams.builder()
                .includeActive(true)
                .canMatchEmail(true)
                .maxResults(100)
                .build());

    return DataListDTO.just(
        result.stream().map(UserDto::new).collect(Collectors.toList()), result.size());
  }

  @GET
  @Path("many")
  @NotNull
  public DataListDTO<UserDto> getMany(@QueryParam("ids[]") @NotNull Set<String> ids) {
    checkPermissions();

    return DataListDTO.just(
        ids.stream().map(userManager::getUserByKey).map(UserDto::new).collect(Collectors.toList()));
  }

  private boolean isJiraAdmin(ApplicationUser user) {
    return globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, user);
  }

  private void checkPermissions() throws SecurityException {
    ApplicationUser user = jiraAuthenticationContext.getLoggedInUser();
    if (isJiraAdmin(user)) {
      return;
    }
    if (Arrays.stream(pluginProperties.getStringArray("access-admin-groups"))
        .anyMatch(group -> groupManager.isUserInGroup(user, group))) {
      return;
    }
    throw new SecurityException("You can't access this resource");
  }
}
