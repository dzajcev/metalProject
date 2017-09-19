package ru.metal.gui.controllers.auth;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.ChangePasswordRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.ChangePasswordResponse;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.crypto.service.CryptoException;
import ru.metal.exceptions.ExceptionUtils;
import ru.metal.gui.StartPage;
import ru.metal.gui.windows.LabelButton;
import ru.metal.rest.ApplicationSettingsSingleton;
import ru.metal.rest.AuthorizationClient;

import javax.ws.rs.ProcessingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationController {
    public final static String AUTHORIZATION_ACCEPT = "accept";
    private StringProperty done = new SimpleStringProperty();
    private BooleanProperty retry = new SimpleBooleanProperty(false);
    @FXML
    private AnchorPane root;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;
    @FXML
    private LabelButton privateKey;
    @FXML
    private LabelButton publicKey;

    @FXML
    private TextField serverPath;

    @FXML
    private TextField privateKeyText;
    @FXML
    private TextField publicKeyText;

    private FileChooser fileChooser = new FileChooser();

    private File lastDirectory;

    @FXML
    private Button registration;

    @FXML
    private void initialize() {
        FileChooser.ExtensionFilter key = new FileChooser.ExtensionFilter("Файл ключа", "*.key");
        fileChooser.getExtensionFilters().addAll(key);
        fileChooser.setSelectedExtensionFilter(key);
        privateKey.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        publicKey.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        message.setTextFill(Color.RED);
        privateKeyText.setText(ApplicationSettingsSingleton.getInstance().getPrivateKey());
        publicKeyText.setText(ApplicationSettingsSingleton.getInstance().getPublicKey());
        serverPath.setText(ApplicationSettingsSingleton.getInstance().getServerAddress());
        registration.setText("Регистрация");
        registration.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setTitle("Регистрация");
                FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/Register.fxml"));

                Node load = null;
                RegisterController registerController = null;
                try {
                    load = loader.load();
                    registerController = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (load != null) {
                    registerController.setAuthRoot(root);
                    root.getScene().setRoot((Parent) load);
                }
            }
        });

        retry.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    registration.setText("Отмена");
                    registration.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Platform.exit();
                        }
                    });
                }
            }
        });

    }

    @FXML
    private void openPrivateKey() {
        fileChooser.setInitialDirectory(lastDirectory);
        fileChooser.setTitle("Выберите закрытый ключ");
        File closeKey = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (closeKey != null) {
            lastDirectory = closeKey.getParentFile();
        }
        privateKeyText.setText(closeKey.getAbsolutePath());
    }

    @FXML
    private void openPublicKey() {
        fileChooser.setInitialDirectory(lastDirectory);
        fileChooser.setTitle("Выберите открытый ключ");
        File openKey = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (openKey != null) {
            lastDirectory = openKey.getParentFile();
        }
        publicKeyText.setText(openKey.getAbsolutePath());
    }

    @FXML
    private void login() {
        ApplicationSettingsSingleton.getInstance().setServerAddress(serverPath.getText());
        UserContextHolder.setUserPermissionDataThreadLocal(null);
        if (login.getText().isEmpty()) {
            message.setText("Логин не введен");
            return;
        }
        if (password.getText().isEmpty()) {
            message.setText("Пароль не введен");
            return;
        }
        File openKey = new File(publicKeyText.getText());
        if (!openKey.exists()) {
            message.setText("Открытый ключ не найден");
            return;
        }
        File closeKey = new File(privateKeyText.getText());
        if (!closeKey.exists()) {
            message.setText("Закрытый ключ не найден");
            return;
        }
        AuthorizationClient authorizationClient = new AuthorizationClient();
        try {
            AuthorizationRequest request = new AuthorizationRequest();
            request.setToken(getToken(login.getText(), password.getText()));
            try {
                UserContextHolder.loadKeyPair(closeKey, openKey);
            } catch (IOException e) {
                message.setText("Произошла ошибка открытия файла ключа");
                return;
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                message.setText("Произошла ошибка формирования файла ключа");
                return;
            }

            AuthorizationResponse authorizationResponse = authorizationClient.login(request);
            if (authorizationResponse.getPermissionContextData() != null) {
                if (authorizationResponse.getPermissionContextData().isToChangePassword()) {
                    AnchorPane lockPane = new AnchorPane();
                    AnchorPane.setLeftAnchor(lockPane, 0.0);
                    AnchorPane.setRightAnchor(lockPane, 0.0);
                    AnchorPane.setBottomAnchor(lockPane, 0.0);
                    AnchorPane.setTopAnchor(lockPane, 0.0);
                    AnchorPane pane = new AnchorPane();

                    lockPane.getChildren().add(pane);
                    Label title = new Label("Необходима смена пароля");
                    title.setPrefHeight(30);
                    title.setTextFill(Color.RED);
                    AnchorPane.setLeftAnchor(title, 0.0);
                    AnchorPane.setRightAnchor(title, 0.0);
                    title.setAlignment(Pos.CENTER);
                    pane.getChildren().add(title);
                    pane.setStyle("-fx-background-color: lightgrey; -fx-border-color: grey;-fx-border-radius:  5");
                    pane.setPrefWidth(340);
                    pane.setPrefHeight(160);
                    pane.setLayoutY(50);
                    pane.setLayoutX(10);
                    Label passL = new Label("Введите новый пароль");
                    passL.setLayoutX(5);
                    passL.setLayoutY(45);
                    Label repassL = new Label("Подтверите новый пароль");
                    repassL.setLayoutX(5);
                    repassL.setLayoutY(85);

                    TextField pass = new PasswordField();
                    pass.setPrefWidth(160);
                    pass.setLayoutX(170);
                    pass.setLayoutY(40);

                    TextField rePass = new PasswordField();
                    rePass.setPrefWidth(160);
                    rePass.setLayoutX(170);
                    rePass.setLayoutY(80);


                    Button save = new Button("Сохранить");
                    save.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                            changePasswordRequest.setToken(authorizationResponse.getPermissionContextData().getToken());
                            changePasswordRequest.setNewToken(getToken(login.getText(), pass.getText()));
                            ChangePasswordResponse changePasswordResponse = authorizationClient.changePassword(changePasswordRequest);
                            if (changePasswordResponse.getErrors().isEmpty()){
                                password.clear();
                                root.getChildren().remove(lockPane);
                            }else{
                                Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
                                alert.setTitle("Смена пароля");
                                alert.setHeaderText("Произошла ошибка при смене пароля.");
                                alert.initOwner(StartPage.primaryStage);
                                alert.setContentText(changePasswordResponse.getErrors().get(0).getDescription());
                                alert.showAndWait();
                            }
                        }
                    });
                    save.setLayoutX(230);
                    save.setLayoutY(120);
                    save.setPrefWidth(100);

                    Button cancel = new Button("Отмена");
                    cancel.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Platform.exit();
                        }
                    });
                    cancel.setLayoutX(5);
                    cancel.setLayoutY(120);
                    cancel.setPrefWidth(100);

                    pane.getChildren().addAll(passL, repassL, pass, rePass, save, cancel);
                    root.getChildren().add(lockPane);
                } else {
                    UserContextHolder.setUserPermissionDataThreadLocal(authorizationResponse.getPermissionContextData());
                    done.set(AUTHORIZATION_ACCEPT);
                }
            } else if (!authorizationResponse.getErrors().isEmpty()) {
                message.setText(authorizationResponse.getErrors().iterator().next().getDescription());
            }
        } catch (RuntimeException e) {
            if (ExceptionUtils.containThrowable(e, CryptoException.class)) {
                message.setText("Ошибка шифрования данных. Скорее всего используется не верный ключ");
            } else if (ExceptionUtils.containThrowable(e, ProcessingException.class)) {
                message.setText("Невозможно подключиться к серверу");

            } else {
                message.setText("Произошла непредвиденная ошибка");
            }
        }
        ApplicationSettingsSingleton.getInstance().setPublicKey(publicKeyText.getText());
        ApplicationSettingsSingleton.getInstance().setPrivateKey(privateKeyText.getText());
        ApplicationSettingsSingleton.getInstance().save();
    }

    public String getDone() {
        return done.get();
    }

    public StringProperty doneProperty() {
        return done;
    }

    public void setDone(String done) {
        this.done.set(done);
    }

    public boolean isRetry() {
        return retry.get();
    }

    public BooleanProperty retryProperty() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry.set(retry);
    }

    private String getToken(String login, String pass) {
        try {
            byte[] bytesOfMessage = (login + "_" + pass).getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesOfMessage);
            return Hex.encodeHexString(theDigest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
