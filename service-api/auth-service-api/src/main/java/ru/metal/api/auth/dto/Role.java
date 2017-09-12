package ru.metal.api.auth.dto;

/**
 * Created by User on 11.09.2017.
 */
public enum Role {
    ADMIN("Администратор"),
    DIRECTOR("Директор"),
    ACCOUNTANT("Бухгалтер"),
    MANAGER("Сотрудник");

    private String title;

    Role(String title){
        this.title=title;
    }
}
