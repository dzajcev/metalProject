package ru.metal.rest;

import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.api.organizationinfo.response.ObtainOrganizationInfoResponse;
import ru.metal.api.organizationinfo.response.UpdateOrganizationResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class OrganizationClient extends AbstractRestClient {
    private final String basePath="metal";
    private final String path = basePath+"/orginfo";

    public ObtainOrganizationInfoResponse getOrganizationInfo() throws ServerErrorException {
        ObtainOrganizationInfoResponse execute = execute(path, RequestType.GET, null, ObtainOrganizationInfoResponse.class);
        return execute;
    }

    public UpdateOrganizationResponse updateOrganizationInfo(UpdateOrganizationRequest updateOrganizationRequest) throws ServerErrorException {
        UpdateOrganizationResponse execute = execute(path, RequestType.POST, updateOrganizationRequest, UpdateOrganizationResponse.class);
        return execute;
    }
}
