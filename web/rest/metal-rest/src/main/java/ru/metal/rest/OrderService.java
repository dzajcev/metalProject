package ru.metal.rest;

import ru.metal.api.documents.order.OrderFacade;
import ru.metal.api.documents.order.request.DropOrderRequest;
import ru.metal.api.documents.order.request.ObtainOrderRequest;
import ru.metal.api.documents.order.request.UpdateOrderRequest;
import ru.metal.api.documents.order.response.ObtainOrderResponse;
import ru.metal.api.documents.order.response.UpdateOrderResponse;

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

    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/orderFacade!ru.metal.api.documents.order.OrderFacade")
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
    @POST
    @Path("/drop")
    public Response dropOrders(DropOrderRequest dropOrderRequest) throws Exception {
        UpdateOrderResponse contragents = orderFacade.dropOrders(dropOrderRequest);
        return Response.ok(contragents).build();
    }
}
