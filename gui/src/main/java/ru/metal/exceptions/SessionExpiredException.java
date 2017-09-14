package ru.metal.exceptions;

/**
 * Created by User on 14.09.2017.
 */
public class SessionExpiredException extends Exception {
    public SessionExpiredException(String message) {
        super(message);
    }
}
