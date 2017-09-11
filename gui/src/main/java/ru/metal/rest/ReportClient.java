package ru.metal.rest;

import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.UpdateOrderResponse;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class ReportClient extends AbstractRestClient {
    private final String pathOrder = "/report";



    public OrderReportResponse getOrders(OrderReportRequest orderReportRequest) throws ServerErrorException {
        OrderReportResponse response = execute(pathOrder + "/order", RequestType.POST, orderReportRequest, OrderReportResponse.class);
        return response;
    }

}
