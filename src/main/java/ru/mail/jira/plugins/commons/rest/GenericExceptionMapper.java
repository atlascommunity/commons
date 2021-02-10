package ru.mail.jira.plugins.commons.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.jira.plugins.commons.SentryClient;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

  private final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

  @Context
  Request request;
  @Context
  UriInfo uriInfo;

  public GenericExceptionMapper() {
  }

  @Override
  public Response toResponse(Exception exception) {
    SentryClient.capture(request, uriInfo, exception);

    logger.error("REST Exception", exception);

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(new ErrorDto(exception.getMessage()))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
