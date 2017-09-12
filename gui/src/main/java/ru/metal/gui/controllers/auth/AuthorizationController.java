package ru.metal.gui.controllers.auth;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.codec.binary.Hex;
import ru.common.api.CryptoPacket;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.crypto.ejb.UserContextHolder;
import ru.metal.crypto.service.AsymmetricCipher;
import ru.metal.exceptions.ServerErrorException;
import ru.metal.gui.StartPage;
import ru.metal.rest.AuthorizationClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 10.09.2017.
 */
public class AuthorizationController {
    public final static String AUTHORIZATION_ACCEPT = "accept";
    public final static String AUTHORIZATION_DECLINE = "decline";

    private StringProperty done = new SimpleStringProperty();
    @FXML
    private AnchorPane root;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;


    @FXML
    private void openKey() {

    }

    @FXML
    private void login() {
        AuthorizationClient authorizationClient = new AuthorizationClient();
        try {
            AuthorizationRequest request = new AuthorizationRequest();
            byte[] bytesOfMessage = (login.getText() + "_" + pass.getText()).getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesOfMessage);
            request.setToken(Hex.encodeHexString(theDigest));

            UserContextHolder.loadKeyPair("d:\\private.key", "d:\\public.key");
            AuthorizationResponse login = authorizationClient.login(request);
            if (login.getPermissionContextData() != null) {
                UserContextHolder.setUserPermissionDataThreadLocal(login.getPermissionContextData());
                done.set(AUTHORIZATION_ACCEPT);
            }
//            String s="привет раиплвоатпжщвтпршоьавзлпрьбзщвоьрщшжаотзршльапзщрлощапоьр";
//            CryptoPacket cryptoPacket = AsymmetricCipher.ecryptPacket(s.getBytes(), UserContextHolder.getPublicKey());
//            Object o = AsymmetricCipher.decryptPacket(cryptoPacket, UserContextHolder.getPrivateKey());
//            System.out.println(o);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | ServerErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void register() {
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
            root.getChildren().add(load);
        }
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
}
