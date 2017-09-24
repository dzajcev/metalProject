package ru.metal.rest;

import ru.metal.api.users.request.ObtainDelegatingUsersRequest;
import ru.metal.api.users.response.ObtainDelegatingUsersResponse;

/**
 * Created by User on 08.08.2017.
 */

public class UserClient extends AbstractRestClient {
    private final String basePath = "metal";
    private final String pathUsers = basePath + "/users";


    public ObtainDelegatingUsersResponse getDelegateUsers(ObtainDelegatingUsersRequest obtainDelegatingUsersRequest) {
        ObtainDelegatingUsersResponse response = executePost(pathUsers + "/getDelegateUsers", obtainDelegatingUsersRequest, ObtainDelegatingUsersResponse.class);
        return response;
    }
}
