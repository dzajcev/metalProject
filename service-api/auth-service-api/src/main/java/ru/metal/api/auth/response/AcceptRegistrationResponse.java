package ru.metal.api.auth.response;

import ru.common.api.dto.Error;
import ru.common.api.response.AbstractResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
public class AcceptRegistrationResponse extends AbstractResponse {

    private byte[] publicServerKey;

    public byte[] getPublicServerKey() {
        return publicServerKey;
    }

    public void setPublicServerKey(byte[] publicServerKey) {
        this.publicServerKey = publicServerKey;
    }
}
