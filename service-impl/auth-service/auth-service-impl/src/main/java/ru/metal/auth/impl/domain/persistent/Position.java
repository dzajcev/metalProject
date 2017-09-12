package ru.metal.auth.impl.domain.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by User on 11.09.2017.
 */
@Entity
@Table(name = "POSITIONS")
public class Position extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "ACTIVE")
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
