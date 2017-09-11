package ru.metal.dto;

import javafx.beans.property.*;
import ru.common.api.dto.TableElement;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.GoodHelper;

/**
 * Created by User on 09.08.2017.
 */

public class GoodFx extends FxEntity<GoodDto> implements TableElement<GroupFx> {

    @ValidatableField(nullable = false, regexp = Formats.NAME_FORMAT)
    private StringProperty name = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private ObjectProperty<GroupFx> group = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<OkeiFx> okei = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false, regexp = Formats.NDS_FORMAT)
    private IntegerProperty nds = new SimpleIntegerProperty();

    private BooleanProperty active = new SimpleBooleanProperty(true);

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

    public GroupFx getGroup() {
        return group.get();
    }

    public ObjectProperty<GroupFx> groupProperty() {
        return group;
    }

    public void setGroup(GroupFx group) {
        this.group.set(group);
    }

    public OkeiFx getOkei() {
        return okei.get();
    }

    public ObjectProperty<OkeiFx> okeiProperty() {
        return okei;
    }

    public void setOkei(OkeiFx okei) {
        this.okei.set(okei);
    }

    public Integer getNds() {
        return nds.getValue();
    }

    public IntegerProperty ndsProperty() {
        return nds;
    }

    public void setNds(Integer nds) {
        this.nds.setValue(nds);
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

    @Override
    public GoodDto getEntity() {
        GoodDto dto = new GoodDto();
        dto.setNds(getNds());
        dto.setName(getName());
        dto.setActive(getActive());
        if (getOkei() != null) {
            dto.setOkei(getOkei().getEntity());
        }
        if (getGroup() != null) {
            dto.setGroup(getGroup().getEntity());
        }
        dto.setGuid(getGuid());
        dto.setLastEditingDate(getLastEditingDate());
        dto.setTransportGuid(getTransportGuid());

        return dto;
    }

    @Override
    public FxHelper<GoodFx, GoodDto> getHelper() {
        return GoodHelper.getInstance();
    }
}
