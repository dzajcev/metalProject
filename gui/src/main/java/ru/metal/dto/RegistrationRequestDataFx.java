package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.RegistrationRequestDataHelper;

/**
 * Created by User on 18.09.2017.
 */
public class RegistrationRequestDataFx extends FxEntity<RegistrationData> {

    private StringProperty login=new SimpleStringProperty();

    private StringProperty secondName=new SimpleStringProperty();

    private StringProperty middleName=new SimpleStringProperty();

    private StringProperty firstName=new SimpleStringProperty();

    private StringProperty email=new SimpleStringProperty();

    private StringProperty token=new SimpleStringProperty();

    private byte[] publicUserKey;

    private BooleanProperty accepted =new SimpleBooleanProperty(false);

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

    public boolean isAccepted() {
        return accepted.get();
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted.set(accepted);
    }

    @Override
    public RegistrationData getEntity() {
        RegistrationData data=new RegistrationData();
        data.setPublicUserKey(publicUserKey);
        data.setAccepted(isAccepted());
        data.setEmail(getEmail());
        data.setFirstName(getFirstName());
        data.setLogin(getLogin());
        data.setMiddleName(getMiddleName());
        data.setSecondName(getSecondName());
        data.setToken(getToken());
        data.setGuid(getGuid());
        data.setLastEditingDate(getLastEditingDate());
        data.setTransportGuid(getTransportGuid());
        return null;
    }

    @Override
    public FxHelper<RegistrationRequestDataFx, RegistrationData> getHelper() {
        return RegistrationRequestDataHelper.getInstance();
    }
}
