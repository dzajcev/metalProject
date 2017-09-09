package ru.metal.impl.domain.persistent.order;

import ru.metal.impl.domain.persistent.BaseEntity;
import ru.metal.impl.domain.persistent.contragents.Contragent;
import ru.metal.impl.domain.persistent.contragents.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 31.08.2017.
 */
@Entity
@Table(name = "ORDER_HEADER")
public class OrderHeader extends BaseEntity {

    @Column(name = "CREATE_DATE", nullable = false)
    protected Date createDate;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SOURCE_GUID")
    private Contragent source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECIPIENT_GUID")
    private Contragent recipient;

    @Column(name = "DATE_ORDER", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOrder;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "USER_GUID")
    private String userGuid;

    @Column(name = "ACTIVE")
    private boolean active;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderBody> body;


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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Contragent getSource() {
        return source;
    }

    public void setSource(Contragent source) {
        this.source = source;
    }

    public Contragent getRecipient() {
        return recipient;
    }

    public void setRecipient(Contragent recipient) {
        this.recipient = recipient;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @PrePersist
    protected void prePersist() {
        super.prePersist();
        createDate = new Date();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OrderBody> getBody() {
        return body;
    }

    public void setBody(List<OrderBody> body) {
        this.body = body;
    }
}
