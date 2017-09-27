package ru.metal.gui.controllers.catalogs.contragents;

import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.gui.controllers.catalogs.BaseCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public class ContragentBaseCriteria implements BaseCriteria {

    private List<ContragentType> contragentTypes;
    private List<String> userGuids;

    public List<ContragentType> getContragentTypes() {
        if (contragentTypes == null) {
            contragentTypes = new ArrayList<>();
        }
        return contragentTypes;
    }

    public void setContragentTypes(List<ContragentType> contragentTypes) {
        this.contragentTypes = contragentTypes;
    }

    public List<String> getUserGuids() {
        if (userGuids==null){
            this.userGuids=new ArrayList<>();
        }
        return userGuids;
    }

    public void setUserGuids(List<String> userGuids) {
        this.userGuids = userGuids;
    }
}
