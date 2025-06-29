package com.safelanes.service.controller;

import com.safelanes.service.dto.Coordinate;
import com.safelanes.service.dto.PathResponse;
import com.safelanes.service.service.DirectionsService;
import com.safelanes.service.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceController {

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private DirectionsService directionsService;

    @GetMapping("/walking-path")
    public List<PathResponse> getWalkingPaths(@RequestParam String source, @RequestParam String destination) {
        Coordinate srcCoord = geocodingService.geocode(source);
        Coordinate destCoord = geocodingService.geocode(destination);
        return directionsService.getWalkingPath(srcCoord, destCoord);
    }
}