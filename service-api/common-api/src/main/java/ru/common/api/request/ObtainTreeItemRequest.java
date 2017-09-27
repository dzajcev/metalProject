package ru.common.api.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15.08.2017.
 */
public class ObtainTreeItemRequest extends ObtainAbstractRequest {
    private boolean active;

    private List<String> groupGuids;

    private List<String> userGuids;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getGroupGuids() {
        if (groupGuids == null) {
            groupGuids = new ArrayList<>();
        }
        return groupGuids;
    }

    public void setGroupGuids(List<String> groupGuids) {
        this.groupGuids = groupGuids;
    }

    public List<String> getUserGuids() {
        if (userGuids==null){
            userGuids=new ArrayList<>();
        }
        return userGuids;
    }

    public void setUserGuids(List<String> userGuids) {
        this.userGuids = userGuids;
    }
}
