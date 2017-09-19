package ru.metal.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 14.09.2017.
 */
public class Email implements Serializable {


    private List<String> recipients;

    private String theme;

    private String message;

    private Map<String,byte[]> attachments;

    public List<String> getRecipients() {
        if (recipients==null){
            recipients=new ArrayList<>();
        }
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String,byte[]> getAttachments() {
        if (attachments==null){
            attachments=new HashMap<>();
        }
        return attachments;
    }

    public void setAttachments(Map<String,byte[]> attachments) {
        this.attachments = attachments;
    }

}
