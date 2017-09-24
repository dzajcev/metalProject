package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.security.ejb.dto.User;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.DelegateUser;
import ru.metal.security.ejb.security.DelegatingUser;

/**
 * Created by User on 19.09.2017.
 */
public class UserFx extends FxEntity<User> {

    @ValidatableField(nullable = false, regexp = Formats.LOGIN_FORMAT)
    private StringProperty login = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.USERNAME_FORMAT)
    private StringProperty secondName = new SimpleStringProperty();

    private StringProperty middleName = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.USERNAME_FORMAT)
    private StringProperty firstName = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.EMAIL_FORMAT)
    private StringProperty email = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private StringProperty token = new SimpleStringProperty();

    //делегирующие юзеры
    private ObservableList<DelegatingUser> donorRights =FXCollections.observableArrayList();

    //кому делегирую права
    private ObservableList<DelegateUser> consumersRights =FXCollections.observableArrayList();

    private BooleanProperty active = new SimpleBooleanProperty(true);

    private ObservableList<Role> roles = FXCollections.observableArrayList();

    private ObservableList<Privilege> privileges = FXCollections.observableArrayList();


    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getSecondName() {
        return secondName.get();
    }

    public StringProperty secondNameProperty() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName.set(secondName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getToken() {
        return token.get();
    }

    public StringProperty tokenProperty() {
        return token;
    }

    public void setToken(String token) {
        this.token.set(token);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public ObservableList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ObservableList<Role> roles) {
        this.roles = roles;
    }

    public ObservableList<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(ObservableList<Privilege> privileges) {
        this.privileges = privileges;
    }

    public ObservableList<DelegatingUser> getDonorRights() {
        return donorRights;
    }

    public void setDonorRights(ObservableList<DelegatingUser> donorRights) {
        this.donorRights = donorRights;
    }

    public ObservableList<DelegateUser> getConsumersRights() {
        return consumersRights;
    }

    public void setConsumersRights(ObservableList<DelegateUser> consumersRights) {
        this.consumersRights = consumersRights;
    }

    public String getShortName() {
        StringBuilder stringBuilder = new StringBuilder(getSecondName()).append(" ");
        stringBuilder.append(getFirstName().substring(0, 1)).append(". ");
        if (getMiddleName() != null) {
            stringBuilder.append(getMiddleName().substring(0, 1)).append(". ");
        }
        return stringBuilder.toString();
    }

    @Override
    public User getEntity() {
        User user = new User();
        user.setGuid(getGuid());
        user.setTransportGuid(getTransportGuid());
        user.setLastEditingDate(getLastEditingDate());

        user.setLogin(getLogin());
        user.setFirstName(getFirstName());
        user.setMiddleName(getMiddleName());
        user.setSecondName(getSecondName());
        user.setEmail(getEmail());
        user.setToken(getToken());
        user.setRoles(roles);
        user.setPrivileges(privileges);
        user.setActive(isActive());
        user.setConsumersRights(getConsumersRights());
        user.setDonorRights(getDonorRights());
        return user;
    }

    @Override
    public FxHelper<FxEntity<User>, User> getHelper() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFx)) return false;

        UserFx userFx = (UserFx) o;

        return getGuid() != null ? getGuid().equals(userFx.getGuid()) : userFx.getGuid() == null;
    }

    @Override
    public int hashCode() {
        return getGuid() != null ? getGuid().hashCode() : 0;
    }
}

