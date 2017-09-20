package ru.metal.crypto.service;

import ru.common.api.dto.Pair;

import javax.inject.Named;
import javax.inject.Singleton;
import java.security.*;

/**
 * Created by User on 20.09.2017.
 */
@Named
@Singleton
public class KeyGeneratorImpl implements KeyGenerator{
    @Override
    public Pair<PublicKey, PrivateKey> generate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return new Pair<>(keyPair.getPublic(),keyPair.getPrivate());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
}
