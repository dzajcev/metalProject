package ru.metal.api.auth.exceptions;

import java.util.Collection;

/**
 * Created by User on 11.09.2017.
 */
public class FieldValidationException extends Exception {

    /**
     * Некорректно заполненные поля
     */
    private Collection<String> fieldNames;

    private static String fieldsToString(Collection<String> fieldNames) {
        if (fieldNames == null || fieldNames.isEmpty()) {
            return "Invalid fields list is empty.";
        } else {
            StringBuilder sb = new StringBuilder();

            sb.append("Invalid fields: ");
            for (String fieldName : fieldNames) {
                sb.append(fieldName).append(',');
            }

            return sb.substring(0, sb.length() - 1);
        }
    }

    public FieldValidationException(Collection<String> fieldNames) {
        super(fieldsToString(fieldNames));
        this.fieldNames = fieldNames;

    }

    public Collection<String> getFieldNames() {
        return fieldNames;
    }
}