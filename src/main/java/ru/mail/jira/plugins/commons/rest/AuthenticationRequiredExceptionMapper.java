package ru.mail.jira.plugins.commons.rest;

import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import ru.mail.jira.plugins.commons.dto.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationRequiredExceptionMapper implements ExceptionMapper<AuthenticationRequiredException> {
    @Override
    public Response toResponse(AuthenticationRequiredException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON_TYPE)
                       .build();
    }
}
