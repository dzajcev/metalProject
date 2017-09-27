package ru.metal.api.contragents.request;

import ru.common.api.request.ObtainTreeItemRequest;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.dto.PersonType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public class ObtainContragentGroupRequest extends ObtainTreeItemRequest {

    private List<String> contragentGuids;

    private List<PersonType> personTypes = new ArrayList<>();

    private List<ContragentType> contragentTypes = new ArrayList<>();

    public List<PersonType> getPersonTypes() {
        return personTypes;
    }

    public void setPersonTypes(List<PersonType> personTypes) {
        this.personTypes = personTypes;
    }

    public List<ContragentType> getContragentTypes() {
        return contragentTypes;
    }

    public void setContragentTypes(List<ContragentType> contragentTypes) {
        this.contragentTypes = contragentTypes;
    }

    public List<String> getContragentGuids() {
        if (contragentGuids==null){
            contragentGuids=new ArrayList<>();
        }
        return contragentGuids;
    }

    public void setContragentGuids(List<String> contragentGuids) {
        this.contragentGuids = contragentGuids;
    }
}
