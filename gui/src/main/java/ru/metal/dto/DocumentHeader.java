package ru.metal.dto;

import javafx.beans.property.*;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.security.ejb.security.DelegatingUser;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by User on 20.09.2017.
 */
public interface DocumentHeader<S extends DocumentStatus> {

    String getGuid();

    String getNumber();

    StringProperty numberProperty();

    ContragentFx getSource();

    ObjectProperty<ContragentFx> sourceProperty();

    ContragentFx getRecipient();

    ObjectProperty<ContragentFx> recipientProperty();

    Date getDateDocument();

    ObjectProperty<LocalDate> dateDocumentProperty();

    Date getDateCreate();

    ObjectProperty<LocalDate> dateCreateProperty();

    int countRows();

    IntegerProperty countRowsProperty();

    double getSum();

    DoubleProperty sumProperty();

    S getDocumentStatus();

    ObjectProperty<S> documentStatusProperty();

    String getUserGuid();

    StringProperty userGuidProperty();


}
