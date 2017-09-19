package ru.metal.api.auth;

import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.RegistrationResponse;

/**
 * Created by User on 11.09.2017.
 */
public interface RegistrationFacade {

    RegistrationResponse createRegistration(RegistrationRequest registrationRequest);

    AcceptRegistrationResponse acceptRegistration(AcceptRegistrationRequest acceptRegistrationRequest, boolean firstRun);

    ObtainRegistrationRequestsResponse getRegistrationRequests();
}
