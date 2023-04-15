package hu.mobilalk.trainticketapp;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private Integer distance;
    private Integer routeID;

    public City(String name, Integer distance, Integer routeID) {
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
