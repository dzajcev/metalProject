package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.dto.ContragentGroupFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class ContragentGroupHelper implements FxHelper<ContragentGroupFx,ContragentGroupDto> {
    private static volatile ContragentGroupHelper instance;

    public static ContragentGroupHelper getInstance() {
        ContragentGroupHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (ContragentGroupHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ContragentGroupHelper();
                }
            }
        }
        return localInstance;
    }
    private ContragentGroupHelper(){}

    @Override
    public ContragentGroupFx getFxEntity(ContragentGroupDto dto){
        if (dto==null){
            return null;
        }
        ContragentGroupFx contragentGroupFx=new ContragentGroupFx();
        contragentGroupFx.setActive(dto.getActive());
        contragentGroupFx.setGuid(dto.getGuid());
        contragentGroupFx.setLastEditingDate(dto.getLastEditingDate());
        contragentGroupFx.setTransportGuid(dto.getTransportGuid());

        contragentGroupFx.setActive(dto.getActive());
        contragentGroupFx.setGroupGuid(dto.getGroupGuid());
        contragentGroupFx.setName(dto.getName());
        contragentGroupFx.setUserGuid(dto.getUserGuid());
        return contragentGroupFx;
    }
    @Override
    public ObservableList<ContragentGroupFx> getFxCollection(List<ContragentGroupDto> collection){
        ObservableList<ContragentGroupFx> result= FXCollections.observableArrayList();
        for (ContragentGroupDto dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }
    @Override
    public List<ContragentGroupDto> getDtoCollection(Collection<ContragentGroupFx> collection){
        List<ContragentGroupDto> result=new ArrayList<>();
        for (ContragentGroupFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }

}
