package ru.metal.security.ejb.security.checker;

public enum EntityOperation {

    READ("Чтение"),
    UPDATE("Изменение"),
    DELETE("Удаление"),
    CANCEL("Отмена");

    private String title;

    EntityOperation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
