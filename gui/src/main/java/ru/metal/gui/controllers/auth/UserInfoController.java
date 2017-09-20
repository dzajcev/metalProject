package ru.metal.gui.controllers.auth;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.commons.codec.binary.Hex;
import ru.common.api.response.AbstractResponse;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.UpdateUserResponse;
import ru.metal.dto.RegistrationRequestDataFx;
import ru.metal.dto.UserFx;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.AdminClient;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

/**
 * Created by User on 19.09.2017.
 */
public class UserInfoController extends AbstractController {

    @FXML
    private SaveButton save;

    @FXML
    private CheckBox setPwd;

    @FXML
    private CheckBox newKeys;

    @FXML
    private TextField login;

    @FXML
    private TextField firstName;

    @FXML
    private TextField middleName;

    @FXML
    private TextField secondName;

    @FXML
    private TextField email;

    @FXML
    private CheckBox active;

    @FXML
    private Label activeLabel;

    @FXML
    private TextField tmpPassword;

    @FXML
    private ListView<Role> availableRoles;

    @FXML
    private ListView<Role> currentRoles;

    @FXML
    private ListView<Privilege> availablePrivilege;

    @FXML
    private ListView<Privilege> currentPrivilege;


    private RegistrationRequestDataFx request;

    private UserFx user;

    private BooleanProperty saved = new SimpleBooleanProperty(false);


    Callback<ListView<Role>, ListCell<Role>> roleCallback = new Callback<ListView<Role>, ListCell<Role>>() {

        @Override
        public ListCell<Role> call(ListView<Role> p) {

            ListCell<Role> cell = new ListCell<Role>() {

                @Override
                protected void updateItem(Role t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t.getTitle());
                    } else {
                        setText(null);
                    }
                }

            };

