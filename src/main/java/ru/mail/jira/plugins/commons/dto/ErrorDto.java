package ru.mail.jira.plugins.commons.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorDto {
  @XmlElement private final String error;
  @XmlElement private final String field;

  public ErrorDto(String error, String field) {
    this.error = error;
    this.field = field;
  }

  public ErrorDto(String error) {
    this.error = error;
    this.field = null;
  }

  public String getError() {
    return error;
  }

  public String getField() {
    return field;
  }
}
