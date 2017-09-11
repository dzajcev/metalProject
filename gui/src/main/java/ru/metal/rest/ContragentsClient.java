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
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class ContragentsClient extends AbstractRestClient implements TreeClient<ContragentGroupFx> {
    private final String basePath="metal";
    private final String pathGroup = basePath+"/contragents/groups";
    private final String pathContragent = basePath+"/contragents/contragent";
    private final String pathEmployee = basePath+"/contragents/employee";

    @Override
    public ObtainTreeItemResponse<ContragentGroupFx> getItems(ObtainTreeItemRequest obtainTreeItemRequest) throws ServerErrorException {
        ObtainContragentGroupReponse execute = execute(pathGroup + "/get", RequestType.POST, obtainTreeItemRequest, ObtainContragentGroupReponse.class);
        ObtainTreeItemResponse<ContragentGroupFx> response = new ObtainTreeItemResponse<ContragentGroupFx>();
        response.setErrors(execute.getErrors());
        response.setDataList(ContragentGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    @Override
    public UpdateTreeItemResponse updateItems(UpdateTreeItemRequest<ContragentGroupFx> updateTreeItemRequest) throws ServerErrorException {
        UpdateTreeItemRequest<ContragentGroupDto> request = new UpdateTreeItemRequest<>();
        request.setDataList(ContragentGroupHelper.getInstance().getDtoCollection(updateTreeItemRequest.getDataList()));
        UpdateContragentGroupResponse execute = execute(pathGroup + "/update", RequestType.POST, request, UpdateContragentGroupResponse.class);
        UpdateTreeItemResponse response = new UpdateTreeItemResponse();
        response.setError(execute.getError());
        response.setImportResults(execute.getImportResults());
        return response;
    }

    @Override
    public UpdateTreeItemResponse deleteItem(DeleteTreeItemRequest<ContragentGroupFx> deleteTreeItemRequest) throws ServerErrorException {
        UpdateContragentGroupResponse execute = execute(pathGroup + "/delete", RequestType.POST, deleteTreeItemRequest, UpdateContragentGroupResponse.class);
        return execute;
    }

    public ObtainContragentResponse getContragents(ObtainContragentRequest obtainContragentRequest) throws ServerErrorException {
        ObtainContragentResponse execute = execute(pathContragent + "/get", RequestType.POST, obtainContragentRequest, ObtainContragentResponse.class);
        return execute;
    }

    public UpdateContragentResponse updateContragents(UpdateContragentRequest updateContragentRequest) throws ServerErrorException {
        UpdateContragentResponse execute = execute(pathContragent + "/update", RequestType.POST, updateContragentRequest, UpdateContragentResponse.class);
        return execute;
    }

    public UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest updateEmployeeRequest) throws ServerErrorException {
        UpdateEmployeeResponse execute = execute(pathEmployee + "/update", RequestType.POST, updateEmployeeRequest, UpdateEmployeeResponse.class);
        return execute;
    }
}
