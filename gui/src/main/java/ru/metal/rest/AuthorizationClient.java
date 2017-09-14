package ru.metal.rest;

import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.response.AuthorizationResponse;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationClient extends AbstractRestClient {

    private final String basePath="auth";
    private final String pathAuthorization = basePath+"/authorization";

    public AuthorizationResponse login(AuthorizationRequest request) {
        AuthorizationResponse execute = execute(pathAuthorization + "/authorize", RequestType.POST, request, AuthorizationResponse.class);
        return execute;
    }
}
