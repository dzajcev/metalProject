package ru.metal.dto;

import ru.metal.api.common.dto.TreeviewElement;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.GoodGroupHelper;

/**
 * Created by User on 09.08.2017.
 */

public class GroupFx extends FxEntity<GroupDto> implements TreeviewElement{

    private String groupGuid;

    private String name;

    private boolean active=true;

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
        this.active=active;
    }

    @Override
    public GroupDto getEntity() {
        GroupDto groupDto=new GroupDto();
        groupDto.setActive(getActive());
        groupDto.setGuid(getGuid());
        groupDto.setLastEditingDate(getLastEditingDate());
        groupDto.setTransportGuid(getTransportGuid());

        groupDto.setActive(getActive());
        groupDto.setGroupGuid(getGroupGuid());
        groupDto.setName(getName());
        return groupDto;
    }

    @Override
    public FxHelper<GroupFx, GroupDto> getHelper() {
        return GoodGroupHelper.getInstance();
    }
}
