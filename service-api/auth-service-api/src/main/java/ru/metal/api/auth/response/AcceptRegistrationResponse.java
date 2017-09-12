package ru.metal.api.auth.response;

import ru.common.api.dto.Error;
import ru.common.api.response.AbstractResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11.09.2017.
 */
public class AcceptRegistrationResponse extends AbstractResponse {
    private List<Error> errors=new ArrayList<>();

    public List<Error> getErrors() {
        if (errors==null){
            return new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
