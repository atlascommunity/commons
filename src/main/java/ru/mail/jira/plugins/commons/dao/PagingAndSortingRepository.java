package ru.mail.jira.plugins.commons.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.exception.NotFoundException;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import net.java.ao.Entity;
import net.java.ao.Query;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class PagingAndSortingRepository<T extends Entity, DTO> {
  protected final ActiveObjects ao;
  protected final Class<T> type;

  public PagingAndSortingRepository(ActiveObjects ao) {
    this.ao = ao;
    this.type =
        (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public abstract DTO entityToDto(@NotNull T entity);

  public abstract void updateEntityFromDto(@NotNull DTO dto, @NotNull T entity);

  public abstract String mapDbField(@NotNull String field);

  /**
   * Update entity with id by DTO data
   *
   * @param id entity ID to update
   * @param data data DTO for updating entity
   */
  @NotNull
  public T update(int id, @NotNull DTO data) throws NotFoundException {
    T entity = get(id);

    updateEntityFromDto(data, entity);

    entity.save();
    return entity;
  }

  /**
   * Update all entities with ids by DTO data with fail-safe iteration
   *
   * @param ids entity IDs to update
   * @param data data DTO for updating entities
   */
  @NotNull
  public List<Integer> update(@NotNull Iterable<Integer> ids, @NotNull DTO data) {
    List<T> entities = findAllById(ids);

    entities.forEach(
        entity -> {
          try {
            updateEntityFromDto(data, entity);
            entity.save();
          } catch (Exception e) {
            log.error("Updating {}[{}]", type.getSimpleName(), entity.getID(), e);
          }
        });

    return entities.stream().map(Entity::getID).collect(Collectors.toList());
  }

  /**
   * Create entity with id by DTO data
   *
   * @param data data DTO for create entity
   */
  @NotNull
  public T create(@NotNull DTO data) {
    T entity = ao.create(type);

    updateEntityFromDto(data, entity);

    entity.save();
    return entity;
  }

  @NotNull
  // todo implement filtering
  public Page<T> findAll(@NotNull Pageable pageable) {
    return Page.<T>builder()
        .data(
            ao.find(
                type,
                Query.select()
                    .order(pageable.orderClause())
                    .offset(pageable.offset())
                    .limit(pageable.limit())
                    .where(pageable.whereClause(), pageable.whereParams())))
        .total(count(pageable))
        .build();
  }

  public int count() {
    return ao.count(type);
  }

  public int count(Pageable pageable) {
    return ao.count(type, Query.select().where(pageable.whereClause(), pageable.whereParams()));
  }

  public int countBy(String target, int id) {
    return ao.count(type, Query.select().where(mapDbField(target) + " = ?", id));
  }

  @NotNull
  public T get(final int id) throws NotFoundException {
    return findById(id)
        .orElseThrow(() -> new NotFoundException(type.getSimpleName() + "[" + id + "] not found"));
  }

  public boolean existsById(int id) {
    return ao.count(type, Query.select().where("ID = ?", id)) > 0;
  }

  @NotNull
  public Optional<T> findById(final int id) {
    return Optional.ofNullable(ao.get(type, id));
  }

  @NotNull
  public List<T> findAllById(@NotNull Iterable<Integer> ids) {
    return Arrays.stream(ao.get(type, Iterables.toArray(ids, Integer.class)))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public Page<T> findBy(@NotNull String target, int id, Pageable pageable) {
    return Page.<T>builder()
        .data(
            ao.find(
                type,
                Query.select()
                    .where(mapDbField(target) + " = ?", id)
                    .order(pageable.orderClause())
                    .offset(pageable.offset())
                    .limit(pageable.limit())))
        .total(countBy(target, id))
        .build();
  }

  @NotNull
  public List<T> findAll() {
    return Arrays.stream(ao.find(type)).collect(Collectors.toList());
  }

  public void delete(@NotNull T entity) {
    ao.delete(entity);
  }

  public void deleteAll(@NotNull Iterable<T> entities) {
    ao.delete(Iterables.toArray(entities, type));
  }

  public void deleteById(int id) {
    ao.deleteWithSQL(type, "ID = ?", id);
  }

  public void deleteAllById(@NotNull Iterable<Integer> ids) {
    deleteAll(findAllById(ids));
  }
}
