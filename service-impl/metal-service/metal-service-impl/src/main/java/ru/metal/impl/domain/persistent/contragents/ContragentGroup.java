package ru.metal.impl.domain.persistent.contragents;

import ru.metal.api.common.dto.AbstractDto;
import ru.metal.api.common.dto.TreeviewElement;
import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 29.08.2017.
 */
@Entity
@Table(name = "CONTRAGENTS_GROUPS")
public class ContragentGroup extends BaseEntity {

    @Column(name = "GROUP_NAME")
    private String name;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "USER_GUID")
    private String userGuid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Contragent> contragents;

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

    public List<Contragent> getContragents() {
        return contragents;
    }

    public void setContragents(List<Contragent> contragents) {
        this.contragents = contragents;
    }
}
