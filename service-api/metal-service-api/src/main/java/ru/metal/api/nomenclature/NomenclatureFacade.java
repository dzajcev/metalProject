package ru.metal.api.nomenclature;

import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.*;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface NomenclatureFacade {

    ObtainGroupReponse getGroups(ObtainTreeItemRequest obtainTreeItemRequest);

    UpdateGoodGroupResponse updateGroups(UpdateTreeItemRequest<GroupDto> request);

    UpdateGoodGroupResponse deleteGroups(DeleteTreeItemRequest<GroupDto> request);

    ObtainGoodResponse getGoods(ObtainGoodRequest obtainGoodRequest);

    UpdateGoodsResponse updateGoods(UpdateGoodsRequest request);

    ObtainOkeiResponse getOkei(ObtainOkeiRequest obtainOkeiRequest);


}
