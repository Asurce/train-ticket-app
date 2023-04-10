package hu.mobilalk.trainticketapp;

public class City {
    private String name;
    private Long distance;
    private Long routeID;

    public City(String name, Long distance, Long routeID) {
        this.name = name;
        this.distance = distance;
        this.routeID = routeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getRouteID() {
        return routeID;
    }

    public void setRouteID(Long routeID) {
        this.routeID = routeID;
    }
}
