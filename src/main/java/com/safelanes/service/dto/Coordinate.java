package com.safelanes.service.dto;

public class Coordinate {
    private String lat;
    private String lon;

    public Coordinate(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() { return lat; }
    public String getLon() { return lon; }
    @Override
    public String toString() {
        return "Coordinate{lat=" + lat + ", lon=" + lon + "}";
    }
}