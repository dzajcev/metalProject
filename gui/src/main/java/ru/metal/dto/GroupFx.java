package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.common.api.dto.TreeviewElement;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.GoodGroupHelper;

/**
 * Created by User on 09.08.2017.
 */

public class GroupFx extends FxEntity<GroupDto> implements TreeviewElement {

    private StringProperty groupGuid=new SimpleStringProperty();

    private StringProperty name=new SimpleStringProperty();

    private BooleanProperty active = new SimpleBooleanProperty(true);

    private StringProperty userGuid=new SimpleStringProperty();


    @Override
    public String getGroupGuid() {
        return groupGuid.get();
    }

    public StringProperty groupGuidProperty() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid.set(groupGuid);
    }

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUserGuid() {
        return userGuid.get();
    }

    public StringProperty userGuidProperty() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid.set(userGuid);
    }

    public boolean getActive() {
        return active.getValue();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    @Override
    public GroupDto getEntity() {
        GroupDto groupDto = new GroupDto();
        groupDto.setActive(getActive());
        groupDto.setGuid(getGuid());
        groupDto.setLastEditingDate(getLastEditingDate());
        groupDto.setTransportGuid(getTransportGuid());

        groupDto.setActive(getActive());
        groupDto.setGroupGuid(getGroupGuid());
        groupDto.setName(getName());
        groupDto.setUserGuid(getUserGuid());
        return groupDto;
    }

    @Override
    public FxHelper<GroupFx, GroupDto> getHelper() {
        return GoodGroupHelper.getInstance();
    }
}
