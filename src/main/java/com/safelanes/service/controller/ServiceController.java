package com.safelanes.service.controller;

import com.safelanes.service.dto.Coordinate;
import com.safelanes.service.dto.PathResponse;
import com.safelanes.service.dto.ScoredCoordinate;
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
    public List<ScoredCoordinate> getWalkingPaths(@RequestParam String source, @RequestParam String destination) {
        System.out.println(source);
        System.out.println(destination);
        Coordinate srcCoord = geocodingService.geocode(source);
        System.out.println(srcCoord);
        Coordinate destCoord = geocodingService.geocode(destination);
        System.out.println(destCoord);
        return directionsService.getWalkingPath(srcCoord, destCoord);
    }
}