package com.safelanes.service.service;

import com.safelanes.service.dto.Coordinate;
import com.safelanes.service.dto.PathResponse;
import com.safelanes.service.dto.ScoredCoordinate;
import com.safelanes.service.service.PolylineDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectionsService {
    @Value("${google.directions.key}")
    private String API_KEY;
    @Value("${google.directions.url}")
    private String DIRECTIONS_URL;

    @Autowired
    private ScoringService scoringService;
    @Autowired
    private SafePathService safePathService;
    private List<List<ScoredCoordinate>> lastScoredPaths = new ArrayList<>();

    @Cacheable(value = "safestPaths", keyGenerator = "customKeyGenerator")
    public List<ScoredCoordinate> getWalkingPath(Coordinate src, Coordinate dest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?origin=%s,%s&destination=%s,%s&mode=walking&alternatives=true&key=%s",
                DIRECTIONS_URL, src.getLat(), src.getLon(), dest.getLat(), dest.getLon(), API_KEY);

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        List<PathResponse> pathResponses = new ArrayList<>();

        if (json.getString("status").equals("OK")) {
            JSONArray routes = json.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                String polyline = route.getJSONObject("overview_polyline").getString("points");

                List<Coordinate> coordinates = PolylineDecoder.decodeWithInterpolation(polyline, 5.0);
                System.out.println("Route " + (i + 1) + ": " + coordinates.size() + " points");

                // Print first and last coordinates of each route
                if (!coordinates.isEmpty()) {
                    Coordinate start = coordinates.get(0);
                    Coordinate end = coordinates.get(coordinates.size() - 1);
                    System.out.println("  Start: (" + start.getLat() + ", " + start.getLon() + ")");
                    System.out.println("  End: (" + end.getLat() + ", " + end.getLon() + ")");
                    System.out.println();
                }

                pathResponses.add(new PathResponse(coordinates));
            }
        }

        lastScoredPaths = scoringService.scorePaths(pathResponses);

        if (lastScoredPaths == null || lastScoredPaths.isEmpty()) {
            return new ArrayList<>();
        }

        return (lastScoredPaths.size() == 1)
                ? lastScoredPaths.get(0)
                : safePathService.findSafestPath(lastScoredPaths);
    }

}