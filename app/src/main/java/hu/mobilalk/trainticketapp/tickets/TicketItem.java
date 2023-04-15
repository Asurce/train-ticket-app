package hu.mobilalk.trainticketapp.tickets;

public class TicketItem {

    private String originCity;
    private String destCity;
    private Long departTime;
    private Long arriveTime;
    private Double discount;
    private Integer comfort;
    private Integer distance;
    private Integer price;
    private String userID;

    public TicketItem(String originCity, String destCity, long departTime, long arriveTime, double discount, int comfort, Integer distance, Integer price, String userID) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.discount = discount;
        this.comfort = comfort;
        this.distance = distance;
        this.price = price;
        this.userID = userID;
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
