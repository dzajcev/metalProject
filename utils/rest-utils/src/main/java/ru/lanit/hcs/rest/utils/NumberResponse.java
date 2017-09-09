package ru.lanit.hcs.rest.utils;

import java.io.Serializable;

public class NumberResponse implements Serializable {

    private static final long serialVersionUID = 6287472976417081027L;
    private String number;

    public NumberResponse() {}


    public NumberResponse(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
