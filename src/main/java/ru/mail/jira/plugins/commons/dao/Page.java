package ru.mail.jira.plugins.commons.dao;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Builder
public class Page<T> {
  @NotNull private final List<T> data;
  private final int total;
}
