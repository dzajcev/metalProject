package ru.metal.api.auth.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends UserCommonData {


    private boolean active=true;

    private List<Role> roles;

    private List<Privilege> privileges;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Role> getRoles() {
        if (roles ==null){
            roles =new ArrayList<>();
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

    public String getShortName() {
        StringBuilder stringBuilder = new StringBuilder(getSecondName()).append(" ");
        stringBuilder.append(getFirstName().substring(0, 1)).append(". ");
        if (getMiddleName() != null) {
            stringBuilder.append(getMiddleName().substring(0, 1)).append(". ");
        }
        return stringBuilder.toString();
    }
}
