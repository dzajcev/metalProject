package ru.common.api.dto;

/**
 * Created by User on 09.08.2017.
 */
public interface TreeviewElement {

    String getGuid();

    String getGroupGuid();

    String getName();

    boolean getActive();

    void setGuid(String guid);

    void setGroupGuid(String groupGuid);

    void setName(String name);

    void setActive(boolean active);

}
