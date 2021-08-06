package ru.mail.jira.plugins.commons.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Filter {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @NotNull private final Map<String, String> data;

  public static <DTO> Filter just(@Nullable DTO filter) {
    Map<String, String> columns = toMap(filter);
    return columns != null ? new Filter(columns) : null;
  }

  @Nullable
  private static Map<String, String> toMap(@Nullable Object obj) {
    if (obj == null) {
      return null;
    }
    Map<String, String> fields =
        objectMapper.convertValue(obj, new TypeReference<Map<String, String>>() {});

    Map<String, String> columns =
        fields.entrySet().stream()
            .filter(entry -> entry.getValue() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return columns.isEmpty() ? null : columns;
  }
}
