package ru.metal.api.nomenclature;

import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public interface NomenclatureFacade {

    ObtainGroupReponse getGroups(ObtainTreeItemRequest obtainTreeItemRequest);

    UpdateGroupResponse updateGroups(UpdateTreeItemRequest<GroupDto> request);

    ObtainGoodResponse getGoods(ObtainGoodRequest obtainGoodRequest);

    UpdateGoodsResponse updateGoods(UpdateGoodsRequest request);

    ObtainOkeiResponse getOkei(ObtainOkeiRequest obtainOkeiRequest);



}
