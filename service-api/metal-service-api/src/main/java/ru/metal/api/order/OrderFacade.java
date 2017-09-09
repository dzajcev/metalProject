package ru.metal.api.order;

import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;
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
