package ru.metal.gui.controllers.catalogs.nomenclature;

import javafx.collections.FXCollections;
import ru.common.api.dto.UpdateResult;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.dto.GroupFx;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.gui.controllers.catalogs.GroupSelector;
import ru.metal.rest.NomenclatureClient;

import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public class NomenclatureGroupSelector implements GroupSelector<GroupFx> {

    private final NomenclatureClient nomenclatureClient;

    private List<GroupFx> groups;

    public NomenclatureGroupSelector(NomenclatureClient nomenclatureClient) {
        this.nomenclatureClient = nomenclatureClient;

        ObtainTreeItemRequest request = new ObtainTreeItemRequest();

        ObtainTreeItemResponse<GroupFx> response = nomenclatureClient.getGroupItems(request);
        groups = response.getDataList();
    }

    @Override
    public List<GroupFx> getGroups(boolean active) {
        List<GroupFx> result = FXCollections.observableArrayList();
        if (active) {
            for (GroupFx fx : groups) {
                if (fx.getActive()) {
                    result.add(fx);
                }
            }
        } else {
            result.addAll(groups);
        }
        return result;
    }

    @Override
    public UpdateTreeItemResponse<? extends UpdateResult> updateGroups(List<GroupFx> list) {
        UpdateTreeItemRequest<GroupFx> request = new UpdateTreeItemRequest<>();
        request.setDataList(list);
        UpdateTreeItemResponse updateTreeItemResponse = nomenclatureClient.updateGroupItems(request);
        return updateTreeItemResponse;
    }

    @Override
    public UpdateTreeItemResponse deleteGroup(List<String> list) {
        DeleteTreeItemRequest request = new DeleteTreeItemRequest();
        request.setDataList(list);
        return nomenclatureClient.deleteGroupItem(request);
    }
}
