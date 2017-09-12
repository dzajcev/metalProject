package ru.metal.gui.controllers.auth;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.exceptions.ServerErrorException;
import ru.metal.rest.RegistrationClient;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by User on 10.09.2017.
 */
public class RegisterController {
    private BooleanProperty done = new SimpleBooleanProperty();
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "private.key";
    public static final String PUBLIC_KEY_FILE = "public.key";

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

    @FXML
    private void register() {
        registrationClient=new RegistrationClient();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            RegistrationRequest registrationRequest = new RegistrationRequest();

            RegistrationData registrationData=new RegistrationData();
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
//            final AsymmetricCipher cipher = new AsymmetricCipher();
//            final CryptoPacket cryptoPacket = cipher.encrypt(SerializationUtils.serialize(registrationData), publicKey);
            registrationData.setPublicUserKey(publicKey.getEncoded());
            registrationRequest.setRegistrationData(registrationData);

            RegistrationResponse registration = registrationClient.createRegistration(registrationRequest);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            try {
                FileOutputStream fos = new FileOutputStream("d:\\private.key");
                fos.write(pkcs8EncodedKeySpec.getEncoded());
                fos.close();
            }catch (IOException e){

            }
            done.set(true);
        } catch (ServerErrorException |NoSuchAlgorithmException e) {
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
}
