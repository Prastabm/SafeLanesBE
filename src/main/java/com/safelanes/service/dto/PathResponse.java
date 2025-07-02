package com.safelanes.service.dto;

import java.util.List;

public class PathResponse {
    private List<Coordinate> coordinates;

    public PathResponse(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PathResponse{coordinates=[");
        for (Coordinate coord : coordinates) {
            sb.append(coord.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}