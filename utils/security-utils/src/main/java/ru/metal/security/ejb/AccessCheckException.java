package ru.metal.security.ejb;

/**
 * Created by User on 18.09.2017.
 */
public class AccessCheckException extends RuntimeException {

    public AccessCheckException() {
    }

    public AccessCheckException(String message) {
        super(message);
    }

    public AccessCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
