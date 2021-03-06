package ru.metal.auth.impl.domain.persistent;

import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by User on 11.09.2017.
 */
@Entity
@Table(name = "USERS", uniqueConstraints = {@UniqueConstraint(columnNames = {"LOGIN"}),
        @UniqueConstraint(columnNames = {"EMAIL"})})
public class UserData extends BaseEntity {

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "SECOND_NAME", nullable = false)
    private String secondName;

    @Column(name = "MIDDLE_NAME", nullable = false)
    private String middleName;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    //кто делегирует
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "DELEGATORS_USERS")
    @Column(name = "USER_GUID", nullable = false)
    private Set<String> donorRights;

    //кому делегируем
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "DELEGATE_USERS")
    @Column(name = "USER_GUID", nullable = false)
    private Set<String> consumersRights;

    @Column(name = "TO_CHANGE_PASSWORD")
    private boolean toChangePassword=false;

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


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public boolean getToChangePassword() {
        return toChangePassword;
    }

    public void setToChangePassword(boolean toChangePassword) {
        this.toChangePassword = toChangePassword;
    }

    public Set<String> getConsumersRights() {
        if (consumersRights ==null){
            consumersRights =new HashSet<>();
        }
        return consumersRights;
    }

    public void setConsumersRights(Set<String> consumersRights) {
        this.consumersRights = consumersRights;
    }

    public Set<String> getDonorRights() {
        if (donorRights ==null){
            donorRights =new HashSet<>();
        }
        return donorRights;
    }

    public void setDonorRights(Set<String> donorRights) {
        this.donorRights = donorRights;
    }

    public String getShortName() {
        StringBuilder stringBuilder = new StringBuilder(getSecondName()).append(" ");
        stringBuilder.append(getFirstName().substring(0, 1)).append(". ");
        if (getMiddleName() != null) {
            stringBuilder.append(getMiddleName().substring(0, 1)).append(". ");
        }
        return stringBuilder.toString();
    }
}
