package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.DocumentDto;
import ru.metal.api.contragents.dto.EmployeeDto;
import ru.metal.dto.DocumentFx;
import ru.metal.dto.EmployeeFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class DocumentHelper implements FxHelper<DocumentFx,DocumentDto> {
    private static volatile DocumentHelper instance;

    public static DocumentHelper getInstance() {
        DocumentHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (DocumentHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DocumentHelper();
                }
            }
        }
        return localInstance;
    }

    private DocumentHelper(){}

    public DocumentFx getFxEntity(DocumentDto dto){
        if (dto==null){
            return null;
        }
        DocumentFx documentFx=new DocumentFx();
        documentFx.setActive(dto.getActive());
        documentFx.setGuid(dto.getGuid());
        documentFx.setLastEditingDate(dto.getLastEditingDate());
        documentFx.setTransportGuid(dto.getTransportGuid());

        documentFx.setDate(dto.getDate());
        documentFx.setDocumentType(dto.getDocumentType());
        documentFx.setNumber(dto.getNumber());
        documentFx.setSerie(dto.getSerie());
        documentFx.setVidan(dto.getVidan());
        return documentFx;
    }

    @Override
    public ObservableList<DocumentFx> getFxCollection(List<DocumentDto> collection) {
        ObservableList<DocumentFx> result=FXCollections.observableArrayList();
        for (DocumentDto dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<DocumentDto> getDtoCollection(Collection<DocumentFx> collection) {
        List<DocumentDto> result=new ArrayList<>();
        for (DocumentFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }
}
