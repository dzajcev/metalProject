package ru.metal.exceptions;

/**
 * Created by User on 09.08.2017.
 */
public class ServerErrorException extends Exception {
    public ServerErrorException(Throwable e) {
        super(e);
    }
}
