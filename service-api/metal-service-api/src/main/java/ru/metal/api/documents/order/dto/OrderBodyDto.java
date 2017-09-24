package ru.metal.api.documents.order.dto;

import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.api.nomenclature.dto.GoodDto;

/**
 * Created by User on 30.08.2017.
 */
public class OrderBodyDto extends AbstractDto {

    private GoodDto good;

    private Double price;

    private Double count;

    public GoodDto getGood() {
        return good;
    }

    public void setGood(GoodDto good) {
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
}
