package ru.metal.api.auth.dto;

import ru.common.api.dto.AbstractDto;

/**
 * Created by User on 11.09.2017.
 */
public class Position extends AbstractDto{

    private String title;

    private boolean active;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
