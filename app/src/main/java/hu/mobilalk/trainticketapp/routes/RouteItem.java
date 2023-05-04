package hu.mobilalk.trainticketapp.routes;

import java.io.Serializable;
import java.util.Date;

import hu.mobilalk.trainticketapp.models.City;
import hu.mobilalk.trainticketapp.enums.Comfort;
import hu.mobilalk.trainticketapp.enums.Discount;

public class RouteItem implements Serializable {
    private final City originCity;
    private final City destCity;
    private final Date departTime;
    private final Date arriveTime;
    private final Integer travelTime;
    private final Discount discount;
    private final Comfort comfort;
    private final Integer distance;
    private final Integer price;

    public RouteItem(City originCity, City destCity, Date departTime, Date arriveTime, Integer travelTime, Discount discount, Comfort comfort, Integer distance, Integer price) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.travelTime = travelTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
    }

    public City getOriginCity() {
        return originCity;
    }

    public City getDestCity() {
        return destCity;
    }

    public Date getDepartTime() {
        return departTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Comfort getComfort() {
        return comfort;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getPrice() {
        return price;
    }
}