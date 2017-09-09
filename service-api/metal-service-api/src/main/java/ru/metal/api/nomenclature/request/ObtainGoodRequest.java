package ru.metal.api.nomenclature.request;

import ru.metal.api.common.request.ObtainAbstractRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public class ObtainGoodRequest extends ObtainAbstractRequest {
    private boolean active;

    private List<String> groupGuids;

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
}
