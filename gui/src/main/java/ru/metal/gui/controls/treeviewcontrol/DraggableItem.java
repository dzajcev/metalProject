package ru.metal.gui.controls.treeviewcontrol;

import java.io.Serializable;

/**
 * Created by User on 16.08.2017.
 */
public class DraggableItem implements Serializable {
    private String guid;

    private String groupGuid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }
}
