package ru.metal.exceptions;

/**
 * Created by User on 13.09.2017.
 */
public class ExceptionUtils {

    public static boolean containThrowable(Throwable source,  Class<? extends Exception> search){
        if (source.getClass()==search){
            return true;
        }else if (source.getCause()==null){
            return false;
        }else{
            return containThrowable(source.getCause(),search);
        }
    }
}
