package ru.mail.jira.plugins.commons;

import kong.unirest.GenericType;
import kong.unirest.ObjectMapper;
import kong.unirest.UnirestException;

import java.io.IOException;

public class JacksonObjectMapper implements ObjectMapper {
  private final org.codehaus.jackson.map.ObjectMapper om;

  public JacksonObjectMapper() {
    this(new org.codehaus.jackson.map.ObjectMapper());
  }

  public JacksonObjectMapper(org.codehaus.jackson.map.ObjectMapper om) {
    this.om = om;
  }

  public <T> T readValue(String value, Class<T> valueType) {
    try {
      return this.om.readValue(value, valueType);
    } catch (IOException var4) {
      throw new UnirestException(var4);
    }
  }

  public <T> T readValue(String value, GenericType<T> genericType) {
    try {
      return this.om.readValue(value, this.om.constructType(genericType.getType()));
    } catch (IOException var4) {
      throw new UnirestException(var4);
    }
  }

  public String writeValue(Object value) {
    try {
      return this.om.writeValueAsString(value);
    } catch (Exception e) {
      throw new UnirestException(e);
    }
  }
}
