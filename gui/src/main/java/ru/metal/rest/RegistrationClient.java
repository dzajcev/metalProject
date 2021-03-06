package ru.metal.rest;

import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.RegistrationResponse;

/**
 * Created by User on 11.09.2017.
 */
public class RegistrationClient extends AbstractRestClient {
    private final String basePath = "auth";
    private final String pathRegistration = basePath + "/registration";

    public RegistrationResponse createRegistration(RegistrationRequest request) {
        RegistrationResponse execute = executePost(pathRegistration + "/create", request, RegistrationResponse.class);
        return execute;
    }


}
