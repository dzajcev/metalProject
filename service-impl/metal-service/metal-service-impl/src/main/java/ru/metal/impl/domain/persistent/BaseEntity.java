package ru.metal.impl.domain.persistent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{


    @Id
    @Column(name = "GUID", nullable = false)
    protected String guid;

    @Column(name = "LAST_EDITING_DATE", nullable = false)
    protected Date lastEditingDate;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getLastEditingDate() {
        return lastEditingDate;
    }

    public void setLastEditingDate(Date lastEditingDate) {
        this.lastEditingDate = lastEditingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return !(guid != null ? !guid.equals(that.guid) : that.guid != null);

    }

    @Override
    public int hashCode() {
        return guid != null ? guid.hashCode() : 0;
    }

    @PrePersist
    protected void prePersist() {
        if (guid==null) {
            guid = UUID.randomUUID().toString();
        }
        lastEditingDate = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        setLastEditingDate(new Date());
    }
}

