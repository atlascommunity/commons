package ru.mail.jira.plugins.commons.rest;

import ru.mail.jira.plugins.commons.RestFieldException;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestFieldExceptionMapper implements ExceptionMapper<RestFieldException> {
  @Override
  public Response toResponse(RestFieldException exception) {
    return Response.status(Response.Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .header("X-Atlassian-Rest-Exception-Field", exception.getField())
        .entity(new ErrorDto(exception.getMessage(), exception.getField()))
        .build();
  }
}
