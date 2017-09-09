package ru.lanit.hcs.rest.utils;

import java.util.Collections;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.apache.commons.lang3.exception.ExceptionUtils;


@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {

    @Context
    Providers providers;

    @Override
    public Response toResponse(EJBException e) {
    	
    	
        if (e.getCausedByException() != null) {
        	Class cause = e.getCausedByException().getClass();
            ExceptionMapper mapper = providers.getExceptionMapper(cause);
            if (mapper != null) {
            	return mapper.toResponse(e.getCausedByException());
            }
        }

        JsonResponseModel response = new JsonResponseModel(e.getClass().getSimpleName(), new HashMap<String, Object>());
		response.getDetails().put("message", e.getMessage());
        response.getDetails().put("cause", e.getCause());
        response.getDetails().put("stackTrace", ExceptionUtils.getStackTrace(e));

        return Response.serverError().entity(response).type(MediaType.APPLICATION_JSON_TYPE).build();
        
        
        
    }
}
