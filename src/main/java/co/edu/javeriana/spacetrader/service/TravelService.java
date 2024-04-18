package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.model.Wormhole;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import co.edu.javeriana.spacetrader.repository.WormholeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TravelService {

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private WormholeRepository wormholeRepository;

    // Find the shortest path using Dijkstra's Algorithm
    public List<Star> findShortestPath(Star startStar, Star endStar) {
        Map<Star, Star> previousStars = new HashMap<>();
        Map<Star, Double> shortestTimes = new HashMap<>();
        PriorityQueue<Star> priorityQueue = new PriorityQueue<>(Comparator.comparing(shortestTimes::get));

        shortestTimes.put(startStar, 0.0);
        priorityQueue.add(startStar);

        Set<Star> visitedStars = new HashSet<>();

        while (!priorityQueue.isEmpty()) {
            Star currentStar = priorityQueue.poll();

            if (visitedStars.contains(currentStar)) {
                continue;
            }

            visitedStars.add(currentStar);

            if (currentStar.equals(endStar)) {
                break;
            }

            // Explore each connected wormhole
            for (Wormhole wormhole : currentStar.getOutgoingWormholes()) {
                Star adjacentStar = wormhole.getDestinationStar();
                double weight = wormhole.getTravelTime();

                double totalTravelTime = shortestTimes.getOrDefault(currentStar, Double.MAX_VALUE) + weight;

                if (totalTravelTime < shortestTimes.getOrDefault(adjacentStar, Double.MAX_VALUE)) {
                    shortestTimes.put(adjacentStar, totalTravelTime);
                    previousStars.put(adjacentStar, currentStar);
                    priorityQueue.add(adjacentStar);
                }
            }
        }

        return reconstructPath(previousStars, startStar, endStar);
    }

    private List<Star> reconstructPath(Map<Star, Star> previousStars, Star startStar, Star endStar) {
        LinkedList<Star> path = new LinkedList<>();
        for (Star at = endStar; at != null; at = previousStars.get(at)) {
            path.addFirst(at);
        }

        if (path.getFirst().equals(startStar)) {
            return path;
        }

        return Collections.emptyList(); // No path found
    }
}
