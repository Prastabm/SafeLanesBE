package com.safelanes.service.service;

import com.safelanes.service.dto.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class GeocodingService {
    @Value("${google.geocode.key}")
    private String API_KEY ;
    @Value("${google.geocode.url}")
    private String GEOCODE_URL;
    public Coordinate geocode(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(GEOCODE_URL, address.replace(" ", "+"), API_KEY);
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        JSONObject location = json.getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        String latStr = String.format("%.7f", lat);
        String lngStr = String.format("%.7f", lng);

        return new Coordinate(latStr, lngStr);
    }
}