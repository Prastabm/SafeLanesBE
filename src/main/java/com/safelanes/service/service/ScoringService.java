package com.safelanes.service.service;

import com.safelanes.service.dto.PathResponse;
import com.safelanes.service.dto.ScoredCoordinate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class ScoringService {
    private static final String SCORING_URL = "http://host.docker.internal:8000/api/v1/score";

    public List<List<ScoredCoordinate>> scorePaths(List<PathResponse> paths) {
        System.out.println(paths);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<PathResponse>> request = new HttpEntity<>(paths, headers);

        ResponseEntity<ScoredCoordinate[][]> response = restTemplate.exchange(
                SCORING_URL,
                HttpMethod.POST,
                request,
                ScoredCoordinate[][].class
        );

        ScoredCoordinate[][] scoredArray = response.getBody();
        List<List<ScoredCoordinate>> result = new ArrayList<>();
        if (scoredArray != null) {
            for (ScoredCoordinate[] arr : scoredArray) {
                result.add(Arrays.asList(arr));
            }
        }
        return result;
    }
}