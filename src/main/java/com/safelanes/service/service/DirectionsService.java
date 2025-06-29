package com.safelanes.service.service;

import com.safelanes.service.dto.Coordinate;
import com.safelanes.service.dto.PathResponse;
import com.safelanes.service.dto.ScoredCoordinate;
import com.safelanes.service.service.PolylineDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public List<ScoredCoordinate> getWalkingPath(Coordinate src, Coordinate dest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?origin=%f,%f&destination=%f,%f&mode=walking&alternatives=true&key=%s",
                DIRECTIONS_URL, src.getLat(), src.getLon(), dest.getLat(), dest.getLon(), API_KEY);
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        List<PathResponse> pathResponses = new ArrayList<>();
        if (json.getString("status").equals("OK")) {
            JSONArray routes = json.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                String polyline = route.getJSONObject("overview_polyline").getString("points");
                List<Coordinate> coordinates = PolylineDecoder.decodeWithInterpolation(polyline,10);
                pathResponses.add(new PathResponse(coordinates));
            }
        }
        lastScoredPaths = scoringService.scorePaths(pathResponses);

        // Ensure lastScoredPaths is not empty
        if (lastScoredPaths == null || lastScoredPaths.isEmpty()) {
            return new ArrayList<>();
        }

        // If only one path, return it; otherwise, find the safest path
        if (lastScoredPaths.size() == 1) {
            return lastScoredPaths.get(0);
        } else {
            return safePathService.findSafestPath(lastScoredPaths);
        }
    }
}