package ru.mail.jira.plugins.commons.rest;

import com.atlassian.jira.exception.NotFoundException;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON_TYPE)
                       .build();
    }
}
