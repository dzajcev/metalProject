package ru.metal.exceptions;

/**
 * Created by User on 14.09.2017.
 */
public class FatalErrorException extends Exception {
    public FatalErrorException(String message) {
        super(message);
    }
}
