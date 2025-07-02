package com.safelanes.service.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import com.safelanes.service.dto.Coordinate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {

    /**
     * Decodes polyline using Google's polylineencoder library.
     */
    public static List<Coordinate> decode(String polyline) {
        List<LatLng> decodedPoints = PolylineEncoding.decode(polyline);
        List<Coordinate> coordinates = new ArrayList<>();

        for (LatLng point : decodedPoints) {
            String latStr = new BigDecimal(point.lat).setScale(7, RoundingMode.HALF_UP).toPlainString();
            String lonStr = new BigDecimal(point.lng).setScale(7, RoundingMode.HALF_UP).toPlainString();
            coordinates.add(new Coordinate(latStr, lonStr));
        }

        return coordinates;
    }

    /**
     * Decodes polyline and adds interpolated points every `stepMeters`.
     */
    public static List<Coordinate> decodeWithInterpolation(String polyline, double stepMeters) {
        List<Coordinate> originalPoints = decode(polyline);
        List<Coordinate> interpolated = new ArrayList<>();

        for (int i = 0; i < originalPoints.size() - 1; i++) {
            Coordinate start = originalPoints.get(i);
            Coordinate end = originalPoints.get(i + 1);

            double lat1 = Double.parseDouble(start.getLat());
            double lon1 = Double.parseDouble(start.getLon());
            double lat2 = Double.parseDouble(end.getLat());
            double lon2 = Double.parseDouble(end.getLon());

            interpolated.add(start); // Always include the start point

            double distance = haversine(lat1, lon1, lat2, lon2);
            int steps = (int) Math.floor(distance / stepMeters);

            for (int step = 1; step < steps; step++) {
                double fraction = (double) step / steps;
                double interpLat = lat1 + (lat2 - lat1) * fraction;
                double interpLon = lon1 + (lon2 - lon1) * fraction;

                String latStr = new BigDecimal(interpLat).setScale(7, RoundingMode.HALF_UP).toPlainString();
                String lonStr = new BigDecimal(interpLon).setScale(7, RoundingMode.HALF_UP).toPlainString();

                interpolated.add(new Coordinate(latStr, lonStr));
            }
        }

        if (!originalPoints.isEmpty()) {
            interpolated.add(originalPoints.get(originalPoints.size() - 1)); // add the last point
        }

        return interpolated;
    }

    /**
     * Computes Haversine distance in meters between two lat/lon points.
     */
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
