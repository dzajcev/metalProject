package ru.metal.gui.controls;

import ru.common.api.dto.TableElement;
import ru.common.api.dto.TreeviewElement;

import java.io.Serializable;

/**
 * Created by User on 23.09.2017.
 */
public class MultiSelectTreeValue<G extends TreeviewElement, I extends TableElement<G>> implements Serializable {

    public static final int UNSELECT = 0;
    public static final int FULL_SELECT = 1;
    public static final int HALF_SELECT = 2;


    private int selectMode;
    private G group;

    private I item;

    public G getGroup() {
        return group;
    }

    public void setGroup(G group) {
        this.group = group;
    }

    public I getItem() {
        return item;
    }

    public void setItem(I item) {
        this.item = item;
    }

    public boolean isLeaf() {
        return group == null && item != null;
    }

    public int getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }
}
