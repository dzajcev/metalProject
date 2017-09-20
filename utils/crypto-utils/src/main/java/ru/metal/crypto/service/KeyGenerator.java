package ru.metal.crypto.service;

import ru.common.api.dto.Pair;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by User on 20.09.2017.
 */
public interface KeyGenerator {
    Pair<PublicKey, PrivateKey> generate();
}
