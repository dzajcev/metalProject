package ru.metal.rest;

import ru.metal.api.documents.order.request.DropOrderRequest;
import ru.metal.api.documents.order.request.ObtainOrderRequest;
import ru.metal.api.documents.order.request.UpdateOrderRequest;
import ru.metal.api.documents.order.response.ObtainOrderResponse;
import ru.metal.api.documents.order.response.UpdateOrderResponse;

/**
 * Created by User on 08.08.2017.
 */

public class OrderClient extends AbstractRestClient {
    private final String basePath = "metal";
    private final String pathOrder = basePath + "/orders";


    public ObtainOrderResponse getOrders(ObtainOrderRequest obtainOrderRequest) {
        ObtainOrderResponse response = executePost(pathOrder + "/get", obtainOrderRequest, ObtainOrderResponse.class);
        return response;
    }

    public UpdateOrderResponse updateOrders(UpdateOrderRequest obtainOrderRequest) {
        UpdateOrderResponse response = executePost(pathOrder + "/update", obtainOrderRequest, UpdateOrderResponse.class);
        return response;
    }
    public UpdateOrderResponse dropOrders(DropOrderRequest dropOrderRequest) {
        UpdateOrderResponse response = executePost(pathOrder + "/drop", dropOrderRequest, UpdateOrderResponse.class);
        return response;
    }
}
