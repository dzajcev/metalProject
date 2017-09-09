package ru.metal.rest;

import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.organizationinfo.OrganizationInfoFacade;
import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.api.organizationinfo.response.ObtainOrganizationInfoResponse;
import ru.metal.api.organizationinfo.response.UpdateOrganizationResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Path("/orginfo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class OrganizationInfoService extends AbstractService implements Serializable {

    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/organizationInfoFacade!ru.metal.api.organizationinfo.OrganizationInfoFacade")
    private OrganizationInfoFacade organizationInfoFacade;

    @GET
    @Path("/")
    public Response findOrganization() throws Exception {
        ObtainOrganizationInfoResponse organizationInfo = organizationInfoFacade.getOrganizationInfo();
        return Response.ok(organizationInfo).build();
    }

    @POST
    @Path("/")
    public Response update(UpdateOrganizationRequest updateOrganizationRequest) throws Exception {
        UpdateOrganizationResponse updateOrganizationResponse = organizationInfoFacade.updateOrganizationInfo(updateOrganizationRequest);
        return Response.ok(updateOrganizationResponse).build();
    }


}
