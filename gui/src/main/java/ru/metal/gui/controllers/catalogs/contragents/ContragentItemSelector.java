package ru.metal.gui.controllers.catalogs.contragents;

import javafx.collections.FXCollections;
import ru.common.api.dto.UpdateResult;
import ru.common.api.response.UpdateAbstractResponse;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.response.ObtainContragentResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.gui.controllers.catalogs.ItemSelector;
import ru.metal.gui.controllers.catalogs.SelectCriteria;
import ru.metal.rest.ContragentsClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public class ContragentItemSelector implements ItemSelector<ContragentFx>, SelectCriteria<ContragentBaseCriteria> {

    private ContragentBaseCriteria contragentBaseCriteria;

    private List<String> groups;

    private ContragentsClient contragentsClient;
    private List<ContragentFx> contragents;

    public ContragentItemSelector(ContragentsClient contragentsClient, ContragentBaseCriteria contragentBaseCriteria) {
        this.contragentBaseCriteria = contragentBaseCriteria;
        this.contragentsClient = contragentsClient;
        ObtainContragentRequest obtainContragentRequest = new ObtainContragentRequest();
        obtainContragentRequest.setContragentTypes(contragentBaseCriteria.getContragentTypes());
        obtainContragentRequest.setUserGuids(contragentBaseCriteria.getUserGuids());
        ObtainContragentResponse contragentResponse = contragentsClient.getContragents(obtainContragentRequest);
        contragents = ContragentHelper.getInstance().getFxCollection(contragentResponse.getDataList());
    }


    @Override
    public ContragentBaseCriteria getBaseCriteria() {
        return contragentBaseCriteria;
    }

    @Override
    public List<ContragentFx> getItems(List<String> groups, boolean active) {
        List<ContragentFx> result = FXCollections.observableArrayList();
        if (active || !groups.isEmpty()) {
            for (ContragentFx fx : contragents) {
                boolean toAdd = false;
                if (active) {
                    if (fx.getActive()) {
                        toAdd = true;
                    }
                }else{
                    toAdd=true;
                }
                if (!groups.isEmpty()) {
                    if (groups.contains(fx.getGroup().getGuid())) {
                        toAdd = toAdd & true;
                    }else{
                        toAdd = toAdd & false;
                    }
                }else{
                    toAdd=toAdd & true;
                }
                if (toAdd) {
                    result.add(fx);
                }
            }
        } else {
            result.addAll(contragents);
        }
        return result;
    }

    @Override
    public List<ContragentFx> getAllItems() {
        return contragents;
    }

    @Override
    public UpdateAbstractResponse<? extends UpdateResult> updateItems(List<ContragentFx> request) {
        UpdateContragentRequest contragentRequest=new UpdateContragentRequest();
        contragentRequest.setDataList(ContragentHelper.getInstance().getDtoCollection(request));
        return contragentsClient.updateContragents(contragentRequest);
    }


    public List<String> getGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
