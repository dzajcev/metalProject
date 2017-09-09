package ru.metal.api.contragents.dto;

/**
 * Created by User on 31.08.2017.
 */
public enum ContragentType {
    SOURCE("Продавец"),
    BUYER("Покупатель"),
    SHIPPER("Грузоотправитель/Грузополучатель"),
    DRIVER("Транспортная компания");

    private String title;

    ContragentType(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }
}
