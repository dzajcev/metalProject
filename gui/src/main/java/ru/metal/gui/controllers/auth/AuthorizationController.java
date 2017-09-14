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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.crypto.ejb.UserContextHolder;
import ru.metal.crypto.service.CryptoException;
import ru.metal.exceptions.ExceptionUtils;
import ru.metal.gui.StartPage;
import ru.metal.gui.windows.LabelButton;
import ru.metal.rest.ApplicationSettingsSingleton;
import ru.metal.rest.AuthorizationClient;

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
    private PasswordField pass;
    @FXML
    private Label message;
    @FXML
    private LabelButton privateKey;
    @FXML
    private LabelButton publicKey;

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
        registration.setText("Регистрация");
        registration.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage=(Stage)root.getScene().getWindow();
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
                    registerController.setAuthRoot((Parent) root);
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
        UserContextHolder.setUserPermissionDataThreadLocal(null);
        if (login.getText().isEmpty()) {
            message.setText("Логин не введен");
            return;
        }
        if (pass.getText().isEmpty()) {
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
            byte[] bytesOfMessage = (login.getText() + "_" + pass.getText()).getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesOfMessage);
            request.setToken(Hex.encodeHexString(theDigest));
            try {
                UserContextHolder.loadKeyPair(closeKey, openKey);
            } catch (IOException e) {
                message.setText("Произошла ошибка открытия файла ключа");
                return;
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                message.setText("Произошла ошибка формирования файла ключа");
                return;
            }

            AuthorizationResponse login = authorizationClient.login(request);
            if (login.getPermissionContextData() != null) {
                UserContextHolder.setUserPermissionDataThreadLocal(login.getPermissionContextData());
                done.set(AUTHORIZATION_ACCEPT);
            } else if (!login.getErrors().isEmpty()) {
                message.setText(login.getErrors().iterator().next().getDescription());
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            message.setText("Произошла непредвиденная ошибка");
        } catch (RuntimeException e) {
            if (ExceptionUtils.containThrowable(e, CryptoException.class)) {
                message.setText("Ошибка шифрования данных. Скорее всего используется не верный ключ");
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
}
