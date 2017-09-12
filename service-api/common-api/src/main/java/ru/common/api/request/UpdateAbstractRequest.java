package ru.common.api.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class UpdateAbstractRequest<DATA> extends AbstractRequest {

    private List<DATA> dataList;

    public List<DATA> getDataList() {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        }
        return dataList;
    }

    public void setDataList(List<DATA> dataList) {
        this.dataList = dataList;
    }
}
