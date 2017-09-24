package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.dto.ContragentFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class ContragentHelper implements FxHelper<ContragentFx,ContragentDto>{
    private static volatile ContragentHelper instance;

    public static ContragentHelper getInstance() {
        ContragentHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (ContragentHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ContragentHelper();
                }
            }
        }
        return localInstance;
    }

    private ContragentHelper(){}
    @Override
    public ContragentFx getFxEntity(ContragentDto dto){
        if (dto==null){
            return null;
        }
        ContragentFx contragentFx=new ContragentFx();
        contragentFx.setActive(dto.getActive());
        contragentFx.setGuid(dto.getGuid());
        contragentFx.setLastEditingDate(dto.getLastEditingDate());
        contragentFx.setTransportGuid(dto.getTransportGuid());

        contragentFx.setAccount(dto.getAccount());
        contragentFx.setAccountant(EmployeeHelper.getInstance().getFxEntity(dto.getAccountant()));
        contragentFx.setDirector(EmployeeHelper.getInstance().getFxEntity(dto.getDirector()));
        contragentFx.setEmployees(EmployeeHelper.getInstance().getFxCollection(dto.getEmployees()));

        contragentFx.setAddress(dto.getAddress());
        contragentFx.setAlias(dto.getAlias());
        contragentFx.setBank(dto.getBank());
        contragentFx.setBik(dto.getBik());
        contragentFx.setComment(dto.getComment());
        contragentFx.setPersonType(dto.getPersonType());
        contragentFx.setContragentTypes(FXCollections.observableArrayList(dto.getContragentTypes()));
        contragentFx.setEmail(dto.getEmail());
        contragentFx.setEntrepreneur(EntrepreneurHelper.getInstance().getFxEntity(dto.getEntrepreneur()));
        contragentFx.setGroup(ContragentGroupHelper.getInstance().getFxEntity(dto.getGroup()));
        contragentFx.setInn(dto.getInn());
        contragentFx.setKorrAccount(dto.getKorrAccount());
        contragentFx.setKpp(dto.getKpp());
        contragentFx.setName(dto.getName());
        contragentFx.setOgrn(dto.getOgrn());
        contragentFx.setOkpo(dto.getOkpo());
        contragentFx.setOkved(dto.getOkved());
        contragentFx.setPhone(dto.getPhone());
        contragentFx.setShipper(getFxEntity(dto.getShipper()));
        contragentFx.setUserGuid(dto.getUserGuid());

        return contragentFx;
    }

    @Override
    public ObservableList<ContragentFx> getFxCollection(List<ContragentDto> collection){
        ObservableList<ContragentFx> result=FXCollections.observableArrayList();
        for (ContragentDto dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<ContragentDto> getDtoCollection(Collection<ContragentFx> collection){
        List<ContragentDto> result=new ArrayList<>();
        for (ContragentFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }
}
