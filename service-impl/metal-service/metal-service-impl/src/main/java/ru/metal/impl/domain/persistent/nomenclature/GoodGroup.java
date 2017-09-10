package ru.metal.impl.domain.persistent.nomenclature;

import ru.metal.impl.domain.persistent.BaseEntity;
import ru.metal.impl.domain.persistent.catalog.Catalog;
import ru.metal.impl.domain.persistent.contragents.Contragent;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 09.08.2017.
 */
@Entity
@Table(name = "GROUPS")
public class GoodGroup extends BaseEntity implements Catalog<Good>{

    @Column(name = "GROUP_NAME")
    private String name;

    @Column(name = "GROUP_GUID")
    private String groupGuid;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "USER_GUID")
    private String userGuid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Good> items;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public List<Good> getItems() {
        return items;
    }

    public void setItems(List<Good> items) {
        this.items = items;
    }
}
