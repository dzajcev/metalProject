package ru.metal.api.contragents.dto;

import ru.metal.api.common.dto.ComboBoxElement;

/**
 * Created by User on 31.08.2017.
 */
public enum  DocumentType implements ComboBoxElement  {
    PASSPORT("Паспорт"),
    CERTIFICATE("Свидетельство"),
    DRIVER_CARD("Водительское удостоверение");

    private String title;

    DocumentType(String title){
        this.title=title;
    }

    public String getName(){
        return title;
    }



}
