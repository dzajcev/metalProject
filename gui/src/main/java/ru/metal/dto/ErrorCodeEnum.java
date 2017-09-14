package ru.metal.dto;

import ru.common.api.dto.OperationErrorCode;


public enum ErrorCodeEnum implements OperationErrorCode {
    ERR000("Непредвиденная ошибка, Стектрейс ошибки: \n %s"),
    ERR001("Ошибка авторизации"),
    ERR002("Ошибка шифрования (ошибка ключей доступа)"),
    ERR003("Ошибка доступа. Сессия истекла. Требуется повторный ввод логина и пароля"),

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
