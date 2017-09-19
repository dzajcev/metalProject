package ru.common.api.dto;

import java.io.Serializable;

/**
 * Created by User on 18.09.2017.
 */
public class Pair<L,R> implements Serializable {
    private L left;

    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }
}
