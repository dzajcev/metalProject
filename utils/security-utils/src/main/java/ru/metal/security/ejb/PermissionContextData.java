package ru.metal.security.ejb;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.security.ejb.dto.User;
import ru.metal.security.ejb.security.DelegatingUser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionContextData implements Serializable {
    private User user;

    private String sessionGuid;

    private boolean systemUser;

    public String getSessionGuid() {
        return sessionGuid;
    }

    private boolean toChangePassword;

    public void setSessionGuid(String sessionGuid) {
        this.sessionGuid = sessionGuid;
    }

    public boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
    }

    public boolean isToChangePassword() {
        return toChangePassword;
    }

    public byte[] serialize() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null; //TODO: maybe should throw exception here
        }
    }

    public static PermissionContextData deserialize(byte[] b) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(b);
             ObjectInputStream is = new ObjectInputStream(in)) {
            return (PermissionContextData) is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null; //TODO: maybe should throw exception here
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
