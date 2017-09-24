package ru.metal.api.auth.request;

import ru.common.api.request.AbstractRequest;
import ru.metal.security.ejb.dto.RegistrationData;

/**
 * Created by User on 10.09.2017.
 */
public class RegistrationRequest extends AbstractRequest {

    private RegistrationData registrationData;

    public RegistrationData getRegistrationData() {
        return registrationData;
    }

    public void setRegistrationData(RegistrationData registrationData) {
        this.registrationData = registrationData;
    }

}
