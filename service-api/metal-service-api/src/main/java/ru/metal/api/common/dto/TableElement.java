package ru.metal.api.common.dto;

import ru.metal.api.nomenclature.dto.GroupDto;

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
