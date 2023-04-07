package hu.mobilalk.trainticketapp;

import java.util.Date;

public class SearchResultItem {
    private String fromCity;
    private String toCity;
    private Date date;
    private int discount;
    private int trainClass;

    public SearchResultItem(String fromCity, String toCity, Date date, int discount, int trainClass) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.date = date;
        this.discount = discount;
        this.trainClass = trainClass;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
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

    public int getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(int trainClass) {
        this.trainClass = trainClass;
    }
}