package ru.metal.api.auth.dto;

/**
 * Created by User on 11.09.2017.
 */
public class InnerUserInfo extends User {
    private byte[] publicUserKey;

    private byte[] privateServerKey;


    public byte[] getPublicUserKey() {
        return publicUserKey;
    }

    public void setPublicUserKey(byte[] publicUserKey) {
        this.publicUserKey = publicUserKey;
    }

    public byte[] getPrivateServerKey() {
        return privateServerKey;
    }

    public void setPrivateServerKey(byte[] privateServerKey) {
        this.privateServerKey = privateServerKey;
    }
}
