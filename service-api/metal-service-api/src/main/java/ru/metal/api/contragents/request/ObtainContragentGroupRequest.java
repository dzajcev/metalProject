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
    private boolean active;

    private List<PersonType> personTypes = new ArrayList<>();

    private List<ContragentType> contragentTypes = new ArrayList<>();

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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
}
