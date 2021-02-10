package ru.mail.jira.plugins.commons;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public final class RestUtils {

    public static Response success(Object entity) {
        return Response.ok()
                       .entity(entity)
                       .build();
    }

    public static Response binary(byte[] entity) {
        return Response.ok()
                       .type(new MediaType("application", "force-download"))
                       .header("Content-Transfer-Encoding", "binary")
                       .header("charset", "UTF-8")
                       .entity(entity)
                       .build();
    }

    public static Response stream(InputStream entity, String contentType) {
        return Response.ok()
                       .entity(entity)
                       .type(contentType)
                       .build();
    }

    public static Response.ResponseBuilder response(Response.Status status) {
        return Response.status(status);
    }
}