            return cell;
        }
    };
    Callback<ListView<Privilege>, ListCell<Privilege>> privilegeCallback = new Callback<ListView<Privilege>, ListCell<Privilege>>() {

        @Override
        public ListCell<Privilege> call(ListView<Privilege> p) {

            ListCell<Privilege> cell = new ListCell<Privilege>() {

                @Override
                protected void updateItem(Privilege t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t.getTitle());
                    } else {
                        setText(null);
                    }
                }

            };

            return cell;
        }
    };

    private AdminClient adminClient;

    @FXML
    protected void saveAction(ActionEvent event) {

        boolean result = saveResult(true);
        if (result) {
            saved.set(true);
            setCloseRequest(true);
        }
    }

    public boolean isSaved() {
        return saved.get();
    }

    public BooleanProperty savedProperty() {
        return saved;
    }

    @Override
    protected AbstractResponse save() {

        if (request != null) {
            AcceptRegistrationRequest acceptRegistrationRequest = new AcceptRegistrationRequest();
            acceptRegistrationRequest.setRegistrationRequestGuid(request.getGuid());
            acceptRegistrationRequest.setPrivileges(currentPrivilege.getItems());
            acceptRegistrationRequest.setRoles(currentRoles.getItems());
            AcceptRegistrationResponse acceptRegistrationResponse = adminClient.acceptRegistration(acceptRegistrationRequest);
            return acceptRegistrationResponse;
        } else if (user != null) {

            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setNeedGenerateKeys(newKeys.isSelected());

            user.setPrivileges(currentPrivilege.getItems());
            user.setRoles(currentRoles.getItems());
            user.setLogin(login.getText());
            user.setFirstName(firstName.getText());
            user.setMiddleName(middleName.getText());
            user.setSecondName(secondName.getText());
            user.setEmail(email.getText());
            user.setActive(active.isSelected());
            updateUserRequest.setToChangePassword(setPwd.isSelected());
            if (setPwd.isSelected()) {

                try {
                    byte[] bytesOfMessage = (login.getText() + "_" + tmpPassword.getText()).getBytes("UTF-8");
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] theDigest = md.digest(bytesOfMessage);
                    user.setToken(Hex.encodeHexString(theDigest));
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            } else {
                user.setToken(user.getToken());
            }
            boolean hasError = user.hasError();
            if (hasError) {
                setError(login, "login", user);
                setError(firstName, "firstName", user);
                setError(secondName, "secondName", user);
                setError(email, "email", user);
                return null;
            }
            updateUserRequest.setUser(user.getEntity());
            UpdateUserResponse updateUserResponse = adminClient.updateUser(updateUserRequest);
            return updateUserResponse;
        }
        return null;
    }

    @FXML
    private void initialize() {
        adminClient = new AdminClient();
        availableRoles.setCellFactory(roleCallback);
        currentRoles.setCellFactory(roleCallback);
        availablePrivilege.setCellFactory(privilegeCallback);
        currentPrivilege.setCellFactory(privilegeCallback);
    }

    public void setUser(UserFx userFx) {
        this.user = userFx;
        login.setText(this.user.getLogin());
        firstName.setText(this.user.getFirstName());
        middleName.setText(this.user.getMiddleName());
        secondName.setText(this.user.getSecondName());
        email.setText(this.user.getEmail());
        active.setSelected(user.isActive());
        ObservableList<Role> availableRolesList = FXCollections.observableArrayList();
        ObservableList<Role> currentRolesList = FXCollections.observableArrayList();

        ObservableList<Privilege> availablePrivilegeList = FXCollections.observableArrayList();
        ObservableList<Privilege> currentPrivilegeList = FXCollections.observableArrayList();

        for (Role role : Role.values()) {
            if (userFx.getRoles().contains(role)) {
                currentRolesList.add(role);
            } else {
                availableRolesList.add(role);
            }
        }
        for (Privilege privilege : Privilege.values()) {
            if (userFx.getPrivileges().contains(privilege)) {
                currentPrivilegeList.add(privilege);
            } else {
                availablePrivilegeList.add(privilege);
            }
        }
        availableRoles.setItems(availableRolesList);
        availablePrivilege.setItems(availablePrivilegeList);
        currentRoles.setItems(currentRolesList);
        currentPrivilege.setItems(currentPrivilegeList);
        availableRoles.getItems().sort(new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        availablePrivilege.setItems(FXCollections.observableArrayList(Privilege.values()));
        availablePrivilege.getItems().sort(new Comparator<Privilege>() {
            @Override
            public int compare(Privilege o1, Privilege o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        registerControls();
    }

    public void setRegistrationRequest(RegistrationRequestDataFx request) {
        login.setDisable(true);
        firstName.setDisable(true);
        middleName.setDisable(true);
        secondName.setDisable(true);
        email.setDisable(true);

        tmpPassword.setDisable(true);
        setPwd.setDisable(true);
        newKeys.setDisable(true);
        this.request = request;
        active.setVisible(false);
        activeLabel.setVisible(false);
        login.setText(request.getLogin());
        firstName.setText(request.getFirstName());
        middleName.setText(request.getMiddleName());
        secondName.setText(request.getSecondName());
        email.setText(request.getEmail());

        availableRoles.setItems(FXCollections.observableArrayList(Role.values()));
        availableRoles.getItems().sort(new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        availablePrivilege.setItems(FXCollections.observableArrayList(Privilege.values()));
        availablePrivilege.getItems().sort(new Comparator<Privilege>() {
            @Override
            public int compare(Privilege o1, Privilege o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        registerControls();
    }

    private void registerControls() {
        registerControlsProperties(save, login.textProperty(), active.selectedProperty(),
                firstName.textProperty(), middleName.textProperty(), secondName.textProperty(), email.textProperty(), firstName.textProperty());
    }

    @FXML
    private void privilegeToRight() {
        if (availablePrivilege.getSelectionModel().getSelectedItem() != null) {
            Privilege selectedItem = availablePrivilege.getSelectionModel().getSelectedItem();
            availablePrivilege.getSelectionModel().clearSelection();
            availablePrivilege.getItems().remove(selectedItem);
            currentPrivilege.getItems().add(selectedItem);
        }
    }

    @FXML
    private void privilegeToLeft() {
        if (currentPrivilege.getSelectionModel().getSelectedItem() != null) {
            Privilege selectedItem = currentPrivilege.getSelectionModel().getSelectedItem();

            currentPrivilege.getSelectionModel().clearSelection();
            currentPrivilege.getItems().remove(selectedItem);
            availablePrivilege.getItems().add(selectedItem);
        }
    }


    @FXML
    private void roleToRight() {
        if (availableRoles.getSelectionModel().getSelectedItem() != null) {
            Role selectedItem = availableRoles.getSelectionModel().getSelectedItem();
            availableRoles.getSelectionModel().clearSelection();
            availableRoles.getItems().remove(selectedItem);
            currentRoles.getItems().add(selectedItem);
        }
    }

    @FXML
    private void roleToLeft() {
        if (currentRoles.getSelectionModel().getSelectedItem() != null) {
            Role selectedItem = currentRoles.getSelectionModel().getSelectedItem();

            currentRoles.getSelectionModel().clearSelection();
            currentRoles.getItems().remove(selectedItem);
            availableRoles.getItems().add(selectedItem);
        }
    }


}
