package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.nomenclature.dto.OkeiDto;
import ru.metal.dto.OkeiFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class OkeiHelper implements FxHelper<OkeiFx,OkeiDto> {
    private static volatile OkeiHelper instance;

    public static OkeiHelper getInstance() {
        OkeiHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (OkeiHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OkeiHelper();
                }
            }
        }
        return localInstance;
    }

    private OkeiHelper(){}


    @Override
    public OkeiFx getFxEntity(OkeiDto dto) {
        if (dto == null) {
            return null;
        }
        OkeiFx okei = new OkeiFx();
        okei.setActive(dto.getActive());
        okei.setGuid(dto.getGuid());
        okei.setLastEditingDate(dto.getLastEditingDate());
        okei.setTransportGuid(dto.getTransportGuid());
        okei.setName(dto.getName());
        return okei;
    }

    @Override
    public ObservableList<OkeiFx> getFxCollection(List<OkeiDto> collection) {
        ObservableList<OkeiFx> result = FXCollections.observableArrayList();
        for (OkeiDto dto : collection) {
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<OkeiDto> getDtoCollection(Collection<OkeiFx> collection) {
        List<OkeiDto> result = new ArrayList<>();
        for (OkeiFx dto : collection) {
            result.add(dto.getEntity());
        }
        return result;
    }
}
