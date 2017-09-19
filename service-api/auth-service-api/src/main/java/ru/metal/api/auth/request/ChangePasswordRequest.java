package ru.metal.api.auth.request;

/**
 * Created by User on 19.09.2017.
 */
public class ChangePasswordRequest extends AuthorizationRequest {

    private String newToken;

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
