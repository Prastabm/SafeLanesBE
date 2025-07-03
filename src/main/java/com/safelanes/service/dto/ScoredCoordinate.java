package com.safelanes.service.dto;

import java.io.Serializable;

public class ScoredCoordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    private double lat;
    private double lon;
    private double score;

    // Getters and setters
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    @Override
    public String toString() {
        return "ScoredCoordinate{lat=" + lat + ", lon=" + lon + ", score=" + score + "}";
    }
}