package hu.mobilalk.trainticketapp.tickets;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class TicketItem implements Serializable {

    private String originCity;
    private String destCity;
    private Long departTime;
    private Long arriveTime;
    private Integer travelTime;
    private String discount;
    private String comfort;
    private Integer distance;
    private Integer price;
    private String userID;
    @Exclude
    private String ticketID;

    public TicketItem(String originCity, String destCity, Long departTime, Long arriveTime, Integer travelTime, String discount, String comfort, Integer distance, Integer price, String userID) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.travelTime = travelTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
        this.userID = userID;
    }

    public TicketItem(String originCity, String destCity, Long departTime, Long arriveTime, Integer travelTime, String discount, String comfort, Integer distance, Integer price, String userID, String ticketID) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.travelTime = travelTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
        this.userID = userID;
        this.ticketID = ticketID;
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

    public Long getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Long departTime) {
        this.departTime = departTime;
    }

    public Long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Exclude
    public String getTicketID() {
        return ticketID;
    }

    @Exclude
    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }
}
