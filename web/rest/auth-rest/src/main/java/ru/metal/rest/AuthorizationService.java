package ru.metal.rest;

import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.RegistrationFacade;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.RegistrationResponse;

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

/**
 * Created by User on 07.09.2017.
 */
@Path("/authorization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class AuthorizationService {
    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/authorizationFacade!ru.metal.api.auth.AuthorizationFacade")
    private AuthorizationFacade authorizationFacade;

    @POST
    @Path("/authorize")
    public Response authorize(AuthorizationRequest authorizationRequest) throws Exception {
        AuthorizationResponse response = authorizationFacade.authorization(authorizationRequest);
        return Response.ok(response).build();
    }

}
