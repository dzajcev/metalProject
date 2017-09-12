package ru.common.api.dto;

/**
 * Created by User on 09.08.2017.
 */
public interface TableElement<T extends TreeviewElement> {

    String getGuid();

    void  setGroup(T group);

    T getGroup();

    String getName();

    boolean getActive();

}
