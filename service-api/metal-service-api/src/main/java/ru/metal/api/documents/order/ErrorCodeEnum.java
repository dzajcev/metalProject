package ru.metal.api.documents.order;

import ru.common.api.dto.OperationErrorCode;


public enum ErrorCodeEnum implements OperationErrorCode {

    ORDER001("Удаление невозможно. Документ не в статусе \"Черновик\""),
    ORDER002("Удаление невозможно. Документ с Guid=%s не найден ");
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
