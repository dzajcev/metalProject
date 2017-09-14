package ru.metal.crypto.ejb;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.crypto.ejb.dto.Position;
import ru.metal.crypto.ejb.dto.Privilege;
import ru.metal.crypto.ejb.dto.Role;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionContextData implements Serializable {
    private String userGuid;

    private String secondName;

    private String middleName;

    private String firstName;

    private Position position;

    private List<Role> roles;

    private List<Privilege> privileges;

    private String sessionGuid;

    public String getSessionGuid() {
        return sessionGuid;
    }

    private String token;

    public void setSessionGuid(String sessionGuid) {
        this.sessionGuid = sessionGuid;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Role> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Privilege> getPrivileges() {
        if (privileges == null) {
            privileges = new ArrayList<>();
        }
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
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

    public String getShortName() {
        StringBuilder stringBuilder = new StringBuilder(secondName).append(" ");
        stringBuilder.append(firstName.substring(0, 1)).append(". ");
        if (middleName != null) {
            stringBuilder.append(middleName.substring(0, 1)).append(". ");
        }
        return stringBuilder.toString();
    }
}
