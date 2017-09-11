package ru.metal.rest;

import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.dto.GroupFx;
import ru.metal.dto.helper.GoodGroupHelper;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 08.08.2017.
 */

public class NomenclatureClient extends AbstractRestClient implements TreeClient<GroupFx> {
    private final String basePath="metal";
    private final String pathGroup = basePath+"/nomenclature/groups";
    private final String pathGood = basePath+"/nomenclature/goods";
    private final String pathOkei = basePath+"/nomenclature/okei";

    @Override
    public ObtainTreeItemResponse<GroupFx> getItems(ObtainTreeItemRequest obtainTreeItemRequest) throws ServerErrorException {
        ObtainGroupReponse execute = execute(pathGroup + "/get", RequestType.POST, obtainTreeItemRequest, ObtainGroupReponse.class);
        ObtainTreeItemResponse<GroupFx> response = new ObtainTreeItemResponse<>();
        response.setErrors(execute.getErrors());
        response.setDataList(GoodGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    @Override
    public UpdateTreeItemResponse updateItems(UpdateTreeItemRequest<GroupFx> updateTreeItemRequest) throws ServerErrorException {
        UpdateTreeItemRequest<GroupDto> request = new UpdateTreeItemRequest<>();
        request.setDataList(GoodGroupHelper.getInstance().getDtoCollection(updateTreeItemRequest.getDataList()));
        UpdateGoodGroupResponse execute = execute(pathGroup + "/update", RequestType.POST, request, UpdateGoodGroupResponse.class);
        UpdateTreeItemResponse response = new UpdateTreeItemResponse();
        response.setError(execute.getError());
        response.setImportResults(execute.getImportResults());
        return response;
    }

    @Override
    public UpdateGoodGroupResponse deleteItem(DeleteTreeItemRequest<GroupFx> deleteTreeItemRequest) throws ServerErrorException {
        return execute(pathGroup + "/delete", RequestType.POST, deleteTreeItemRequest, UpdateGoodGroupResponse.class);
    }


    public ObtainGoodResponse getGoods(ObtainGoodRequest obtainGoodRequest) throws ServerErrorException {
        ObtainGoodResponse execute = execute(pathGood + "/get", RequestType.POST, obtainGoodRequest, ObtainGoodResponse.class);
        return execute;
    }


    public UpdateGoodsResponse updateGoods(UpdateGoodsRequest updateGoodsRequest) throws ServerErrorException {
        UpdateGoodsResponse execute = execute(pathGood + "/update", RequestType.POST, updateGoodsRequest, UpdateGoodsResponse.class);
        return execute;
    }

    public ObtainOkeiResponse getOkei(ObtainOkeiRequest obtainOkeiRequest) throws ServerErrorException {
        ObtainOkeiResponse execute = execute(pathOkei + "/get", RequestType.POST, obtainOkeiRequest, ObtainOkeiResponse.class);
        return execute;
    }

}
