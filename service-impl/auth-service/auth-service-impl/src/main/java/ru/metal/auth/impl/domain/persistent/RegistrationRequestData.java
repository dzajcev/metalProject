package ru.metal.auth.impl.domain.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by User on 11.09.2017.
 */
@Entity
@Table(name = "REGISTRATION_REQUESTS",uniqueConstraints = {@UniqueConstraint(columnNames={"LOGIN",}),
                                                            @UniqueConstraint(columnNames={"EMAIL"})})
public class RegistrationRequestData extends BaseEntity {

    @Column(name = "LOGIN",nullable = false)
    private String login;

    @Column(name = "SECOND_NAME",nullable = false)
    private String secondName;

    @Column(name = "MIDDLE_NAME",nullable = false)
    private String middleName;

    @Column(name = "FIRST_NAME",nullable = false)
    private String firstName;

    @Column(name = "EMAIL",nullable = false)
    private String email;

    @Column(name = "TOKEN",nullable = false)
    private String token;

    @Column(name = "PUBLIC_USER_KEY",nullable = false)
    private byte[] publicUserKey;

    @Column(name = "ACCEPTED")
    private boolean accepted;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getPublicUserKey() {
        return publicUserKey;
    }

    public void setPublicUserKey(byte[] publicUserKey) {
        this.publicUserKey = publicUserKey;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
