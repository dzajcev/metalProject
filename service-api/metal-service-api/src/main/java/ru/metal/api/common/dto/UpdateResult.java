package ru.metal.api.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class UpdateResult implements Serializable{
    private String guid;

    private String transportGuid;

    private List<Error> errors;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTransportGuid() {
        return transportGuid;
    }

    public void setTransportGuid(String transportGuid) {
        this.transportGuid = transportGuid;
    }

    public List<Error> getErrors() {
        if (errors == null) {
            this.errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
