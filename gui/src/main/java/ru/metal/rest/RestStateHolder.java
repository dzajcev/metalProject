package ru.metal.rest;

/**
 * Created by User on 14.09.2017.
 */
public class RestStateHolder {
    private static volatile RestStateHolder instance;

    private boolean fatalError;
    public static RestStateHolder getInstance() {
        RestStateHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (RestStateHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RestStateHolder();
                }
            }
        }
        return localInstance;
    }
    private RestStateHolder(){}

    public static void setInstance(RestStateHolder instance) {
        RestStateHolder.instance = instance;
    }

    public boolean hasFatalError() {
        return fatalError;
    }

    public void setHasFatalError(boolean fatalError) {
        this.fatalError = fatalError;
    }
}
