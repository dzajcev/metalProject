package ru.metal.security.ejb.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ru.metal.security.ejb.security.DelegateUser;
import ru.metal.security.ejb.security.DelegatingUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends UserCommonData {

    //делегирующие юзеры
    private List<DelegatingUser> donorRights;

    //кому делегирую права
    private List<DelegateUser> consumersRights;

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

    public List<DelegatingUser> getDonorRights() {
        if (donorRights ==null){
            donorRights =new ArrayList<>();
        }
        return donorRights;
    }

    public void setDonorRights(List<DelegatingUser> donorRights) {
        this.donorRights = donorRights;
    }

    public List<DelegateUser> getConsumersRights() {
        if (consumersRights ==null){
            consumersRights =new ArrayList<>();
        }
        return consumersRights;
    }

    public void setConsumersRights(List<DelegateUser> consumersRights) {
        this.consumersRights = consumersRights;
    }


}
