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

    // Find the shortest path from one star to another using Dijkstra's algorithm
    public List<Wormhole> findShortestPath(Star startStar, Star endStar) {
        // A map of stars to their corresponding shortest path wormhole
        Map<Star, Wormhole> shortestPathTree = new HashMap<>();
        // A map of stars to the weight (distance) of the shortest path to them
        Map<Star, Double> shortestDistances = new HashMap<>();
        // Priority queue to select the star with the shortest path during each iteration
        PriorityQueue<Star> queue = new PriorityQueue<>(Comparator.comparingDouble(shortestDistances::get));

        // Initialize distances and queue
        shortestDistances.put(startStar, 0.0); // Start node is at distance 0 from itself
        for (Star star : starRepository.findAll()) {
            if (!star.equals(startStar)) {
                shortestDistances.put(star, Double.POSITIVE_INFINITY); // Unknown distance from start to each node
            }
            queue.add(star);
        }

        while (!queue.isEmpty()) {
            Star currentStar = queue.poll(); // Star with the shortest distance (first iteration will return startStar)

            // If we reached the destination star, we can break early
            if (currentStar.equals(endStar)) {
                break;
            }

            // Visit each 'neighbor' of the current star
            for (Wormhole wormhole : wormholeRepository.findBySourceStar(currentStar)) {
                Star adjacentStar = wormhole.getDestinationStar();
                double edgeWeight = wormhole.getTravelTime(); // This would be the 'distance' for this edge
                double distanceThroughU = shortestDistances.get(currentStar) + edgeWeight;

                // If shorter path from start to neighbor found, update the distance and add to the queue
                if (distanceThroughU < shortestDistances.get(adjacentStar)) {
                    shortestDistances.put(adjacentStar, distanceThroughU);
                    shortestPathTree.put(adjacentStar, wormhole);

                    // Reorder the queue based on the new shortest distance found
                    queue.remove(adjacentStar);
                    queue.add(adjacentStar);
                }
            }
            for (Star star : starRepository.findAll()) {
                if (!currentStar.equals(star) && !hasDirectWormhole(currentStar, star)) {
                    double distance = calculateRelativisticTravelTime(currentStar, star);
        }
            }
        }

        // Reconstruct the path from the shortest path tree
        List<Wormhole> path = new ArrayList<>();
        Star step = endStar;
        // Check if a path exists
        if (shortestPathTree.get(step) == null && !startStar.equals(endStar)) {
            throw new NoSuchElementException("No path found from start to end star");
        }
        // Construct the path
        while (shortestPathTree.get(step) != null) {
            path.add(shortestPathTree.get(step));
            step = shortestPathTree.get(step).getSourceStar();
        }
        Collections.reverse(path); // Reverse to get the correct order from start to end
        return path;
    }

    // Calculate the travel time for the shortest path (assuming 1 light-year per day)
    public double calculateTravelTime(Star startStar, Star endStar) {
        List<Wormhole> path = findShortestPath(startStar, endStar);
        return path.stream().mapToDouble(Wormhole::getTravelTime).sum();
    }

    // Method to simulate the travel of a spaceship through the wormhole network
    public void travelThroughWormhole(Star source, Star destination) {
        // Use the pathfinding method to get the wormhole path
        List<Wormhole> wormholePath = findShortestPath(source, destination);

        // Here you can update the state of the game to reflect the spaceship's journey
        // For example, updating the location of the spaceship, decrementing resources, etc.
    }

    private boolean hasDirectWormhole(Star source, Star destination) {
        return wormholeRepository.findBySourceStarAndDestinationStar(source, destination).isPresent();
    }

    private double calculateRelativisticTravelTime(Star source, Star destination) {
        double distance = calculateDistance(source, destination);
        double speedOfTravel = 1.0; // Speed as a fraction of light speed
        double inefficiencyFactor = 100; // Arbitrary large factor to represent the inefficiency
        return distance / speedOfTravel * inefficiencyFactor;
    }

    private double calculateDistance(Star source, Star destination) {
        return Math.sqrt(
                Math.pow(destination.getX() - source.getX(), 2) +
                        Math.pow(destination.getY() - source.getY(), 2) +
                        Math.pow(destination.getZ() - source.getZ(), 2)
        );
    }

}
