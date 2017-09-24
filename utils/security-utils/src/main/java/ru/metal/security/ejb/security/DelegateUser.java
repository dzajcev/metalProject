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
public class DelegateUser implements Serializable {
    private String userGuid;

    private String userName;

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
