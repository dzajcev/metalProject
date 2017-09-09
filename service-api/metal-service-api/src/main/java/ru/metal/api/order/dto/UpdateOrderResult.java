package ru.metal.api.order.dto;

import ru.metal.api.common.dto.UpdateResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public class UpdateOrderResult extends UpdateResult {

    private String orderNumber;

    private List<UpdateBodyResult> bodyResults;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }



    public List<UpdateBodyResult> getBodyResults() {
        if (bodyResults==null){
            bodyResults=new ArrayList<>();
        }
        return bodyResults;
    }

    public void setBodyResults(List<UpdateBodyResult> bodyResults) {
        this.bodyResults = bodyResults;
    }
}