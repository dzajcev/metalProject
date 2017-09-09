package ru.metal.impl.domain.persistent.nomenclature;

import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by User on 09.08.2017.
 */
@Entity
@Table(name = "OKEI")
public class Okei extends BaseEntity{

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
