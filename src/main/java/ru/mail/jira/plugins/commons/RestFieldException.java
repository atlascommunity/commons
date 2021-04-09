package ru.mail.jira.plugins.commons;

public class RestFieldException extends RuntimeException {
  private final String field;

  public RestFieldException(String message) {
    super(message);
    this.field = null;
  }

  public RestFieldException(String message, Throwable cause) {
    super(message, cause);
    this.field = null;
  }

  public RestFieldException(String message, String field) {
    super(message);
    this.field = field;
  }

  public RestFieldException(String message, String field, Throwable cause) {
    super(message, cause);
    this.field = field;
  }

  public String getField() {
    return field;
  }
}
