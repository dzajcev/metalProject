package ru.metal.api.report.response;

import ru.metal.api.report.dto.order.OrderData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07.09.2017.
 */
public class OrderReportResponse implements Serializable {
    private OrderData orderData;

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }
}