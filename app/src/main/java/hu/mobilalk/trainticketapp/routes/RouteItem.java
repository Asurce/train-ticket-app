package hu.mobilalk.trainticketapp.routes;

import java.io.Serializable;
import java.util.Date;

public class RouteItem implements Serializable {
    private String originCity;
    private String destCity;
    private Date date;
    private int discount;
    private int comfort;

    public RouteItem(String originCity, String destCity, Date date, int discount, int comfort) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.date = date;
        this.discount = discount;
        this.comfort = comfort;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getComfort() {
        return comfort;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }
}