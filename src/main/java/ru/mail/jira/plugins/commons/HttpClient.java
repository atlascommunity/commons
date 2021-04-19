package ru.mail.jira.plugins.commons;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

public class HttpClient {
  static {
    Unirest.config()
        .connectTimeout(10_000)
        .socketTimeout(300_000)
        .setObjectMapper(new JacksonObjectMapper());
  }

  public static UnirestInstance getPrimaryClient() {
    return Unirest.primaryInstance();
  }
}
