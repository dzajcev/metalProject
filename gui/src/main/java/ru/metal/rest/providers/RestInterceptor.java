package ru.metal.rest.providers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.crypto.ejb.PermissionContextData;
import ru.metal.crypto.ejb.UserContextHolder;
import ru.metal.crypto.service.AsymmetricCipher;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.*;
import java.io.*;

/**
 * Created by User on 11.09.2017.
 */

@Provider
public class RestInterceptor implements WriterInterceptor, ReaderInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();

        if (isAuthorization(context.getType())) {
            context.getHeaders().putSingle("authorization", true);
        }
        PermissionContextData permissionContextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (permissionContextData == null) {
            context.setOutputStream(old);
        } else {
            context.getHeaders().putSingle("sessionGuid", permissionContextData.getSessionGuid());
            ObjectMapper mapper = new ObjectMapper();
            Serializable entity = (Serializable) context.getEntity();
            String value = mapper.writeValueAsString(entity);
            CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(value, UserContextHolder.getPublicKey());
            context.setEntity(cryptoPacket);
            old.write(SerializationUtils.serialize(cryptoPacket));
            context.setOutputStream(old);
        }
        context.proceed();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        try {
            InputStream inputStream = context.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            try {
                Object o = SerializationUtils.deserialize(bytes);
                if (o instanceof CryptoPacket) {
                    CryptoPacket cryptoPacket = (CryptoPacket) o;
                    String stringValue = AsymmetricCipher.decryptPacket(cryptoPacket, UserContextHolder.getPrivateKey());
                    stringValue=new String(stringValue.getBytes("UTF-8"));
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

    private boolean isAuthorization(Class<?> type) {
        return type == AuthorizationRequest.class
                || type == RegistrationRequest.class;
    }
}
