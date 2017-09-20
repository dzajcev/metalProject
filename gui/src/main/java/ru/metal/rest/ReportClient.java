package ru.metal.rest;

import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;

/**
 * Created by User on 08.08.2017.
 */

public class ReportClient extends AbstractRestClient {
    private final String pathOrder = "/report";


    public OrderReportResponse getOrders(OrderReportRequest orderReportRequest) {
        OrderReportResponse response = executePost(pathOrder + "/order", orderReportRequest, OrderReportResponse.class);
        return response;
    }

}
