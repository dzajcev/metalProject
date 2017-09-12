package ru.common.api.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15.08.2017.
 */
public class DeleteTreeItemRequest<T> extends UpdateAbstractRequest<T> {

    private List<String> guids;

    public List<String> getGuids() {
        if (guids == null) {
            guids = new ArrayList<>();
        }
        return guids;
    }

    public void setGuids(List<String> guids) {
        this.guids = guids;
    }
}
