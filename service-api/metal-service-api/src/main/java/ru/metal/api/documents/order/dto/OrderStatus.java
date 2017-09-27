package ru.metal.api.documents.order.dto;

import ru.common.api.dto.ComboBoxElement;
import ru.metal.api.documents.DocumentStatus;

/**
 * Created by User on 20.09.2017.
 */
public enum OrderStatus implements DocumentStatus {
    DRAFT("Черновик"),
    PAID_FOR("Оплачен")

    ;


    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }



}
