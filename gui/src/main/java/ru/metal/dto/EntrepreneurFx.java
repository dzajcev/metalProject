package ru.metal.dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.DocumentDto;
import ru.metal.api.contragents.dto.EntrepreneurDto;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.dto.DocumentFx;
import ru.metal.dto.FxEntity;
import ru.metal.dto.annotations.ValidatableCollection;
import ru.metal.dto.helper.DocumentHelper;
import ru.metal.dto.helper.EntrepreneurHelper;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.GoodHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.08.2017.
 */
public class EntrepreneurFx extends FxEntity<EntrepreneurDto> {

    @ValidatableCollection(minSize = 2)
    private ObservableList<DocumentFx> documents=FXCollections.observableArrayList();

    public ObservableList<DocumentFx> getDocuments() {
        return documents;
    }

    public void setDocuments(ObservableList<DocumentFx> documents) {
        this.documents = documents;
    }

    @Override
    public EntrepreneurDto getEntity() {
        EntrepreneurDto entrepreneurDto=new EntrepreneurDto();

        entrepreneurDto.setGuid(getGuid());
        entrepreneurDto.setLastEditingDate(getLastEditingDate());
        entrepreneurDto.setTransportGuid(getTransportGuid());
        entrepreneurDto.setDocuments(DocumentHelper.getInstance().getDtoCollection(getDocuments()));
        return entrepreneurDto;
    }
    @Override
    public FxHelper<EntrepreneurFx, EntrepreneurDto> getHelper() {
        return EntrepreneurHelper.getInstance();
    }
}