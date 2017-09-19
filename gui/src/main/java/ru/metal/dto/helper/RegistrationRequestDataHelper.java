package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.dto.GoodFx;
import ru.metal.dto.RegistrationRequestDataFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class RegistrationRequestDataHelper implements FxHelper<RegistrationRequestDataFx,RegistrationData> {
    private static volatile RegistrationRequestDataHelper instance;

    public static RegistrationRequestDataHelper getInstance() {
        RegistrationRequestDataHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (RegistrationRequestDataHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RegistrationRequestDataHelper();
                }
            }
        }
        return localInstance;
    }
    private RegistrationRequestDataHelper(){}

    @Override
    public RegistrationRequestDataFx getFxEntity(RegistrationData dto){
        if (dto==null){
            return null;
        }
        RegistrationRequestDataFx dataFx=new RegistrationRequestDataFx();
        dataFx.setAccepted(dto.isAccepted());
        dataFx.setGuid(dto.getGuid());
        dataFx.setLastEditingDate(dto.getLastEditingDate());
        dataFx.setTransportGuid(dto.getTransportGuid());

        dataFx.setEmail(dto.getEmail());
        dataFx.setFirstName(dto.getFirstName());
        dataFx.setMiddleName(dto.getMiddleName());
        dataFx.setLogin(dto.getLogin());
        dataFx.setSecondName(dto.getSecondName());
        dataFx.setToken(dto.getToken());
        return dataFx;
    }
    @Override
    public ObservableList<RegistrationRequestDataFx> getFxCollection(List<RegistrationData> collection){
        ObservableList<RegistrationRequestDataFx> result= FXCollections.observableArrayList();
        for (RegistrationData dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }
    @Override
    public List<RegistrationData> getDtoCollection(Collection<RegistrationRequestDataFx> collection){
        List<RegistrationData> result=new ArrayList<>();
        for (RegistrationRequestDataFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }

}
