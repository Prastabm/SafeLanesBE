package com.safelanes.service.service;

import com.safelanes.service.dto.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {
    public static List<Coordinate> decode(String polyline) {
        List<Coordinate> coordinates = new ArrayList<>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            coordinates.add(new Coordinate(lat / 1E5, lng / 1E5));
        }
        return coordinates;
    }

    // New method: decode and interpolate points every stepMeters
    public static List<Coordinate> decodeWithInterpolation(String polyline, double stepMeters) {
        List<Coordinate> decoded = decode(polyline);
        List<Coordinate> result = new ArrayList<>();
        if (decoded.isEmpty()) return result;

        for (int i = 0; i < decoded.size() - 1; i++) {
            Coordinate start = decoded.get(i);
            Coordinate end = decoded.get(i + 1);
            result.add(start);

            double distance = haversine(start.getLat(), start.getLon(), end.getLat(), end.getLon());
            int steps = (int) (distance / stepMeters);

            for (int s = 1; s < steps; s++) {
                double fraction = (double) s / steps;
                double lat = start.getLat() + (end.getLat() - start.getLat()) * fraction;
                double lon = start.getLon() + (end.getLon() - start.getLon()) * fraction;
                result.add(new Coordinate(lat, lon));
            }
        }
        result.add(decoded.get(decoded.size() - 1));
        return result;
    }

    // Haversine formula in meters
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}