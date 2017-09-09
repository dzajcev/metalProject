package ru.metal.impl.domain.persistent.order;

import ru.metal.impl.domain.persistent.BaseEntity;
import ru.metal.impl.domain.persistent.contragents.Contragent;
import ru.metal.impl.domain.persistent.nomenclature.Good;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 31.08.2017.
 */
@Entity
@Table(name = "ORDER_BODY",uniqueConstraints = {@UniqueConstraint(columnNames={"ORDER_GUID","GOOD_GUID"})})

public class OrderBody extends BaseEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORDER_GUID")
    private OrderHeader order;


    @ManyToOne(optional = false)
    @JoinColumn(name = "GOOD_GUID")
    private Good good;

    @Column(name = "price",nullable = false)
    private Double price;

    @Column(name = "count",nullable = false)
    private Double count;

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public OrderHeader getOrder() {
        return order;
    }

    public void setOrder(OrderHeader order) {
        this.order = order;
    }
}
