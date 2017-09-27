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

/**
 * Created by User on 08.08.2017.
 */

public class NomenclatureClient extends AbstractRestClient implements TreeClient<GroupFx> {
    private final String basePath = "metal";
    private final String pathGroup = basePath + "/nomenclature/groups";
    private final String pathGood = basePath + "/nomenclature/goods";
    private final String pathOkei = basePath + "/nomenclature/okei";

    @Override
    public ObtainTreeItemResponse<GroupFx> getGroupItems(ObtainTreeItemRequest obtainTreeItemRequest) {
        ObtainGroupReponse execute = executePost(pathGroup + "/get", obtainTreeItemRequest, ObtainGroupReponse.class);
        ObtainTreeItemResponse<GroupFx> response = new ObtainTreeItemResponse<>();
        response.setErrors(execute.getErrors());
        response.setDataList(GoodGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    public ObtainTreeItemResponse<GroupFx> getGroupFullItems(ObtainTreeItemRequest obtainTreeItemRequest) {
        ObtainGroupReponse execute = executePost(pathGroup + "/getFull", obtainTreeItemRequest, ObtainGroupReponse.class);
        ObtainTreeItemResponse<GroupFx> response = new ObtainTreeItemResponse<>();
        response.setErrors(execute.getErrors());
        response.setDataList(GoodGroupHelper.getInstance().getFxCollection(execute.getDataList()));
        return response;
    }

    @Override
    public UpdateTreeItemResponse updateGroupItems(UpdateTreeItemRequest<GroupFx> updateTreeItemRequest) {
        UpdateTreeItemRequest<GroupDto> request = new UpdateTreeItemRequest<>();
        request.setDataList(GoodGroupHelper.getInstance().getDtoCollection(updateTreeItemRequest.getDataList()));
        UpdateGoodGroupResponse execute = executePost(pathGroup + "/update", request, UpdateGoodGroupResponse.class);
        UpdateTreeItemResponse response = new UpdateTreeItemResponse();
        response.getErrors().addAll(execute.getErrors());
        response.setImportResults(execute.getImportResults());
        return response;
    }

    @Override
    public UpdateGoodGroupResponse deleteGroupItem(DeleteTreeItemRequest deleteTreeItemRequest) {
        return executePost(pathGroup + "/delete", deleteTreeItemRequest, UpdateGoodGroupResponse.class);
    }


    public ObtainGoodResponse getGoods(ObtainGoodRequest obtainGoodRequest) {
        ObtainGoodResponse execute = executePost(pathGood + "/get", obtainGoodRequest, ObtainGoodResponse.class);
        return execute;
    }


    public UpdateGoodsResponse updateGoods(UpdateGoodsRequest updateGoodsRequest) {
        UpdateGoodsResponse execute = executePost(pathGood + "/update", updateGoodsRequest, UpdateGoodsResponse.class);
        return execute;
    }

    public ObtainOkeiResponse getOkei(ObtainOkeiRequest obtainOkeiRequest) {
        ObtainOkeiResponse execute = executePost(pathOkei + "/get", obtainOkeiRequest, ObtainOkeiResponse.class);
        return execute;
    }

}
