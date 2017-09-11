package ru.metal.api.order;

import ru.metal.api.order.request.ObtainOrderRequest;
import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.ObtainOrderResponse;
import ru.metal.api.order.response.UpdateOrderResponse;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface OrderFacade {

    ObtainOrderResponse getOrders(ObtainOrderRequest obtainOrderRequest);

    UpdateOrderResponse updateOrders(UpdateOrderRequest updateOrderRequest);


}
