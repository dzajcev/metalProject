package ru.metal.dto;

import javafx.beans.property.*;
import ru.metal.api.contragents.dto.DocumentDto;
import ru.metal.api.contragents.dto.DocumentType;
import ru.metal.api.contragents.dto.EmployeeDto;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.DocumentHelper;
import ru.metal.dto.helper.EmployeeHelper;
import ru.metal.dto.helper.FxHelper;

import java.util.Date;

/**
 * Created by User on 01.09.2017.
 */
public class DocumentFx extends FxEntity<DocumentDto> {

    @ValidatableField(nullable = false)
    private ObjectProperty<DocumentType> documentType = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false, regexp = "(.){0,10}")
    private StringProperty serie = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = "(.){0,10}")
    private StringProperty number = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = "(.){0,50}")
    private StringProperty vidan = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private ObjectProperty<Date> date = new SimpleObjectProperty<>();

    private BooleanProperty active = new SimpleBooleanProperty(true);

    public DocumentType getDocumentType() {
        return documentType.get();
    }

    public ObjectProperty<DocumentType> documentTypeProperty() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType.set(documentType);
    }

    public String getSerie() {
        return serie.get();
    }

    public StringProperty serieProperty() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie.set(serie);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getVidan() {
        return vidan.get();
    }

    public StringProperty vidanProperty() {
        return vidan;
    }

    public void setVidan(String vidan) {
        this.vidan.set(vidan);
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public DocumentDto getEntity() {
        DocumentDto dto = new DocumentDto();
        dto.setActive(isActive());
        dto.setDate(getDate());
        dto.setDocumentType(getDocumentType());
        dto.setNumber(getNumber());
        dto.setSerie(getSerie());
        dto.setVidan(getVidan());
        dto.setGuid(getGuid());
        dto.setLastEditingDate(getLastEditingDate());
        dto.setTransportGuid(getTransportGuid());
        return dto;
    }

    @Override
    public FxHelper<DocumentFx, DocumentDto> getHelper() {
        return DocumentHelper.getInstance();
    }
}
