package ru.metal.email;

import javax.annotation.Resource;
import javax.ejb.Stateless;

/**
 * Created by User on 18.09.2017.
 */
@Stateless
public class EmailProperty {
    @Resource(lookup = "java:global/smtpHost")
    private String smtpHost;
    @Resource(lookup = "java:global/smtpPort")
    private Integer smtpPort;
    @Resource(lookup = "java:global/from")
    private String from;
    @Resource(lookup = "java:global/userName")
    private String userName;
    @Resource(lookup = "java:global/password")
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