package ru.metal.api.common.request;

import java.io.Serializable;

/**
 * Created by User on 16.08.2017.
 */
public abstract class ObtainAbstractRequest extends AbstractRequest{
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
