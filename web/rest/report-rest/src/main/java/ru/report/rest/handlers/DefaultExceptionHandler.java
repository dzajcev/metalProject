package ru.report.rest.handlers;

import ru.lanit.hcs.rest.utils.ExceptionUtils;
import ru.metal.crypto.service.CryptoException;

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
        Throwable throwable = ExceptionUtils.containThrowable(e, CryptoException.class);
        if (throwable != null) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            return Response.serverError().type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
}
