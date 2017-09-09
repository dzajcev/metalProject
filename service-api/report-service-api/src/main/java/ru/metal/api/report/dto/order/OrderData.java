package ru.metal.api.report.dto.order;

import java.io.Serializable;

/**
 * Created by User on 07.09.2017.
 */
public class OrderData implements Serializable {

    private byte[] jasperData;

    public byte[] getJasperData() {
        return jasperData;
    }

    public void setJasperData(byte[] jasperData) {
        this.jasperData = jasperData;
    }
}
