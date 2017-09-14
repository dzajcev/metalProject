package ru.auth;

import ru.metal.api.auth.RegistrationFacade;
import ru.metal.api.auth.request.RegistrationRequest;
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
@Path("/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RegistrationService {
    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/registrationFacade!ru.metal.api.auth.RegistrationFacade")
    private RegistrationFacade registrationFacade;

    @POST
    @Path("/create")
    public Response createRegistration(RegistrationRequest registrationRequest) throws Exception {
        RegistrationResponse response = registrationFacade.createRegistration(registrationRequest);
        return Response.ok(response).build();
    }

}
