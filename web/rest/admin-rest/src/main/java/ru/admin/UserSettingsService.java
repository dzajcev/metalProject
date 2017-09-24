package ru.admin;

import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.RegistrationFacade;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.UpdateUserResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by User on 18.09.2017.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UserSettingsService {
    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/registrationFacade!ru.metal.api.auth.RegistrationFacade")
    private RegistrationFacade registrationFacade;
    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/authorizationFacade!ru.metal.api.auth.AuthorizationFacade")
    private AuthorizationFacade authorizationFacade;
    @POST
    @Path("/update")
    public Response updateUser(UpdateUserRequest registrationRequest) throws Exception {
        UpdateUserResponse response = authorizationFacade.updateUser(registrationRequest);
        return Response.ok(response).build();
    }
    @POST
    @Path("/get")
    public Response getUsers(ObtainUserRequest obtainUserRequest) throws Exception {
        ObtainUserResponse response = authorizationFacade.obtainUsers(obtainUserRequest);
        return Response.ok(response).build();
    }
    @POST
    @Path("/acceptRegistrationRequest")
    public Response acceptRegistration(AcceptRegistrationRequest registrationRequest) throws Exception {
        AcceptRegistrationResponse response = registrationFacade.acceptRegistration(registrationRequest,false);
        return Response.ok(response).build();
    }
    @Path("/getRegistrationRequests")
    @GET
    public Response getRegistrationRequests() throws Exception {
        ObtainRegistrationRequestsResponse response = registrationFacade.getRegistrationRequests();
        return Response.ok(response).build();
    }
}
