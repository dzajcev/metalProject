package ru.lanit.hcs.rest.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.*;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.dto.SessionDto;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.crypto.ejb.PermissionContextData;
import ru.metal.crypto.ejb.UserContextHolder;
import ru.metal.crypto.service.AsymmetricCipher;

import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by User on 11.09.2017.
 */
@Provider
@ServerInterceptor
@Singleton
public class RestInterceptor implements MessageBodyReaderInterceptor, MessageBodyWriterInterceptor, PreProcessInterceptor, PostProcessInterceptor {
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

    @Override
    public Object read(MessageBodyReaderContext context) throws IOException, WebApplicationException {
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
            } catch (Exception e) {
                context.setInputStream(new ByteArrayInputStream(new String(bytes).getBytes("UTF-8")));
            }
            return context.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(MessageBodyWriterContext context) throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        Serializable entity = (Serializable) context.getEntity();

        if (entity instanceof AuthorizationResponse){
            AuthorizationResponse authorizationResponse=(AuthorizationResponse)entity;
            if (authorizationResponse.getPermissionContextData()!=null) {
                KeyPair keyPair = authorizationFacade.getKeyPair(authorizationResponse.getPermissionContextData().getUserGuid());
                UserContextHolder.loadKeyPair(keyPair.getPrivateKey(), keyPair.getPublicKey());
            }else{

            }
        }else if (entity instanceof RegistrationResponse){
            context.setOutputStream(old);
            context.proceed();
            return;
        }
        //если сущность это контекст, то прям тут получаем ключи и шифруем
        String value = mapper.writeValueAsString(entity);
        CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(value, UserContextHolder.getPublicKey());
        context.setEntity(cryptoPacket);
        old.write(SerializationUtils.serialize(cryptoPacket));
        context.setOutputStream(old);

        context.proceed();
    }

    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        ServerResponse response = null;

        HttpHeaders httpHeaders = request.getHttpHeaders();
        if (httpHeaders.getRequestHeader("authorization") != null) {
            return response;
        }
        List<String> sessionGuids = httpHeaders.getRequestHeader("sessionGuid");
        SessionDto session = null;
        boolean authorized = true;
        if (sessionGuids == null || sessionGuids.isEmpty()) {
            authorized = false;
        }
        if (authorized) {
            String sessionGuid = sessionGuids.get(0);
            session = authorizationFacade.getSession(sessionGuid);
            if (session == null || session.isClosed()) {
                authorized = false;
            }
        }

        if (!authorized) {
            response = new ServerResponse(
                    "User not authorized",
                    401, new Headers<Object>());
            return response;
        }


        PermissionContextData p = new PermissionContextData();
        p.setUserGuid(session.getUser().getGuid());
        p.setSecondName(session.getUser().getSecondName());
        p.setMiddleName(session.getUser().getMiddleName());
        p.setFirstName(session.getUser().getFirstName());
        p.setPosition(session.getUser().getPosition());
        p.setRoles(session.getUser().getRole());
        p.setPrivileges(session.getUser().getPrivileges());
        KeyPair keyPair = authorizationFacade.getKeyPair(session.getUser().getGuid());
        UserContextHolder.loadKeyPair(keyPair.getPrivateKey(), keyPair.getPublicKey());
        UserContextHolder.setUserPermissionDataThreadLocal(p);
        return response;
    }

    @Override
    public void postProcess(ServerResponse response) {

    }
}