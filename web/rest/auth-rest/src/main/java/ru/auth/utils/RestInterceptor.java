package ru.auth.utils;

import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.crypto.ejb.UserContextHolder;
import ru.metal.crypto.service.AsymmetricCipher;
import ru.metal.crypto.service.CryptoException;

import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

/**
 * Created by User on 13.09.2017.
 */
@Provider
@Singleton
public class RestInterceptor implements ContainerRequestFilter, WriterInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestInterceptor.class);
    AuthorizationFacade authorizationFacade;

    {
        try {
            Properties jndiProperties = new Properties();
            jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            Context context1 = new InitialContext(jndiProperties);
            authorizationFacade = (AuthorizationFacade) context1.lookup("ejb:auth-service-ear/auth-service-impl/authorizationFacade!ru.metal.api.auth.AuthorizationFacade");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @javax.ws.rs.core.Context
    private HttpResponse httpResponse;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String registration = requestContext.getHeaderString("registration");
        String token = requestContext.getHeaderString("token");
        if (registration != null) {
            return;
        } else if (token != null) {
            AuthorizationRequest authRequest = new AuthorizationRequest();
            authRequest.setToken(token);
            AuthorizationResponse authorization;
            authorization = authorizationFacade.authorization(authRequest);
            if (authorization.getPermissionContextData() == null) {
                requestContext.abortWith(new ServerResponse(
                        "User not found",
                        401, new Headers<>()));

            } else {
                KeyPair keyPair = authorizationFacade.getKeyPair(authorization.getPermissionContextData().getUserGuid());
                try {
                    UserContextHolder.loadKeyPair(keyPair.getPrivateKey(), keyPair.getPublicKey());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                UserContextHolder.setUserPermissionDataThreadLocal(authorization.getPermissionContextData());
            }
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        int status = httpResponse.getStatus();
        if (status != 200) {
            context.proceed();
            return;
        }
        OutputStream old = context.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        Serializable entity = (Serializable) context.getEntity();

        if (entity instanceof RegistrationResponse) {
            context.setOutputStream(old);
            context.proceed();
            return;
        }
        String value = mapper.writeValueAsString(entity);
        try {
            CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(value, UserContextHolder.getPublicKey());
            context.setEntity(cryptoPacket);
            context.setType(CryptoPacket.class);
            context.setGenericType(CryptoPacket.class);
            old.write(SerializationUtils.serialize(cryptoPacket));
            context.setOutputStream(old);
        }catch (CryptoException e){
            context.setEntity(null);
            httpResponse.setStatus(403);
        }
        context.proceed();
    }
}
