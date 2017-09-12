package ru.metal.api.auth.response;

import ru.common.api.response.AbstractResponse;
import ru.metal.crypto.ejb.PermissionContextData;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationResponse extends AbstractResponse {

    private PermissionContextData permissionContextData;

    public PermissionContextData getPermissionContextData() {
        return permissionContextData;
    }

    public void setPermissionContextData(PermissionContextData permissionContextData) {
        this.permissionContextData = permissionContextData;
    }
}
