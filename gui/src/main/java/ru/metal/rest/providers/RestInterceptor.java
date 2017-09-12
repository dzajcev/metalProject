package ru.metal.rest.providers;

import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import ru.metal.api.auth.dto.PermissionContextData;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.crypto.service.CryptoPacket;
import ru.metal.crypto.service.RestEntity;
import ru.metal.crypto.service.UserContextHolder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 11.09.2017.
 */

@Provider
public class RestInterceptor implements WriterInterceptor, ReaderInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();

        if (isAuthorization(context.getType())) {
            context.getHeaders().putSingle("authorization",true);
        }
        PermissionContextData permissionContextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (permissionContextData == null) {
            context.setOutputStream(old);
        } else {
            context.getHeaders().putSingle("sessionGuid", permissionContextData.getSessionGuid());
            ObjectMapper mapper = new ObjectMapper();
            Serializable entity = (Serializable) context.getEntity();
            String value = mapper.writeValueAsString(entity);
            value = new String(value.getBytes("UTF-8"));
            CryptoPacket cryptoPacket = UserContextHolder.ecryptPacket(value);
            context.setEntity(cryptoPacket);
            old.write(SerializationUtils.serialize(cryptoPacket));
            context.setOutputStream(old);
        }
        context.proceed();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        return context.proceed();
    }

    private boolean isAuthorization(Class<?> type) {
        if (type == AuthorizationRequest.class
                || type == RegistrationRequest.class) {
            return true;
        }
        return false;
    }
}
