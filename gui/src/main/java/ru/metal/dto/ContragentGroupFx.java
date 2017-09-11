package ru.metal.dto;

import ru.common.api.dto.TreeviewElement;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.dto.helper.ContragentGroupHelper;
import ru.metal.dto.helper.FxHelper;

/**
 * Created by User on 29.08.2017.
 */
public class ContragentGroupFx extends FxEntity<ContragentGroupDto> implements TreeviewElement {

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

    @Override
    public ContragentGroupDto getEntity() {
        ContragentGroupDto contragentGroup = new ContragentGroupDto();
        contragentGroup.setActive(getActive());
        contragentGroup.setGuid(getGuid());
        contragentGroup.setLastEditingDate(getLastEditingDate());
        contragentGroup.setTransportGuid(getTransportGuid());

        contragentGroup.setActive(getActive());
        contragentGroup.setGroupGuid(getGroupGuid());
        contragentGroup.setName(getName());
        return contragentGroup;
    }

    @Override
    public FxHelper<ContragentGroupFx, ContragentGroupDto> getHelper() {
        return ContragentGroupHelper.getInstance();
    }
}
