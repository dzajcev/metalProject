package ru.metal.gui.controllers.auth;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.auth.RegisterRequest;
import ru.metal.crypto.org.apache.commons.codec_1_9.binary.Base64;
import ru.metal.crypto.service.AsymmetricCipher;
import ru.metal.crypto.service.CryptoException;
import ru.metal.crypto.service.CryptoPacket;
import ru.metal.crypto.service.CryptoPacketConverter;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by User on 10.09.2017.
 */
public class RegisterController {

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

    private BooleanProperty done = new SimpleBooleanProperty();
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "private.key";
    public static final String PUBLIC_KEY_FILE = "public.key";
    @FXML
    private void register() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setLogin(login.getText());
            registerRequest.setFirstName(firstName.getText());
            registerRequest.setSecondName(secondName.getText());
            registerRequest.setMiddleName(middleName.getText());
            registerRequest.setEmail(email.getText());
            registerRequest.setPublicKey(SerializationUtils.serialize(publicKey));
            try {
                byte[] bytesOfMessage = (login.getText() + "_" + pass.getText()).getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theDigest = md.digest(bytesOfMessage);
                registerRequest.setToken(Hex.encodeHexString(theDigest));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            final AsymmetricCipher cipher = new AsymmetricCipher();

            // Encrypt the data and the random symmetric key.
            final CryptoPacket cryptoPacket = cipher.encrypt(SerializationUtils.serialize(registerRequest), privateKey);

//            // Convert the CryptoPacket into a Base64 String that can be readily reconstituted at the other end.
//            final CryptoPacketConverter cryptoPacketConverter = new CryptoPacketConverter();
//            final String base64EncryptedData = cryptoPacketConverter.convert(cryptoPacket);

            // Decrypt the Base64 encoded (and encrypted) String.
            final byte[] outputData = cipher.decrypt(cryptoPacket, publicKey);
            Object deserialize = SerializationUtils.deserialize(outputData);
            done.set(true);
        } catch (NoSuchAlgorithmException e) {

        } catch (CryptoException e) {
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
