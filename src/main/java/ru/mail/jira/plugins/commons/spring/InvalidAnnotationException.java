package ru.mail.jira.plugins.commons.spring;

import org.springframework.beans.BeansException;

public class InvalidAnnotationException extends BeansException {
  public InvalidAnnotationException(String msg) {
    super(msg);
  }

  public InvalidAnnotationException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
