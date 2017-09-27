package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.common.api.dto.TreeviewElement;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.dto.helper.ContragentGroupHelper;
import ru.metal.dto.helper.FxHelper;

/**
 * Created by User on 29.08.2017.
 */
public class ContragentGroupFx extends FxEntity<ContragentGroupDto> implements TreeviewElement {

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

    @Override
    public boolean getActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
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
        contragentGroup.setUserGuid(getUserGuid());
        return contragentGroup;
    }

    @Override
    public FxHelper<ContragentGroupFx, ContragentGroupDto> getHelper() {
        return ContragentGroupHelper.getInstance();
    }
}
