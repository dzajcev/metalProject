package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.metal.api.common.dto.AbstractDto;
import ru.metal.api.nomenclature.dto.OkeiDto;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.OkeiHelper;

/**
 * Created by User on 09.08.2017.
 */
public class OkeiFx extends FxEntity<OkeiDto>{

    private StringProperty name=new SimpleStringProperty();

    private BooleanProperty Active=new SimpleBooleanProperty();

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isActive() {
        return Active.get();
    }

    public BooleanProperty activeProperty() {
        return Active;
    }

    public void setActive(boolean active) {
        this.Active.set(active);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OkeiFx)) return false;

        OkeiFx okeiDto = (OkeiFx) o;

        return getGuid() != null ? getGuid().equals(okeiDto.getGuid()) : okeiDto.getGuid() == null;
    }

    @Override
    public int hashCode() {
        return getGuid() != null ? getGuid().hashCode() : 0;
    }

    @Override
    public OkeiDto getEntity() {
        OkeiDto okeiDto=new OkeiDto();
        okeiDto.setName(getName());
        okeiDto.setActive(isActive());
        okeiDto.setGuid(getGuid());
        okeiDto.setLastEditingDate(getLastEditingDate());
        okeiDto.setTransportGuid(getTransportGuid());
        return okeiDto;
    }

    @Override
    public FxHelper<OkeiFx, OkeiDto> getHelper() {
        return OkeiHelper.getInstance();
    }
}
