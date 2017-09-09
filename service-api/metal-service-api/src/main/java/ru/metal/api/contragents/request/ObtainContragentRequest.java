package ru.metal.api.contragents.request;

import ru.metal.api.common.request.ObtainAbstractRequest;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.dto.PersonType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public class ObtainContragentRequest extends ObtainAbstractRequest {
    private boolean active;

    private List<String> groupGuids;

    private List<PersonType> personTypes=new ArrayList<>();

    private List<ContragentType> contragentTypes=new ArrayList<>();

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getGroupGuids() {
        if (groupGuids==null){
            groupGuids=new ArrayList<>();
        }
        return groupGuids;
    }

    public void setGroupGuids(List<String> groupGuids) {
        this.groupGuids = groupGuids;
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