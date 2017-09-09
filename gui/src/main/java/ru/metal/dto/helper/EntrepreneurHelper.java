package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.EntrepreneurDto;
import ru.metal.dto.EntrepreneurFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class EntrepreneurHelper implements FxHelper<EntrepreneurFx, EntrepreneurDto> {
    private static volatile EntrepreneurHelper instance;

    public static EntrepreneurHelper getInstance() {
        EntrepreneurHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (EntrepreneurHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EntrepreneurHelper();
                }
            }
        }
        return localInstance;
    }

    private EntrepreneurHelper() {
    }

    @Override
    public EntrepreneurFx getFxEntity(EntrepreneurDto dto) {
        if (dto == null) {
            return null;
        }
        EntrepreneurFx entrepreneurFx = new EntrepreneurFx();

        entrepreneurFx.setGuid(dto.getGuid());
        entrepreneurFx.setLastEditingDate(dto.getLastEditingDate());
        entrepreneurFx.setTransportGuid(dto.getTransportGuid());
        entrepreneurFx.setDocuments(DocumentHelper.getInstance().getFxCollection(dto.getDocuments()));
        return entrepreneurFx;
    }

    @Override
    public ObservableList<EntrepreneurFx> getFxCollection(List<EntrepreneurDto> collection) {
        ObservableList<EntrepreneurFx> result = FXCollections.observableArrayList();
        for (EntrepreneurDto dto : collection) {
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<EntrepreneurDto> getDtoCollection(Collection<EntrepreneurFx> collection) {
        List<EntrepreneurDto> result = new ArrayList<>();
        for (EntrepreneurFx dto : collection) {
            result.add(dto.getEntity());
        }
        return result;
    }
}
