package ru.metal.api.auth.request;

import ru.common.api.request.AbstractRequest;

/**
 * Created by User on 11.09.2017.
 */
public class ObtainUserRequest extends AbstractRequest {
    private String login;

    private String email;

    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
