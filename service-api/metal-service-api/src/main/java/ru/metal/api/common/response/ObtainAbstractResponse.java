package ru.metal.api.common.response;

import ru.metal.api.common.dto.Error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class ObtainAbstractResponse<DATA> extends AbstractResponse {

    private List<Error> errors;

    private List<DATA> responseData;

    public List<DATA> getDataList() {
        if (responseData == null) {
            this.responseData = new ArrayList<>();
        }
        return responseData;
    }

    public void setDataList(List<DATA> dataList) {
        this.responseData = dataList;
    }

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
