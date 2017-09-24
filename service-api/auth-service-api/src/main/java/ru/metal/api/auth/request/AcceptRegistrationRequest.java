package ru.metal.api.auth.request;

import ru.common.api.dto.ValidationConstants;
import ru.common.api.request.AbstractRequest;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.DelegateUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
public class AcceptRegistrationRequest extends AbstractRequest {
    @NotNull
    @Pattern(regexp = ValidationConstants.GUID_PATTERN)
    private String registrationRequestGuid;

    private List<Role> roles;

    private List<DelegateUser> consumerRights;

    private List<Privilege> privileges;

    public List<Role> getRoles() {
        if (roles==null){
            roles=new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Privilege> getPrivileges() {
        if (privileges==null){
            privileges=new ArrayList<>();
        }
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public String getRegistrationRequestGuid() {
        return registrationRequestGuid;
    }

    public void setRegistrationRequestGuid(String registrationRequestGuid) {
        this.registrationRequestGuid = registrationRequestGuid;
    }

    public List<DelegateUser> getConsumerRights() {
        if (consumerRights ==null){
            consumerRights =new ArrayList<>();
        }
        return consumerRights;
    }

    public void setConsumerRights(List<DelegateUser> delegateUsers) {
        this.consumerRights = delegateUsers;
    }
}
