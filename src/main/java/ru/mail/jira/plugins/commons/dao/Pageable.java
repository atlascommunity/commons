package ru.mail.jira.plugins.commons.dao;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@Builder
@Getter
public class Pageable {
  private static final int DEFAULT_PAGE_SIZE = 25;

  @Nullable private final Sort sort;
  @Nullable private final Integer size;
  @Nullable private final Integer page;
  @Nullable private final Filter filter;

  public String orderClause() {
    return sort != null ? sort.property() + " " + sort.order() : null;
  }

  public String whereClause() {
    return filter != null
        ? filter.getData().keySet().stream()
            .map(s -> s.toUpperCase() + " LIKE ? ")
            .collect(Collectors.joining(" and "))
        : null;
  }

  public Object[] whereParams() {
    return filter != null
        ? filter.getData().values().stream().map(s -> "%" + s + "%").toArray()
        : null;
  }

  public int offset() {
    if (page != null) {
      return (page - 1) * limit();
    } else {
      return 1;
    }
  }

  public int limit() {
    return size != null ? size : DEFAULT_PAGE_SIZE;
  }
}
