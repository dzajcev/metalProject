package ru.metal.api.common.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class UpdateAbstractRequest<DATA> implements Serializable {

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
