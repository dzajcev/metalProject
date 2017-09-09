package ru.metal.api.organizationinfo;

import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.api.organizationinfo.response.ObtainOrganizationInfoResponse;
import ru.metal.api.organizationinfo.response.UpdateOrganizationResponse;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface OrganizationInfoFacade {

    ObtainOrganizationInfoResponse getOrganizationInfo();

    UpdateOrganizationResponse updateOrganizationInfo(UpdateOrganizationRequest updateOrganizationRequest);

}
