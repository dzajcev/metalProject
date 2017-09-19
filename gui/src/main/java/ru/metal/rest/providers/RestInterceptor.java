package ru.metal.rest.providers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.ChangePasswordRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.security.ejb.PermissionContextData;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.crypto.service.AsymmetricCipher;
import ru.metal.crypto.service.CryptoException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.*;
import java.io.*;

/**
 * Created by User on 11.09.2017.
 */

@Provider
public class RestInterceptor implements WriterInterceptor, ReaderInterceptor, ClientRequestFilter {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();
        PermissionContextData permissionContextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (permissionContextData == null) {
            context.setOutputStream(old);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Serializable entity = (Serializable) context.getEntity();
            String value = mapper.writeValueAsString(entity);
            try {

                CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(value, UserContextHolder.getPublicKey());
                context.setEntity(cryptoPacket);
                old.write(SerializationUtils.serialize(cryptoPacket));
                context.setOutputStream(old);
            }catch (CryptoException e){
                throw new RuntimeException(e);
            }
        }
        context.proceed();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

            InputStream inputStream = context.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            try {
                Object o = SerializationUtils.deserialize(bytes);
                if (o instanceof CryptoPacket) {
                    CryptoPacket cryptoPacket = (CryptoPacket) o;
                    String stringValue = AsymmetricCipher.decryptPacket(cryptoPacket, UserContextHolder.getPrivateKey());
                    stringValue = new String(stringValue.getBytes("UTF-8"));
                    context.setInputStream(new ByteArrayInputStream(stringValue.getBytes("UTF-8")));
                } else {
                    byte[] bytes1 = IOUtils.toByteArray(context.getInputStream());
                    context.setInputStream(new ByteArrayInputStream(bytes1));
                }
            }catch (SerializationException e){
                context.setInputStream(new ByteArrayInputStream(new String(bytes).getBytes("UTF-8")));
            } catch (CryptoException e) {
                throw new IllegalStateException(e);
            }
            return context.proceed();
    }

    private boolean isAuthorization(Class<?> type) {
        return type == AuthorizationRequest.class;
    }
    private boolean isChangePassword(Class<?> type) {
        return type == ChangePasswordRequest.class;
    }
    private boolean isRegistration(Class<?> type) {
        return type == RegistrationRequest.class;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        PermissionContextData permissionContextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (isAuthorization(requestContext.getEntityClass()) || isChangePassword(requestContext.getEntityClass())) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) requestContext.getEntity();
            requestContext.getHeaders().putSingle("token", authorizationRequest.getToken());
        }
        if (isRegistration(requestContext.getEntityClass())) {
            requestContext.getHeaders().putSingle("registration", true);
        }
        if (permissionContextData!=null) {
            requestContext.getHeaders().putSingle("sessionGuid", permissionContextData.getSessionGuid());
        }
    }
}
