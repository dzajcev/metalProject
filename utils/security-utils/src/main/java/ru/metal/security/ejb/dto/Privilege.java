package ru.metal.security.ejb.dto;

/**
 * Created by User on 11.09.2017.
 */
public enum Privilege {
    READ_ORDERS("Просмотр всех счетов на оплату"),
    WRITE_ORDERS("Редактирование всех счетов на оплату");

    private String title;

    Privilege(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }

}
