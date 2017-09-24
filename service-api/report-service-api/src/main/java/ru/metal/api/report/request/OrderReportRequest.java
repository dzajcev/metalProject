package ru.metal.api.report.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07.09.2017.
 */
public class OrderReportRequest implements Serializable {
    private List<String> guids;

    public List<String> getOrderList() {
        if (guids ==null){
            guids =new ArrayList<>();
        }
        return guids;
    }

    public void setOrderList(List<String> guids) {
        this.guids = guids;
    }
}
