package ru.metal.impl.domain.persistent.order;

import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by User on 08.09.2017.
 */
@Entity
@Table(name = "ORDER_NUMBER")
public class OrderNumber extends BaseEntity {

    @Column(name = "ORDER_NUMBER")
    private Integer orderNumber;

    @Column(name = "SUFFIX")
    private String suffix;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNumber() {
        return orderNumber + (suffix != null ? suffix : "");
    }
}
