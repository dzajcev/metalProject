package ru.metal.api.auth.response;

import ru.common.api.dto.Error;
import ru.common.api.response.AbstractResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10.09.2017.
 */
public class RegistrationResponse extends AbstractResponse {

    private List<Error> errors;

    public List<Error> getErrors() {
        if (errors==null){
            errors=new ArrayList<>();
        }

        return errors;
    }

    public void setError(List<Error> errors) {
        this.errors = errors;
    }
}
