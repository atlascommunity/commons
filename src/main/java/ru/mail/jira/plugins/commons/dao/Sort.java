package ru.mail.jira.plugins.commons.dao;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Builder
@Getter
public class Sort {
  @NotNull private final String property;
  @Builder.Default @NotNull private final Direction order = Direction.ASC;

  public static Sort build(String sort, String order) {
    if (StringUtils.isEmpty(sort)) {
      return null;
    }

    return Sort.builder()
        .property(sort)
        .order(
            Optional.ofNullable(order)
                .map(o -> Direction.valueOf(o.toUpperCase()))
                .orElse(Direction.ASC))
        .build();
  }

  public String property() {
    return property;
  }

  public Direction order() {
    return order;
  }

  public enum Direction {
    ASC,
    DESC;
  }
}
