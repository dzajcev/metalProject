package ru.metal.security.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by User on 11.09.2017.
 */
public class UserContextHolder {
    public static final String DELEGATED_USER_KEY = UserContextHolder.class + "$user";

    private static ThreadLocal<PermissionContextData> permissionContextDataThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<java.security.KeyPair> keyPairThreadLocal = new ThreadLocal<>();

    public static PermissionContextData getPermissionContextDataThreadLocal() {
        return permissionContextDataThreadLocal.get();
    }

    public static void setUserPermissionDataThreadLocal(PermissionContextData name) {
        permissionContextDataThreadLocal.set(name);
    }

    public static PrivateKey getPrivateKey() {
        return keyPairThreadLocal.get().getPrivate();
    }

    public static PublicKey getPublicKey() {
        return keyPairThreadLocal.get().getPublic();
    }

    public static void loadKeyPair(byte[] privateKey, byte[] publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                publicKey);
        PublicKey publicKey1 = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKey1 = keyFactory.generatePrivate(privateKeySpec);

        keyPairThreadLocal.set(new KeyPair(publicKey1, privateKey1));
    }

    public static void loadKeyPair(File privateKey, File publicKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Read Public Key.
        FileInputStream fis = new FileInputStream(publicKey);
        byte[] encodedPublicKey = new byte[(int) publicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        fis = new FileInputStream(privateKey);
        byte[] encodedPrivateKey = new byte[(int) privateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        loadKeyPair(encodedPrivateKey, encodedPublicKey);
    }

    public static void loadKeyPair(String privateKeyPath, String publicKeyPath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Read Public Key.
        File filePublicKey = new File(publicKeyPath);
        FileInputStream fis = new FileInputStream(publicKeyPath);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(privateKeyPath);
        fis = new FileInputStream(privateKeyPath);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        loadKeyPair(encodedPrivateKey, encodedPublicKey);
    }
}
