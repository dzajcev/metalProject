package ru.metal.rest;

import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.ChangePasswordRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.ChangePasswordResponse;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationClient extends AbstractRestClient {

    private final String basePath = "auth";
    private final String pathAuthorization = basePath + "/authorization";

    public AuthorizationResponse login(AuthorizationRequest request) {
        AuthorizationResponse execute = executePost(pathAuthorization + "/authorize", request, AuthorizationResponse.class);
        return execute;
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        ChangePasswordResponse execute = executePost(pathAuthorization + "/changePassword", request, ChangePasswordResponse.class);
        return execute;
    }
}
