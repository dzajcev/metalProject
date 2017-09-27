package ru.metal.rest;

import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.dto.helper.ContragentGroupHelper;
import ru.metal.dto.response.ObtainTreeItemResponse;

/**
 * Created by User on 08.08.2017.
 */

public class ContragentsClient extends AbstractRestClient implements TreeClient<ContragentGroupFx> {
    private final String basePath = "metal";
    private final String pathGroup = basePath + "/contragents/groups";
    private final String pathContragent = basePath + "/contragents/contragent";
    private final String pathEmployee = basePath + "/contragents/employee";

    @Override
    public ObtainTreeItemResponse<ContragentGroupFx> getGroupItems(ObtainTreeItemRequest obtainTreeItemRequest) {
        ObtainContragentGroupResponse execute = executePost(pathGroup + "/get", obtainTreeItemRequest, ObtainContragentGroupResponse.class);
        ObtainTreeItemResponse<ContragentGroupFx> response = new ObtainTreeItemResponse<ContragentGroupFx>();
        response.setErrors(execute.getErrors());
        response.setDataList(ContragentGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    public ObtainTreeItemResponse<ContragentGroupFx> getGroupFullItems(ObtainTreeItemRequest obtainTreeItemRequest) {
        ObtainContragentGroupResponse execute = executePost(pathGroup + "/getFull", obtainTreeItemRequest, ObtainContragentGroupResponse.class);
        ObtainTreeItemResponse<ContragentGroupFx> response = new ObtainTreeItemResponse<ContragentGroupFx>();
        response.setErrors(execute.getErrors());
        response.setDataList(ContragentGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    @Override
    public UpdateTreeItemResponse updateGroupItems(UpdateTreeItemRequest<ContragentGroupFx> updateTreeItemRequest) {
        UpdateTreeItemRequest<ContragentGroupDto> request = new UpdateTreeItemRequest<>();
        request.setDataList(ContragentGroupHelper.getInstance().getDtoCollection(updateTreeItemRequest.getDataList()));
        UpdateContragentGroupResponse execute = executePost(pathGroup + "/update", request, UpdateContragentGroupResponse.class);
        UpdateTreeItemResponse response = new UpdateTreeItemResponse();
        response.getErrors().addAll(execute.getErrors());
        response.setImportResults(execute.getImportResults());
        return response;
    }

    @Override
    public UpdateTreeItemResponse deleteGroupItem(DeleteTreeItemRequest deleteTreeItemRequest) {
        UpdateContragentGroupResponse execute = executePost(pathGroup + "/delete", deleteTreeItemRequest, UpdateContragentGroupResponse.class);
        return execute;
    }

    public ObtainContragentResponse getContragents(ObtainContragentRequest obtainContragentRequest) {
        ObtainContragentResponse execute = executePost(pathContragent + "/get", obtainContragentRequest, ObtainContragentResponse.class);
        return execute;
    }

    public UpdateContragentResponse updateContragents(UpdateContragentRequest updateContragentRequest) {
        UpdateContragentResponse execute = executePost(pathContragent + "/update", updateContragentRequest, UpdateContragentResponse.class);
        return execute;
    }

    public UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest updateEmployeeRequest) {
        UpdateEmployeeResponse execute = executePost(pathEmployee + "/update", updateEmployeeRequest, UpdateEmployeeResponse.class);
        return execute;
    }
}
