package ru.metal.api.users.request;

import ru.common.api.request.ObtainAbstractRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 23.09.2017.
 */
public class ObtainDelegatingUsersRequest extends ObtainAbstractRequest {
    private List<String> userGuids;

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
