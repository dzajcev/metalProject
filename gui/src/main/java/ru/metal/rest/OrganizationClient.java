package ru.metal.rest;

import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.api.organizationinfo.response.ObtainOrganizationInfoResponse;
import ru.metal.api.organizationinfo.response.UpdateOrganizationResponse;

/**
 * Created by User on 08.08.2017.
 */

public class OrganizationClient extends AbstractRestClient {
    private final String basePath = "metal";
    private final String path = basePath + "/orginfo";

    public ObtainOrganizationInfoResponse getOrganizationInfo() {
        ObtainOrganizationInfoResponse execute = executeGet(path, ObtainOrganizationInfoResponse.class);
        return execute;
    }

    public UpdateOrganizationResponse updateOrganizationInfo(UpdateOrganizationRequest updateOrganizationRequest) {
        UpdateOrganizationResponse execute = executePost(path, updateOrganizationRequest, UpdateOrganizationResponse.class);
        return execute;
    }
}
