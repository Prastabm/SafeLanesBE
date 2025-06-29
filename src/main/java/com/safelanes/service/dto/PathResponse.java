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
}