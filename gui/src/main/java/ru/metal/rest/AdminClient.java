package ru.metal.rest;

import ru.common.api.response.ObtainEmailSettingsResponse;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.api.auth.response.UpdateUserResponse;

/**
 * Created by User on 10.09.2017.
 */
public class AdminClient extends AbstractRestClient {

    private final String basePath = "admin";

    public ObtainEmailSettingsResponse getEmailSettings() {
        ObtainEmailSettingsResponse execute = execute(basePath + "/emailsettings", RequestType.GET, ObtainEmailSettingsResponse.class);
        return execute;
    }

    public ObtainRegistrationRequestsResponse getRegistrationRequests() {
        ObtainRegistrationRequestsResponse execute = execute(basePath + "/users/get", RequestType.GET, ObtainRegistrationRequestsResponse.class);
        return execute;
    }

    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        UpdateUserResponse execute = execute(basePath + "/users/update", RequestType.POST, UpdateUserResponse.class);
        return execute;
    }

    public AcceptRegistrationResponse acceptRegistration(AcceptRegistrationRequest acceptRegistrationRequest) {
        AcceptRegistrationResponse execute = execute(basePath + "/users/accept", RequestType.POST, acceptRegistrationRequest, AcceptRegistrationResponse.class);
        return execute;
    }
}
