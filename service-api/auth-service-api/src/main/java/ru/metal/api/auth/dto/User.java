package ru.metal.api.auth.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.crypto.ejb.dto.AbstractDto;
import ru.metal.crypto.ejb.dto.Position;
import ru.metal.crypto.ejb.dto.Privilege;
import ru.metal.crypto.ejb.dto.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AbstractDto {
    private String login;

    private String secondName;

    private String middleName;

    private String firstName;

    private Position position;

    private String email;

    private String token;

    private List<Role> role;

    private List<Privilege> privileges;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Role> getRole() {
        if (role==null){
            role=new ArrayList<>();
        }
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public List<Privilege> getPrivileges() {
        if (privileges==null){
            privileges=new ArrayList<>();
        }
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
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
