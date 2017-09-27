package ru.metal.gui.controllers.catalogs.contragents;

import javafx.collections.FXCollections;
import ru.common.api.dto.UpdateResult;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.api.contragents.request.ObtainContragentGroupRequest;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.gui.controllers.catalogs.GroupSelector;
import ru.metal.gui.controllers.catalogs.SelectCriteria;
import ru.metal.rest.ContragentsClient;

import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public class ContragentGroupSelector implements GroupSelector<ContragentGroupFx>, SelectCriteria<ContragentBaseCriteria> {

    private final ContragentsClient contragentsClient;

    private final ContragentBaseCriteria contragentBaseCriteria;

    private List<ContragentGroupFx> groups;

    public ContragentGroupSelector(ContragentsClient contragentsClient, ContragentBaseCriteria contragentBaseCriteria) {
        this.contragentsClient = contragentsClient;
        this.contragentBaseCriteria = contragentBaseCriteria;

        ObtainContragentGroupRequest request = new ObtainContragentGroupRequest();

        if (!contragentBaseCriteria.getContragentTypes().isEmpty()) {
            request.setContragentTypes(contragentBaseCriteria.getContragentTypes());
        }
        if (!contragentBaseCriteria.getUserGuids().isEmpty()) {
            request.setUserGuids(contragentBaseCriteria.getUserGuids());
        }
        ObtainTreeItemResponse<ContragentGroupFx> response = contragentsClient.getGroupFullItems(request);
        groups = response.getDataList();
    }

    @Override
    public List<ContragentGroupFx> getGroups(boolean active) {
        List<ContragentGroupFx> result = FXCollections.observableArrayList();
        if (active) {
            for (ContragentGroupFx fx : groups) {
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
    public UpdateTreeItemResponse<? extends UpdateResult> updateGroups(List<ContragentGroupFx> list) {
        UpdateTreeItemRequest<ContragentGroupFx> request=new UpdateTreeItemRequest<>();
        request.setDataList(list);
        UpdateTreeItemResponse updateTreeItemResponse = contragentsClient.updateGroupItems(request);
        return updateTreeItemResponse;
    }

    @Override
    public UpdateTreeItemResponse deleteGroup(List<String> list) {
        DeleteTreeItemRequest request = new DeleteTreeItemRequest();
        request.setDataList(list);
        return contragentsClient.deleteGroupItem(request);
    }

    @Override
    public ContragentBaseCriteria getBaseCriteria() {
        return contragentBaseCriteria;
    }
}
