package ru.metal.crypto.service;

import java.io.Serializable;

/**
 * Created by User on 11.09.2017.
 */
public class RestEntity implements Serializable {
    private String userGuid;

    private CryptoPacket cryptoPacket;

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public CryptoPacket getCryptoPacket() {
        return cryptoPacket;
    }

    public void setCryptoPacket(CryptoPacket cryptoPacket) {
        this.cryptoPacket = cryptoPacket;
    }
}
