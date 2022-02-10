package ru.mail.jira.plugins.commons.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.jira.plugins.commons.SentryClient;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

  private final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

  public GenericExceptionMapper() {}

  @Override
  public Response toResponse(Exception exception) {
    SentryClient.capture(exception);

    logger.error("REST Exception", exception);

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(new ErrorDto(exception.getMessage()))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}
