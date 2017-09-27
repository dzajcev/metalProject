package ru.metal.gui.controllers.catalogs.nomenclature;

import javafx.collections.FXCollections;
import ru.common.api.dto.UpdateResult;
import ru.common.api.response.UpdateAbstractResponse;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.ObtainGoodResponse;
import ru.metal.dto.GoodFx;
import ru.metal.dto.helper.GoodHelper;
import ru.metal.gui.controllers.catalogs.ItemSelector;
import ru.metal.rest.NomenclatureClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public class NomenclatureItemSelector implements ItemSelector<GoodFx> {

    private NomenclatureClient nomenclatureClient;
    private List<GoodFx> goods;

    public NomenclatureItemSelector(NomenclatureClient nomenclatureClient) {
        this.nomenclatureClient = nomenclatureClient;
        ObtainGoodRequest obtainGoodRequest = new ObtainGoodRequest();
        ObtainGoodResponse obtainGoodResponse = nomenclatureClient.getGoods(obtainGoodRequest);
        goods = GoodHelper.getInstance().getFxCollection(obtainGoodResponse.getDataList());
    }

    @Override
    public List<GoodFx> getItems(List<String> groups, boolean active) {
        List<GoodFx> result = FXCollections.observableArrayList();
        if (active || !groups.isEmpty()) {
            for (GoodFx fx : goods) {
                boolean toAdd = false;
                if (active) {
                    if (fx.getActive()) {
                        toAdd = true;
                    }
                } else {
                    toAdd = true;
                }
                if (!groups.isEmpty()) {
                    if (groups.contains(fx.getGroup().getGuid())) {
                        toAdd = toAdd & true;
                    } else {
                        toAdd = toAdd & false;
                    }
                } else {
                    toAdd = toAdd & true;
                }
                if (toAdd) {
                    result.add(fx);
                }
            }
        } else {
            result.addAll(goods);
        }
        return result;
    }

    @Override
    public List<GoodFx> getAllItems() {
        return goods;
    }

    @Override
    public UpdateAbstractResponse<? extends UpdateResult> updateItems(List<GoodFx> request) {
        UpdateGoodsRequest updateGoodsRequest=new UpdateGoodsRequest();
        updateGoodsRequest.setDataList(GoodHelper.getInstance().getDtoCollection(request));
        return nomenclatureClient.updateGoods(updateGoodsRequest);
    }
}
