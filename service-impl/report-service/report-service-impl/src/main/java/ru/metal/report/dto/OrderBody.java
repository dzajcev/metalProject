package ru.metal.report.dto;

import java.io.Serializable;

/**
 * Created by User on 08.09.2017.
 */
public class OrderBody implements Serializable{
    private Integer npp;
    private String goodName;
    private String okei;
    private String count;
    private String price;
    private String summa;

    public Integer getNpp() {
        return npp;
    }

    public void setNpp(Integer npp) {
        this.npp = npp;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getOkei() {
        return okei;
    }

    public void setOkei(String okei) {
        this.okei = okei;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSumma() {
        return summa;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }
}
