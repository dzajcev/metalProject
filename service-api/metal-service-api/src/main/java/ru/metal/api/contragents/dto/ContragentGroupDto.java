package ru.metal.api.contragents.dto;

import ru.metal.crypto.ejb.dto.AbstractDto;
import ru.common.api.dto.TreeviewElement;

/**
 * Created by User on 29.08.2017.
 */
public class ContragentGroupDto extends AbstractDto implements TreeviewElement {

    private String groupGuid;

    private String name;

    private boolean active = true;

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
