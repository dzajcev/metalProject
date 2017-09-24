package ru.metal.security.ejb.security;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 21.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelegatingUser extends DelegateUser {
    private List<Role> roles;

    private List<Privilege> privileges;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
