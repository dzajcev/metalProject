package ru.metal.rest;

import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.users.request.ObtainDelegatingUsersRequest;
import ru.metal.api.users.response.ObtainDelegatingUsersResponse;
import ru.metal.security.ejb.dto.User;
import ru.metal.security.ejb.security.DelegatingUser;

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
import java.util.*;

/**
 * Created by User on 07.09.2017.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UserService {
    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/authorizationFacade!ru.metal.api.auth.AuthorizationFacade")
    private AuthorizationFacade authorizationFacade;


    @POST
    @Path("/getDelegateUsers")
    public Response changePassword(ObtainDelegatingUsersRequest usersRequest) throws Exception {
        Set<String> guids = new HashSet<>();
        guids.addAll(usersRequest.getUserGuids());
        Map<String, DelegatingUser> delegatingUsers = new HashMap<>();
        constructDelegateUsers(guids, delegatingUsers);
        ObtainDelegatingUsersResponse obtainDelegatingUsersResponse = new ObtainDelegatingUsersResponse();
        obtainDelegatingUsersResponse.setDataList(new ArrayList<>(delegatingUsers.values()));
        return Response.ok(obtainDelegatingUsersResponse).build();
    }

    private void constructDelegateUsers(Set<String> userGuids, Map<String, DelegatingUser> all) {
        ObtainUserRequest obtainUserRequest = new ObtainUserRequest();
        obtainUserRequest.getGuids().addAll(userGuids);
        ObtainUserResponse obtainUserResponse = authorizationFacade.obtainUsers(obtainUserRequest);

        Map<String, User> users = new HashMap<>();
        Set<String> delegatingUsers = new HashSet<>();
        for (User user : obtainUserResponse.getDataList()) {
            users.put(user.getGuid(), user);
            for (DelegatingUser delegatingUser : user.getDonorRights()) {
                if (!all.containsKey(delegatingUser.getUserGuid())) {
                    delegatingUsers.add(delegatingUser.getUserGuid());
                }
            }
        }

        for (String s : users.keySet()) {
            User user = users.get(s);
            DelegatingUser delegatingUser = new DelegatingUser();
            delegatingUser.setUserGuid(user.getGuid());
            delegatingUser.setUserName(user.getShortName());
            delegatingUser.setPrivileges(user.getPrivileges());
            delegatingUser.setRoles(user.getRoles());
            all.put(user.getGuid(), delegatingUser);
        }

        if (!delegatingUsers.isEmpty()) {
            constructDelegateUsers(delegatingUsers, all);
        }
    }
}
