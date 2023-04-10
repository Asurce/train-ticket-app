package hu.mobilalk.trainticketapp.routes;

import java.io.Serializable;
import java.util.Date;

public class RouteItem implements Serializable {
    private String originCity;
    private String destCity;
    private Date departTime;
    private Date arriveTime;
    private int discount;
    private int comfort;
    private Long distance;
    private int price;

    public RouteItem(String originCity, String destCity, Date departTime, Date arriveTime, int discount, int comfort, Long distance, int price) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
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

    public Date getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Date departTime) {
        this.departTime = departTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
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

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}