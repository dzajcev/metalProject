package ru.metal.api.auth.request;

import ru.common.api.request.AbstractRequest;
import ru.metal.api.auth.dto.User;

/**
 * Created by User on 11.09.2017.
 */
public class UpdateUserRequest extends AbstractRequest {
    private User user;

    private boolean toChangePassword;
    private boolean needGenerateKeys;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNeedGenerateKeys() {
        return needGenerateKeys;
    }

    public void setNeedGenerateKeys(boolean needGenerateKeys) {
        this.needGenerateKeys = needGenerateKeys;
    }

    public boolean isToChangePassword() {
        return toChangePassword;
    }

    public void setToChangePassword(boolean toChangePassword) {
        this.toChangePassword = toChangePassword;
    }
}
