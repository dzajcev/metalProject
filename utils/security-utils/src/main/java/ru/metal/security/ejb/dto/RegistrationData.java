package ru.metal.security.ejb.dto;


import javax.validation.constraints.NotNull;

/**
 * Created by User on 11.09.2017.
 */
public class RegistrationData extends UserCommonData {
    @NotNull
    private byte[] publicUserKey;

    private boolean accepted = false;

    public byte[] getPublicUserKey() {
        return publicUserKey;
    }

    public void setPublicUserKey(byte[] publicUserKey) {
        this.publicUserKey = publicUserKey;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
