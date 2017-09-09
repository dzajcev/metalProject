package ru.metal.api.contragents.dto;

/**
 * Created by User on 31.08.2017.
 */
public enum PersonType {
    UL("Юридическое лицо"),
    IP("Индивидуальный предприниматель");

    private String title;

    PersonType(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }
}
