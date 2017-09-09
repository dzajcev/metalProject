package ru.metal.api.contragents;

import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.dto.UpdateEmployeeResult;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.*;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface ContragentsFacade {

    ObtainContragentGroupReponse getGroups(ObtainContragentRequest obtainTreeItemRequest);

    UpdateContragentGroupResponse updateGroups(UpdateTreeItemRequest<ContragentGroupDto> request);

    ObtainContragentResponse getContragents(ObtainContragentRequest obtainContragentRequest);

    UpdateContragentResponse updateContragents(UpdateContragentRequest request);

    UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest request);

}
