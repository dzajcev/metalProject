package ru.metal.api.auth.request;

import ru.common.api.request.AbstractRequest;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationRequest extends AbstractRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
