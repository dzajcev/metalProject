package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.EmployeeDto;
import ru.metal.dto.EmployeeFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class EmployeeHelper implements FxHelper<EmployeeFx,EmployeeDto> {
    private static volatile EmployeeHelper instance;

    public static EmployeeHelper getInstance() {
        EmployeeHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (EmployeeHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EmployeeHelper();
                }
            }
        }
        return localInstance;
    }

    private EmployeeHelper(){}


    @Override
    public EmployeeFx getFxEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        EmployeeFx employeeFx = new EmployeeFx();
        employeeFx.setActive(dto.getActive());
        employeeFx.setDocuments(DocumentHelper.getInstance().getFxCollection(dto.getDocuments()));
        employeeFx.setFirstName(dto.getFirstName());
        employeeFx.setMiddleName(dto.getMiddleName());
        employeeFx.setPosition(dto.getPosition());
        employeeFx.setSecondName(dto.getSecondName());
        employeeFx.setGuid(dto.getGuid());
        employeeFx.setLastEditingDate(dto.getLastEditingDate());
        employeeFx.setTransportGuid(dto.getTransportGuid());
        return employeeFx;
    }

    @Override
    public ObservableList<EmployeeFx> getFxCollection(List<EmployeeDto> collection) {
        ObservableList<EmployeeFx> result = FXCollections.observableArrayList();
        for (EmployeeDto dto : collection) {
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<EmployeeDto> getDtoCollection(Collection<EmployeeFx> collection) {
        List<EmployeeDto> result = new ArrayList<>();
        for (EmployeeFx dto : collection) {
            result.add(dto.getEntity());
        }
        return result;
    }
}
