package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.dto.UserCommonData;
import ru.metal.dto.helper.FxHelper;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 19.09.2017.
 */
public class UserFx extends FxEntity<User> {

    private StringProperty login = new SimpleStringProperty();

    private StringProperty secondName = new SimpleStringProperty();

    private StringProperty middleName = new SimpleStringProperty();

    private StringProperty firstName = new SimpleStringProperty();

    private StringProperty email = new SimpleStringProperty();

    private StringProperty token = new SimpleStringProperty();

    private BooleanProperty active=new SimpleBooleanProperty(true);
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
        User user=new User();
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
        return user;
    }

    @Override
    public FxHelper<FxEntity<User>, User> getHelper() {
        return null;
    }
}

