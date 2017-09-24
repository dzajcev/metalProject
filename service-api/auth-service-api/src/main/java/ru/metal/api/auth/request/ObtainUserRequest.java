package ru.metal.api.auth.request;

import ru.common.api.request.AbstractRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
public class ObtainUserRequest extends AbstractRequest {
    private String login;

    private String email;

    private boolean active;

    private List<String> guids;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getGuids() {
        if (guids == null) {
            guids = new ArrayList<>();
        }
        return guids;
    }

    public void setGuids(List<String> guids) {
        this.guids = guids;
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
