package ru.metal.rest;

import ru.metal.api.order.OrderFacade;
import ru.metal.api.order.request.ObtainOrderRequest;
import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.ObtainOrderResponse;
import ru.metal.api.order.response.UpdateOrderResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class OrderService {

    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/orderFacade!ru.metal.api.order.OrderFacade")
    private OrderFacade orderFacade;

    @POST
    @Path("/get")
    public Response getOrders(ObtainOrderRequest obtainOrderRequest) throws Exception {
        ObtainOrderResponse contragents = orderFacade.getOrders(obtainOrderRequest);
        return Response.ok(contragents).build();
    }

    @POST
    @Path("/update")
    public Response updateOrders(UpdateOrderRequest updateOrderRequest) throws Exception {
        UpdateOrderResponse contragents = orderFacade.updateOrders(updateOrderRequest);
        return Response.ok(contragents).build();
    }
}
