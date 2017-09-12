package ru.metal.api.auth.request;

import ru.common.api.dto.ValidationConstants;
import ru.common.api.request.AbstractRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by User on 11.09.2017.
 */
public class AcceptRegistrationRequest extends AbstractRequest {
    @NotNull
    @Pattern(regexp = ValidationConstants.GUID_PATTERN)
    private String registrationRequestGuid;

    public String getRegistrationRequestGuid() {
        return registrationRequestGuid;
    }

    public void setRegistrationRequestGuid(String registrationRequestGuid) {
        this.registrationRequestGuid = registrationRequestGuid;
    }
}
