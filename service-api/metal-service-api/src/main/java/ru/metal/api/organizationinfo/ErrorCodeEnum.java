package ru.metal.api.organizationinfo;

import ru.common.api.dto.OperationErrorCode;

public enum ErrorCodeEnum implements OperationErrorCode {

    ORG001("Информация об организации не заполнена");
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
