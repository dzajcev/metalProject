package ru.metal.api.auth.dto;

import java.io.Serializable;

/**
 * Created by User on 11.09.2017.
 */
public class KeyPair implements Serializable {

    private byte[] privateKey;

    private byte[] publicKey;

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
