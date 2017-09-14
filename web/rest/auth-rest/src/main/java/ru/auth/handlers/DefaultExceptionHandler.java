package ru.auth.handlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by User on 09.08.2017.
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {


    @Override
    public Response toResponse(Exception e) {
        StringBuilder response = new StringBuilder("<response>");
        response.append("<status>ERROR</status>");
        response.append("<message>" + e.getMessage() + "</message>");
        response.append("</response>");
        return Response.serverError().entity(response.toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
