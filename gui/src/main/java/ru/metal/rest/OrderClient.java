package ru.metal.rest;

import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.common.response.UpdateTreeItemResponse;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.api.order.request.ObtainOrderRequest;
import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.ObtainOrderResponse;
import ru.metal.api.order.response.UpdateOrderResponse;
import ru.metal.dto.GroupFx;
import ru.metal.dto.helper.GoodGroupHelper;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class OrderClient extends AbstractRestClient {

    private final String pathOrder = "orders";



    public ObtainOrderResponse getOrders(ObtainOrderRequest obtainOrderRequest) throws ServerErrorException {
        ObtainOrderResponse response = execute(pathOrder + "/get", RequestType.POST, obtainOrderRequest, ObtainOrderResponse.class);
        return response;
    }
    public UpdateOrderResponse updateOrders(UpdateOrderRequest obtainOrderRequest) throws ServerErrorException {
        UpdateOrderResponse response = execute(pathOrder + "/update", RequestType.POST, obtainOrderRequest, UpdateOrderResponse.class);
        return response;
    }

}
