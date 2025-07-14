package com.safelanes.service.dto;

public class Coordinate {
    private String lat;
    private String lng;

    public Coordinate(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() { return lat; }
    public String getLng() { return lng; }
    @Override
    public String toString() {
        return "Coordinate{lat=" + lat + ", lng=" + lng + "}";
    }
}