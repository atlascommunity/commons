package ru.mail.jira.plugins.commons.dto;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement
public class ErrorDto {
  @NotNull @XmlElement private final String error;
  @Nullable @XmlElement private final String field;

  public ErrorDto(@NotNull String error, @NotNull String field) {
    this.error = error;
    this.field = field;
  }

  public ErrorDto(@NotNull String error) {
    this.error = error;
    this.field = null;
  }
}
