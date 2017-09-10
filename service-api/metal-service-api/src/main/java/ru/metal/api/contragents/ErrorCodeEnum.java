package ru.metal.api.contragents;

import ru.metal.api.OperationErrorCode;


public enum ErrorCodeEnum implements OperationErrorCode {

    CONTRAGENT001("Группа %s содержит в себе элементы. Удаление не возможно.");
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
