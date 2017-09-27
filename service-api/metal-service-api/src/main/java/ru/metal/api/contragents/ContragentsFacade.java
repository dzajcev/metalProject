package ru.metal.api.contragents;

import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
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

    ObtainContragentGroupResponse getGroups(ObtainContragentGroupRequest obtainTreeItemRequest);

    ObtainContragentGroupResponse getFullGroupsByContragents(ObtainContragentGroupRequest obtainContragentGroupRequest);

    UpdateContragentGroupResponse updateGroups(UpdateTreeItemRequest<ContragentGroupDto> request);

    UpdateContragentGroupResponse deleteGroups(DeleteTreeItemRequest request);

    ObtainContragentResponse getContragents(ObtainContragentRequest obtainContragentRequest);

    UpdateContragentResponse updateContragents(UpdateContragentRequest request);

    UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest request);

}
