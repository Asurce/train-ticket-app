package hu.mobilalk.trainticketapp.routes;

import java.io.Serializable;
import java.util.Date;

public class RouteItem implements Serializable {
    private String originCity;
    private String destCity;
    private Date departTime;
    private Date arriveTime;
    private Integer discount;
    private Integer comfort;
    private Integer distance;
    private Integer price;

    public RouteItem(String originCity, String destCity, Date departTime, Date arriveTime, Integer discount, Integer comfort, Integer distance, Integer price) {
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getComfort() {
        return comfort;
    }

    public void setComfort(Integer comfort) {
        this.comfort = comfort;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}