package ru.lanit.hcs.rest.utils;

import java.io.Serializable;
import java.util.Map;

public class JsonResponseModel implements Serializable {
    private String title;

    private Map<String, Object> details;

    public JsonResponseModel() {
    }

    public JsonResponseModel(String title) {
        this.title = title;
    }

    public JsonResponseModel(String titls, Map<String, Object> details) {
        this.title = titls;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
