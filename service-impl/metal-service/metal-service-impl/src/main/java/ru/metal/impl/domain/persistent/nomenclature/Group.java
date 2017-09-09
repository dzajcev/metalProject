package ru.metal.impl.domain.persistent.nomenclature;

import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by User on 09.08.2017.
 */
@Entity
@Table(name = "GROUPS")
public class Group extends BaseEntity{

    @Column(name = "GROUP_NAME")
    private String name;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "USER_GUID")
    private String userGuid;

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
}
