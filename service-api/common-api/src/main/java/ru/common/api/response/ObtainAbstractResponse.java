package ru.common.api.response;

import ru.common.api.dto.Error;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class ObtainAbstractResponse<DATA> extends AbstractResponse {

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

}
