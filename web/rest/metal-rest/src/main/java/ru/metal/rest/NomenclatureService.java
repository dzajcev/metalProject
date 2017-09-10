package ru.metal.rest;

import ru.metal.api.common.request.DeleteTreeItemRequest;
import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.api.nomenclature.NomenclatureFacade;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/nomenclature")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class NomenclatureService extends AbstractService {

    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/nomenclatureFacade!ru.metal.api.nomenclature.NomenclatureFacade")
    private NomenclatureFacade nomenclatureFacade;

    @POST
    @Path("/groups/get")
    public Response getGroups(ObtainTreeItemRequest obtainTreeItemRequest) throws Exception {
        ObtainGroupReponse groupResponse = nomenclatureFacade.getGroups(obtainTreeItemRequest);
        return Response.ok(groupResponse).build();
    }

    @POST
    @Path("/groups/update")
    public Response updateGroups(UpdateTreeItemRequest<GroupDto> request) throws Exception {
        UpdateGoodGroupResponse groupUpdateResponse = nomenclatureFacade.updateGroups(request);
        return Response.ok(groupUpdateResponse).build();
    }

    @POST
    @Path("/groups/delete")
    public Response deleteGroups(DeleteTreeItemRequest<GroupDto> request) throws Exception {
        UpdateGoodGroupResponse groupUpdateResponse = nomenclatureFacade.deleteGroups(request);
        return Response.ok(groupUpdateResponse).build();
    }

    @POST
    @Path("/goods/get")
    public Response getGoods(ObtainGoodRequest obtainGoodRequest) throws Exception {
        ObtainGoodResponse obtainGoodResponse = nomenclatureFacade.getGoods(obtainGoodRequest);
        return Response.ok(obtainGoodResponse).build();
    }

    @POST
    @Path("/goods/update")
    public Response updateGoods(UpdateGoodsRequest request) throws Exception {

        UpdateGoodsResponse updateGoodsResponse = nomenclatureFacade.updateGoods(request);
        return Response.ok(updateGoodsResponse).build();
    }

    @POST
    @Path("/okei/get")
    public Response getOkei(ObtainOkeiRequest obtainOkeiRequest) throws Exception {
        ObtainOkeiResponse okei = nomenclatureFacade.getOkei(obtainOkeiRequest);
        return Response.ok(okei).build();
    }


}
