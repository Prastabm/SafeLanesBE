package com.safelanes.service.service;

import com.safelanes.service.dto.ScoredCoordinate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SafePathService {

    public static class Node {
        public double lat, lon, score;
        public double totalScore = Double.NEGATIVE_INFINITY;
        public Node parent;

        public Node(double lat, double lon, double score) {
            this.lat = lat;
            this.lon = lon;
            this.score = score;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return Double.compare(lat, other.lat) == 0 && Double.compare(lon, other.lon) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lat, lon);
        }
    }

    private static double distance(Node a, Node b) {
        return Math.sqrt(Math.pow(a.lat - b.lat, 2) + Math.pow(a.lon - b.lon, 2));
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

    public List<ScoredCoordinate> findSafestPath(List<List<ScoredCoordinate>> scoredNodes) {
        // Flatten the list of lists
        List<Node> allNodes = new ArrayList<>();
        for (List<ScoredCoordinate> path : scoredNodes) {
            for (ScoredCoordinate sc : path) {
                allNodes.add(new Node(sc.getLat(), sc.getLon(), sc.getScore()));
            }
        }
        System.out.println("All nodes:");
        for (Node n : allNodes) {
            System.out.printf("Node(lat=%.5f, lon=%.5f, score=%.2f)%n", n.lat, n.lon, n.score);
        }
        if (allNodes.isEmpty()) {
            System.out.println("No nodes found, returning empty path.");
            return Collections.emptyList();
        }
        Node start = allNodes.get(0);
        Node end = allNodes.get(allNodes.size() - 1);
        System.out.printf("Start node: (lat=%.5f, lon=%.5f)%n", start.lat, start.lon);
        System.out.printf("End node: (lat=%.5f, lon=%.5f)%n", end.lat, end.lon);


        // Pathfinding
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
            System.out.printf("Processing node: (lat=%.5f, lon=%.5f, totalScore=%.2f)%n", current.lat, current.lon, current.totalScore);
            if (current.equals(end)) {
                System.out.println("End node reached, reconstructing path.");
                return toScoredCoordinates(reconstructPath(current));
            }
            List<Node> neighbors = getNeighbors(current, allNodes);
            System.out.printf("Neighbors found for (lat=%.5f, lon=%.5f): %s%n", current.lat, current.lon,
                    neighbors.stream().map(n -> String.format("(%.5f,%.5f)", n.lat, n.lon)).collect(Collectors.toList()));
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
        System.out.println("No path found, returning empty list.");
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
            sc.setLon(n.lon);
            sc.setScore(n.score);
            result.add(sc);
        }
        return result;
    }
}