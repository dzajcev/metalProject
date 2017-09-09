package ru.metal.api.common.dto;

import ru.metal.api.OperationErrorCode;

import java.io.Serializable;

/**
 * Created by User on 16.08.2017.
 */
public class Error implements Serializable {
    private String code;

    private String description;
    public Error(){
    }
    public Error(OperationErrorCode operationErrorCode){
        this.code=operationErrorCode.getCode();
        this.description=operationErrorCode.getPattern();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}