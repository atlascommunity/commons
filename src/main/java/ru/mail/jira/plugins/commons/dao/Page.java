package ru.mail.jira.plugins.commons.dao;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
public class Page<T> {
  @NotNull private final T[] data;
  private final int total;
}
