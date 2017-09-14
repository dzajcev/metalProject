package ru.lanit.hcs.rest.utils;

/**
 * Created by User on 13.09.2017.
 */
public class ExceptionUtils {

    public static Throwable containThrowable(Throwable source,  Class<? extends Exception> search){
        if (source.getClass()==search){
            return source;
        }else if (source.getCause()==null){
            return null;
        }else{
            return containThrowable(source.getCause(),search);
        }
    }
}
