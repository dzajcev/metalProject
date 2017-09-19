package ru.metal.api.auth.dto;

import ru.metal.security.ejb.dto.AbstractDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by User on 19.09.2017.
 */
public abstract class UserCommonData extends AbstractDto {
    @NotNull
    @Size(min = 6)
    private String login;
    @NotNull
    @Size(min = 3)
    private String secondName;
    @NotNull
    @Size(min = 3)
    private String middleName;
    @NotNull
    @Size(min = 3)
    private String firstName;
    @NotNull
    @Size(min = 4)
    private String email;
    @NotNull
    private String token;

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
}
