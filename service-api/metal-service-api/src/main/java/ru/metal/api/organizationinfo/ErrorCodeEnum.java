package ru.metal.api.organizationinfo;

import ru.metal.api.OperationErrorCode;

/**
 * Date: 12/4/14
 *
 * @author Alexander V. Zinin (zinin@lanit.ru)
 */
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
