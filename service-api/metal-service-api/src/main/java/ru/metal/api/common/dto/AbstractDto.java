package ru.metal.api.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 05.08.2017.
 */
public abstract class AbstractDto implements Serializable{

    private String transportGuid;
    private String guid;
    private Date lastEditingDate;


    public AbstractDto(){
        transportGuid= UUID.randomUUID().toString();
    }
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

    public Date getLastEditingDate() {
        return lastEditingDate;
    }

    public void setLastEditingDate(Date lastEditingDate) {
        this.lastEditingDate = lastEditingDate;
    }
}
