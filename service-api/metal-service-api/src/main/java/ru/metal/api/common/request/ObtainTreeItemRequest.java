package ru.metal.api.common.request;

import java.io.Serializable;

/**
 * Created by User on 15.08.2017.
 */
public class ObtainTreeItemRequest extends ObtainAbstractRequest {
    private boolean active;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }
}
