package ru.metal.dto;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by User on 20.09.2017.
 */
public interface DocumentHeader {

    String getNumber();

    StringProperty numberProperty();

    ContragentFx getSource();

    ObjectProperty<ContragentFx> sourceProperty();

    ContragentFx getRecipient();

    ObjectProperty<ContragentFx> recipientProperty();

    Date getDateDocument();

    ObjectProperty<LocalDate> dateDocumentProperty();

    int countRows();

    IntegerProperty countRowsProperty();

    double getSum();

    DoubleProperty sumProperty();


}
