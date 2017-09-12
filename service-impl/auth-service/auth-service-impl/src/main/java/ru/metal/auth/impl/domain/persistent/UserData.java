package ru.metal.auth.impl.domain.persistent;

import ru.metal.crypto.ejb.dto.Privilege;
import ru.metal.crypto.ejb.dto.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
@Entity
@Table(name = "USERS", uniqueConstraints = {@UniqueConstraint(columnNames = {"LOGIN"}),
        @UniqueConstraint(columnNames = {"EMAIL"})})
public class UserData extends BaseEntity {

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "SECOND_NAME", nullable = false)
    private String secondName;

    @Column(name = "MIDDLE_NAME", nullable = false)
    private String middleName;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "POSITION")
    private Position position;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "PUBLIC_USER_KEY", nullable = false)
    private byte[] publicUserKey;

    @Column(name = "PRIVATE_SERVER_KEY", nullable = false)
    private byte[] privateServerKey;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "Roles")
    @Column(name = "ROLE_GUID", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @ElementCollection(targetClass = Privilege.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "PRIVILEGES")
    @Column(name = "PRIVILEGE_GUID", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Privilege> privileges;

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public byte[] getPrivateServerKey() {
        return privateServerKey;
    }

    public void setPrivateServerKey(byte[] privateServerKey) {
        this.privateServerKey = privateServerKey;
    }

    public List<Role> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Privilege> getPrivileges() {
        if (privileges == null) {
            privileges = new ArrayList<>();
        }
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
