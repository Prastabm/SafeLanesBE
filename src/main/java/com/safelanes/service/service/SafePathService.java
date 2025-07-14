package com.safelanes.service.service;

import com.safelanes.service.dto.ScoredCoordinate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SafePathService {

    static class Node {
        double lat, lng, score;
        double totalScore = Double.NEGATIVE_INFINITY;
        Node parent;

        Node(double lat, double lng, double score) {
            this.lat = lat;
            this.lng = lng;
            this.score = score;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return Double.compare(lat, other.lat) == 0 && Double.compare(lng, other.lng) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lat, lng);
        }
    }

    private static double distance(Node a, Node b) {
        return Math.sqrt(Math.pow(a.lat - b.lat, 2) + Math.pow(a.lng - b.lng, 2));
    }

    private static List<Node> getNeighbors(Node current, List<Node> nodes) {
        List<Node> neighbors = new ArrayList<>();
        double threshold = 0.001;
        for (Node node : nodes) {
            double dist = distance(current, node);
            if (!node.equals(current) && dist <= threshold) {
                neighbors.add(node);
            }
        }
        return neighbors;
    }

    private List<Node> safestPath(Node start, Node end, List<Node> allNodes) {
        for (Node n : allNodes) {
            n.totalScore = Double.NEGATIVE_INFINITY;
            n.parent = null;
        }

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> -n.totalScore));
        Set<Node> processed = new HashSet<>();

        start.totalScore = start.score;
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (processed.contains(current)) continue;
            processed.add(current);

            if (current.equals(end)) {
                return reconstructPath(current);
            }

            List<Node> neighbors = getNeighbors(current, allNodes);

            for (Node neighbor : neighbors) {
                if (processed.contains(neighbor)) continue;

                double newScore = current.totalScore + neighbor.score;
                if (newScore > neighbor.totalScore) {
                    neighbor.totalScore = newScore;
                    neighbor.parent = current;
                    queue.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Node> reconstructPath(Node end) {
        List<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = at.parent) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private List<ScoredCoordinate> toScoredCoordinates(List<Node> nodes) {
        List<ScoredCoordinate> result = new ArrayList<>();
        for (Node n : nodes) {
            ScoredCoordinate sc = new ScoredCoordinate();
            sc.setLat(n.lat);
            sc.setLng(n.lng);
            sc.setScore(n.score);
            result.add(sc);
        }
        return result;
    }

    public List<ScoredCoordinate> findSafestPath(List<List<ScoredCoordinate>> scoredPaths) {
        if (scoredPaths == null || scoredPaths.isEmpty()) {
            return Collections.emptyList();
        }

        List<ScoredCoordinate> safest = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        int safestPathIndex = -1;

        for (int i = 0; i < scoredPaths.size(); i++) {
            List<ScoredCoordinate> path = scoredPaths.get(i);
            if (path.isEmpty()) continue;

            double total = 0;
            for (ScoredCoordinate sc : path) {
                total += sc.getScore();
            }
            double average = total / path.size();

            if (average > bestScore) {
                bestScore = average;
                safest = path;
                safestPathIndex = i;
            }
        }

        if (safestPathIndex != -1) {
            System.out.println("Path " + (safestPathIndex + 1) + " is the safest route with average score: " +
                    String.format("%.2f", bestScore));
        }

        return safest != null ? safest : Collections.emptyList();
    }

}