package ru.mail.jira.plugins.commons.rest;

import com.atlassian.plugins.rest.common.sal.websudo.WebSudoRequiredException;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebSudoRequiredExceptionMapper implements ExceptionMapper<WebSudoRequiredException> {
  @Override
  public Response toResponse(WebSudoRequiredException exception) {
    return Response.status(Response.Status.FORBIDDEN)
        .entity(new ErrorDto(exception.getMessage()))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
