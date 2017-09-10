package ru.metal.api.common.response;

import ru.metal.api.common.dto.Error;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 09.09.2017.
 */
public class DeleteTreeItemResponse extends AbstractResponse {

    private List<Error> errors;

    public List<Error> getErrors() {
        if (errors==null){
            errors=new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
