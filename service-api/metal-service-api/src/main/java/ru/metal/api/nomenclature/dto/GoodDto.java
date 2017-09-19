package ru.metal.api.nomenclature.dto;

import ru.metal.security.ejb.dto.AbstractDto;
import ru.common.api.dto.TableElement;

/**
 * Created by User on 09.08.2017.
 */

public class GoodDto extends AbstractDto implements TableElement<GroupDto> {

    private String name;

    private GroupDto group;

    private OkeiDto okei;

    private Integer nds;

    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupDto getGroup() {
        return group;
    }

    public void setGroup(GroupDto group) {
        this.group = group;
    }

    public OkeiDto getOkei() {
        return okei;
    }

    public void setOkei(OkeiDto okei) {
        this.okei = okei;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getNds() {
        return nds;
    }

    public void setNds(Integer nds) {
        this.nds = nds;
    }
}
