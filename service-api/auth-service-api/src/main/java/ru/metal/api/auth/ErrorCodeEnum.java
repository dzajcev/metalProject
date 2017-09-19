package ru.metal.api.auth;

import ru.common.api.dto.OperationErrorCode;


public enum ErrorCodeEnum implements OperationErrorCode {

    REGISTRATION001("Запрос на регистрацию пуст"),
    REGISTRATION002("Не заполнены обязательные поля: %s"),
    REGISTRATION003("Пользователь с таким логином уже существует"),
    REGISTRATION004("Пользователь с таким Email уже существует"),
    REGISTRATION005("Запроса на регистрацию с guid=%s не существует"),
    REGISTRATION006("GUID %s запроса на регистрацию имеет не верный формат или пуст"),
    AUTH001("Не удалось изменить пароль"),
    AUTH002("Несанкционированная попытка смены пароля")
    ;
    private String pattern;

    ErrorCodeEnum(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}
