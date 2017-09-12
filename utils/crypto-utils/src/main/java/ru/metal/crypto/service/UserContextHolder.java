package ru.metal.crypto.service;

import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.auth.dto.PermissionContextData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by User on 11.09.2017.
 */
public class UserContextHolder {
    public static final String DELEGATED_USER_KEY = UserContextHolder.class + "$user";

    private static ThreadLocal<PermissionContextData> permissionContextDataThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<KeyPair> keyPairThreadLocal = new ThreadLocal<>();

    public static PermissionContextData getPermissionContextDataThreadLocal() {
        return permissionContextDataThreadLocal.get();
    }

    public static void setUserPermissionDataThreadLocal(PermissionContextData name) {
        permissionContextDataThreadLocal.set(name);
    }

    private static PrivateKey getPrivateKey() {
        return keyPairThreadLocal.get().getPrivate();
    }

    private static PublicKey getPublicKey() {
        return keyPairThreadLocal.get().getPublic();
    }

    public static void loadKeyPair(byte[] privateKey, byte[] publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    publicKey);
            PublicKey publicKey1 = keyFactory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
            PrivateKey privateKey1 = keyFactory.generatePrivate(privateKeySpec);

            keyPairThreadLocal.set(new KeyPair(publicKey1, privateKey1));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void loadKeyPair(String privateKeyPath, String publicKeyPath) {
        try {
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
        } catch (IOException e) {

        }
    }

    public static CryptoPacket ecryptPacket(Serializable serializable) {
        final AsymmetricCipher cipher = new AsymmetricCipher();
        try {
            final CryptoPacket cryptoPacket = cipher.encrypt(SerializationUtils.serialize(serializable), getPublicKey());
            return cryptoPacket;
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T decryptPacket(CryptoPacket packet) {
        final AsymmetricCipher cipher = new AsymmetricCipher();
        try {
            byte[] decrypt = cipher.decrypt(packet, getPrivateKey());
            return SerializationUtils.deserialize(decrypt);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }
}
