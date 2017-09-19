package ru.common.api.dto;

import ru.metal.security.ejb.dto.AbstractDto;

/**
 * Created by User on 18.09.2017.
 */
public class EmailSettings extends AbstractDto {
    private String smtpHost;
    private Integer smtpPort;
    private String from;
    private String userName;
    private String password;

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
