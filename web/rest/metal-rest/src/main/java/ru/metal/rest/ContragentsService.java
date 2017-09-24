package ru.metal.rest;

import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.metal.api.contragents.ContragentsFacade;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentGroupRequest;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;

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

@Path("/contragents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ContragentsService {

    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/contragentsFacade!ru.metal.api.contragents.ContragentsFacade")
    private ContragentsFacade contragentsFacade;

    @POST
    @Path("/groups/get")
    public Response getGroups(ObtainContragentGroupRequest obtainTreeItemRequest) throws Exception {
        ObtainContragentGroupReponse groups = contragentsFacade.getGroups(obtainTreeItemRequest);
        return Response.ok(groups).build();
    }
    @POST
    @Path("/groups/getFull")
    public Response getGroupsFull(ObtainContragentGroupRequest obtainTreeItemRequest) throws Exception {
        ObtainContragentGroupReponse groups = contragentsFacade.getFullGroupsByContragents(obtainTreeItemRequest);
        return Response.ok(groups).build();
    }
    @POST
    @Path("/groups/update")
    public Response updateGroups(UpdateTreeItemRequest<ContragentGroupDto> request) throws Exception {
        UpdateContragentGroupResponse updateContragentGroupResponse = contragentsFacade.updateGroups(request);
        return Response.ok(updateContragentGroupResponse).build();
    }

    @POST
    @Path("/groups/delete")
    public Response deleteGroups(DeleteTreeItemRequest<ContragentGroupDto> request) throws Exception {
        UpdateContragentGroupResponse groupUpdateResponse = contragentsFacade.deleteGroups(request);
        return Response.ok(groupUpdateResponse).build();
    }

    @POST
    @Path("/contragent/get")
    public Response getContragents(ObtainContragentRequest obtainContragentRequest) throws Exception {
        ObtainContragentResponse contragents = contragentsFacade.getContragents(obtainContragentRequest);
        return Response.ok(contragents).build();
    }

    @POST
    @Path("/contragent/update")
    public Response updateContragents(UpdateContragentRequest request) throws Exception {
        UpdateContragentResponse updateContragentResponse = contragentsFacade.updateContragents(request);
        return Response.ok(updateContragentResponse).build();
    }

    @POST
    @Path("/employee/update")
    public Response updateEmployee(UpdateEmployeeRequest request) throws Exception {
        UpdateEmployeeResponse updateContragentResponse = contragentsFacade.updateEmployee(request);
        return Response.ok(updateContragentResponse).build();
    }
}
