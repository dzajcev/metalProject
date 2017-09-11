package ru.metal.rest;

import ru.metal.api.order.request.ObtainOrderRequest;
import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.ObtainOrderResponse;
import ru.metal.api.order.response.UpdateOrderResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class OrderClient extends AbstractRestClient {
    private final String basePath="metal";
    private final String pathOrder = basePath+"/orders";


    public ObtainOrderResponse getOrders(ObtainOrderRequest obtainOrderRequest) throws ServerErrorException {
        ObtainOrderResponse response = execute(pathOrder + "/get", RequestType.POST, obtainOrderRequest, ObtainOrderResponse.class);
        return response;
    }

    public UpdateOrderResponse updateOrders(UpdateOrderRequest obtainOrderRequest) throws ServerErrorException {
        UpdateOrderResponse response = execute(pathOrder + "/update", RequestType.POST, obtainOrderRequest, UpdateOrderResponse.class);
        return response;
    }

}
