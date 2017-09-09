package ru.metal.api.nomenclature.dto;

import ru.metal.api.common.dto.TableElement;
import ru.metal.api.common.dto.TreeviewElement;
import ru.metal.api.common.dto.AbstractDto;

/**
 * Created by User on 09.08.2017.
 */

public class GroupDto extends AbstractDto implements TreeviewElement{

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
