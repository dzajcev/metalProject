package ru.metal.rest;

import ru.common.api.response.ObtainEmailSettingsResponse;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.UpdateUserResponse;

/**
 * Created by User on 10.09.2017.
 */
public class AdminClient extends AbstractRestClient {

    private final String basePath = "admin";

    public ObtainEmailSettingsResponse getEmailSettings() {
        ObtainEmailSettingsResponse execute = executeGet(basePath + "/emailsettings", ObtainEmailSettingsResponse.class);
        return execute;
    }

    public ObtainRegistrationRequestsResponse getRegistrationRequests() {
        ObtainRegistrationRequestsResponse execute = executeGet(basePath + "/users/getRegistrationRequests", ObtainRegistrationRequestsResponse.class);
        return execute;
    }

    public ObtainUserResponse getUsers(ObtainUserRequest obtainUserRequest) {
        ObtainUserResponse execute = executePost(basePath + "/users/get", obtainUserRequest, ObtainUserResponse.class);
        return execute;
    }

    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        UpdateUserResponse execute = executePost(basePath + "/users/update", request, UpdateUserResponse.class);
        return execute;
    }

    public AcceptRegistrationResponse acceptRegistration(AcceptRegistrationRequest acceptRegistrationRequest) {
        AcceptRegistrationResponse execute = executePost(basePath + "/users/acceptRegistrationRequest", acceptRegistrationRequest, AcceptRegistrationResponse.class);
        return execute;
    }
}
