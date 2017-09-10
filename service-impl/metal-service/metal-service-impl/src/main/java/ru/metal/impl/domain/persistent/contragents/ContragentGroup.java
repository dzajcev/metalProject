package ru.metal.impl.domain.persistent.contragents;

import ru.metal.impl.domain.persistent.BaseEntity;
import ru.metal.impl.domain.persistent.catalog.Catalog;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 29.08.2017.
 */
@Entity
@Table(name = "CONTRAGENTS_GROUPS")
public class ContragentGroup extends BaseEntity implements Catalog<Contragent> {

    @Column(name = "GROUP_NAME")
    private String name;

    @Column(name = "GROUP_GUID")
    private String groupGuid;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "USER_GUID")
    private String userGuid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Contragent> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active=active;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public List<Contragent> getItems() {
        return items;
    }

    public void setItems(List<Contragent> items) {
        this.items = items;
    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
