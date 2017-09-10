package ru.metal.api.contragents;

import ru.metal.api.common.request.DeleteTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.common.response.DeleteTreeItemResponse;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentGroupRequest;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface ContragentsFacade {

    ObtainContragentGroupReponse getGroups(ObtainContragentGroupRequest obtainTreeItemRequest);

    UpdateContragentGroupResponse updateGroups(UpdateTreeItemRequest<ContragentGroupDto> request);

    UpdateContragentGroupResponse deleteGroups(DeleteTreeItemRequest<ContragentGroupDto> request);

    ObtainContragentResponse getContragents(ObtainContragentRequest obtainContragentRequest);

    UpdateContragentResponse updateContragents(UpdateContragentRequest request);

    UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest request);

}
