package hu.mobilalk.trainticketapp.routes;

import java.io.Serializable;
import java.util.Date;

import hu.mobilalk.trainticketapp.City;
import hu.mobilalk.trainticketapp.enums.Comfort;
import hu.mobilalk.trainticketapp.enums.Discount;

public class RouteItem implements Serializable {
    private City originCity;
    private City destCity;
    private Date departTime;
    private Date arriveTime;
    private Discount discount;
    private Comfort comfort;
    private Integer distance;
    private Integer price;

    public RouteItem() {
    }

    public RouteItem(City originCity, City destCity, Date departTime, Date arriveTime, Discount discount, Comfort comfort, Integer distance, Integer price) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
    }

    public City getOriginCity() {
        return originCity;
    }

    public void setOriginCity(City originCity) {
        this.originCity = originCity;
    }

    public City getDestCity() {
        return destCity;
    }

    public void setDestCity(City destCity) {
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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Comfort getComfort() {
        return comfort;
    }

    public void setComfort(Comfort comfort) {
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