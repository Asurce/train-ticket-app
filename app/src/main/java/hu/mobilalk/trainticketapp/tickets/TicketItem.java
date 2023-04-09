package hu.mobilalk.trainticketapp.tickets;

public class TicketItem {
    private String originCity;
    private String destCity;
    private int price;
    private long date;
    private String userID;

    public TicketItem(String originCity, String destCity, int price, long date, String userID) {
        this.originCity = originCity;
        this.destCity = destCity;
        this.price = price;
        this.date = date;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
