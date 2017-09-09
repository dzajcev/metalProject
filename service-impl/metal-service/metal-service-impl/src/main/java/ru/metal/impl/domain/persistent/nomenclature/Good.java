package ru.metal.impl.domain.persistent.nomenclature;

import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by User on 09.08.2017.
 */
@Entity
@Table(name = "GOODS")
public class Good extends BaseEntity{
    @Column(name = "NAME")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name="GROUP_GUID")
    private Group group;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "nds",nullable = false)
    private Integer nds;

    @ManyToOne(optional = false)
    @JoinColumn(name="OKEI_GUID")
    private Okei okei;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Okei getOkei() {
        return okei;
    }

    public void setOkei(Okei okei) {
        this.okei = okei;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getNds() {
        return nds;
    }

    public void setNds(Integer nds) {
        this.nds = nds;
    }
}
