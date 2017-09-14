package ru.metal.gui.controllers.auth;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.gui.StartPage;
import ru.metal.rest.RegistrationClient;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 10.09.2017.
 */
public class RegisterController {
    private BooleanProperty done = new SimpleBooleanProperty();
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "private.key";
    public static final String PUBLIC_KEY_FILE = "public.key";

    @FXML
    private Parent root;

    private Parent authRoot;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField secondName;
    @FXML
    private TextField middleName;
    @FXML
    private TextField firstName;
    @FXML
    private TextField email;

    @FXML
    private PasswordField rePass;

    private RegistrationClient registrationClient;
    private Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @FXML
    private void initialize() {
        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                TextInputControl textInputControl = (TextInputControl) event.getSource();
                textInputControl.setStyle(null);
            }
        };
        login.setOnKeyTyped(eventHandler);
        secondName.setOnKeyTyped(eventHandler);
        firstName.setOnKeyTyped(eventHandler);
        middleName.setOnKeyTyped(eventHandler);
        email.setOnKeyTyped(eventHandler);
        pass.setOnKeyTyped(eventHandler);
        rePass.setOnKeyTyped(eventHandler);
    }

    @FXML
    private void register() {
        boolean error = false;
        if (login.getText().length() < 6) {
            login.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (secondName.getText().length() < 3) {
            secondName.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (firstName.getText().length() < 3) {
            firstName.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (middleName.getText().length() < 3) {
            middleName.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (email.getText().length() < 6) {
            email.setStyle("-fx-background-color: red;");
            error = true;
        } else {
            Matcher matcher = emailPattern.matcher(email.getText());
            if (!matcher.find()) {
                email.setStyle("-fx-background-color: red;");
                error = true;
            }
        }
        if (pass.getText().length() < 1) {
            pass.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (rePass.getText().length() < 1) {
            rePass.setStyle("-fx-background-color: red;");
            error = true;
        }
        if (!pass.getText().equals(rePass.getText())) {
            rePass.setStyle("-fx-background-color: red;");
            pass.setStyle("-fx-background-color: red;");
            error = true;
        } else {
            pass.setStyle(null);
            rePass.setStyle(null);
        }
        if (error) {
            return;
        }
        registrationClient = new RegistrationClient();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            RegistrationRequest registrationRequest = new RegistrationRequest();

            RegistrationData registrationData = new RegistrationData();
            registrationData.setLogin(login.getText());
            registrationData.setFirstName(firstName.getText());
            registrationData.setSecondName(secondName.getText());
            registrationData.setMiddleName(middleName.getText());
            registrationData.setEmail(email.getText());
            try {
                byte[] bytesOfMessage = (login.getText() + "_" + pass.getText()).getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theDigest = md.digest(bytesOfMessage);
                registrationData.setToken(Hex.encodeHexString(theDigest));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            registrationData.setPublicUserKey(publicKey.getEncoded());
            registrationRequest.setRegistrationData(registrationData);

            RegistrationResponse registration = registrationClient.createRegistration(registrationRequest);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("privateKey");
            FileChooser.ExtensionFilter key = new FileChooser.ExtensionFilter("Файл ключа", "*.key");
            fileChooser.getExtensionFilters().addAll(key);
            fileChooser.setSelectedExtensionFilter(key);
            fileChooser.setTitle("Сохраните закрытый ключ");

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            File privateKeyFile = fileChooser.showSaveDialog(root.getScene().getWindow());

            try {
                FileOutputStream fos = new FileOutputStream(privateKeyFile);
                fos.write(pkcs8EncodedKeySpec.getEncoded());
                fos.close();
            } catch (IOException e) {

            }
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Регистрация");
            alert.setHeaderText("После подтверждения регистрации Вам придет на почту открытый ключ.");
            alert.initOwner(StartPage.primaryStage);
            alert.setContentText("Сохраните его");
            alert.showAndWait();
            Platform.exit();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(Serializable serializable, PublicKey key) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(SerializationUtils.serialize(serializable));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Авторизация");
        root.getScene().setRoot(authRoot);
    }

    public boolean isDone() {
        return done.get();
    }

    public BooleanProperty doneProperty() {
        return done;
    }

    public void setDone(boolean done) {
        this.done.set(done);
    }

    public void setAuthRoot(Parent authRoot) {
        this.authRoot = authRoot;
    }
}
