package ru.mail.jira.plugins.commons.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class Filter<DTO> {
  @NotNull private final DTO data;

  public static <DTO> Filter<DTO> just(@Nullable DTO filter) {
    return filter != null ? new Filter<DTO>(filter) : null;
  }
}
