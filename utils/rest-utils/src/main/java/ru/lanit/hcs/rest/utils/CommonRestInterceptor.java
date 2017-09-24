package ru.lanit.hcs.rest.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.dto.SessionDto;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.crypto.service.AsymmetricCipher;
import ru.metal.crypto.service.CryptoException;
import ru.metal.security.ejb.AccessCheckException;
import ru.metal.security.ejb.PermissionContextData;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.security.ejb.security.SecurityUtils;
import ru.metal.security.ejb.security.annotation.Allow;
import ru.metal.security.ejb.security.annotation.AllowCondition;
import ru.metal.security.ejb.security.annotation.AllowSystemUser;
import ru.metal.security.ejb.security.annotation.CheckSecurity;

import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.*;
import java.io.*;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

/**
 * Created by User on 13.09.2017.
 */
@Provider
@Singleton
public class CommonRestInterceptor implements ContainerRequestFilter, ReaderInterceptor, WriterInterceptor {
    AuthorizationFacade authorizationFacade;

    @javax.ws.rs.core.Context
    private HttpResponse httpResponse;

    @javax.ws.rs.core.Context
    private HttpRequest httpRequest;
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

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String sessionGuid = requestContext.getHeaderString("sessionGuid");
        SessionDto session;
        if (sessionGuid != null) {
            session = authorizationFacade.getSession(sessionGuid);
            if (session == null) {
                requestContext.abortWith(new ServerResponse(
                        "User not authorized",
                        401, new Headers<>()));
                return;
            }
            if (session.isClosed()) {
                requestContext.abortWith(new ServerResponse(
                        "session is expired",
                        406, new Headers<>()));
                return;
            }
            PermissionContextData p = new PermissionContextData();
            p.setUser(session.getUser());
            KeyPair keyPair = authorizationFacade.getKeyPair(session.getUser().getGuid());
            try {
                UserContextHolder.loadKeyPair(keyPair.getPrivateKey(), keyPair.getPublicKey());
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            UserContextHolder.setUserPermissionDataThreadLocal(p);
            check(requestContext);
        }
    }


    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        if (context.getHeaders().containsKey("token") || context.getHeaders().containsKey("registration")) {
            return context.proceed();
        }
        try {
            InputStream inputStream = context.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            try {
                Object o = SerializationUtils.deserialize(bytes);
                if (o instanceof CryptoPacket) {
                    CryptoPacket cryptoPacket = (CryptoPacket) o;
                    String stringValue = AsymmetricCipher.decryptPacket(cryptoPacket, UserContextHolder.getPrivateKey());
                    context.setInputStream(new ByteArrayInputStream(stringValue.getBytes("UTF-8")));
                } else {
                    byte[] bytes1 = IOUtils.toByteArray(context.getInputStream());
                    context.setInputStream(new ByteArrayInputStream(bytes1));
                }
            } catch (CryptoException e) {
                throw new IOException(e);
            } catch (Exception e) {
                context.setInputStream(new ByteArrayInputStream(new String(bytes).getBytes("UTF-8")));
            }
            return context.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        if (context.getHeaders().containsKey("token") || context.getHeaders().containsKey("registration")) {
            context.proceed();
            return;
        }
        OutputStream old = context.getOutputStream();

        if (httpResponse.getStatus() < 200 || httpResponse.getStatus() >= 300) {
            context.proceed();
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Serializable entity = (Serializable) context.getEntity();

        if (entity instanceof RegistrationResponse) {
            context.setOutputStream(old);
            context.proceed();
            return;
        }

        try {
            String value = mapper.writeValueAsString(entity);
            CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(value, UserContextHolder.getPublicKey());
            context.setEntity(cryptoPacket);
            context.setType(CryptoPacket.class);
            context.setGenericType(CryptoPacket.class);
            old.write(SerializationUtils.serialize(cryptoPacket));
            context.setOutputStream(old);
        } catch (CryptoException e) {
            throw new IOException(e);
        }catch (Exception e){
            e.printStackTrace();
        }
        context.proceed();
    }

    private void check(ContainerRequestContext requestContext){
        PostMatchContainerRequestContext postMatchContainerRequestContext=(PostMatchContainerRequestContext)requestContext;

        Method m = postMatchContainerRequestContext.getResourceMethod().getMethod();
        if (m.isAnnotationPresent(AllowSystemUser.class) && SecurityUtils.isSystemUser()) {
            return;
        }
        if (!isSecuredType(postMatchContainerRequestContext)) {
            return;
        }
        if (m.isAnnotationPresent(AllowSystemUser.class) && SecurityUtils.isSystemUser()) {
            return;
        }
        if (m.isAnnotationPresent(Allow.class)) {
            Allow allow = m.getAnnotation(Allow.class);
            // Просто @Allow разрешает доступ всем.
            if (allow.condition().length == 0) {
                return;
            }
            for (AllowCondition condition : allow.condition()) {

                if (SecurityUtils.checkPrivileges(condition.privilege()) &&
                        SecurityUtils.checkUserRoles(condition.userRole())) {
                    return; // Все проверки пройдены
                }
            }
        }
        // А если метод не аннотирован - по умолчанию запрещаем.
        throw new AccessCheckException("Access is denied");
    }
    private boolean isSecuredType(PostMatchContainerRequestContext context) {
        Class clazz = context.getResourceMethod().getResourceClass();
        if (clazz.isAnnotationPresent(CheckSecurity.class)) {
            return true;
        }
        return false;
    }
}


