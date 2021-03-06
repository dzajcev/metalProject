package ru.common.api.response;

import ru.common.api.dto.Error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 09.09.2017.
 */
public abstract class AbstractResponse implements Serializable {
    private List<Error> errors;

    public List<Error> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
