package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.model.Wormhole;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NavigationService {

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    // Assuming TravelService is the service that contains the findShortestPath method
    @Autowired
    private TravelService travelService;


    // Method to handle space travel to the selected star using shortest path
    public double travelToStar(Spaceship spaceship, Star destination) {
        Star currentStar = spaceship.getCurrentStar();
        if (currentStar.equals(destination)) {
            return 0; // No travel needed if already at the destination
        }

        // Find the shortest path using a pathfinding algorithm
        List<Star> path = travelService.findShortestPath(currentStar, destination);
        if (path.isEmpty()) {
            throw new RuntimeException("No route available to the destination star.");
        }

        // Calculate total travel time for the path
        double totalTravelTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Star start = path.get(i);
            Star end = path.get(i + 1);
            Wormhole wormhole = findWormholeBetween(start, end);
            if (wormhole == null) {
                throw new RuntimeException("Wormhole missing between " + start.getName() + " and " + end.getName());
            }
            totalTravelTime += wormhole.getTravelTime();
        }

        // Update spaceship's current star to the destination
        spaceship.setCurrentStar(destination);
        spaceshipRepository.save(spaceship);
        return totalTravelTime;
    }

    private Wormhole findWormholeBetween(Star start, Star end) {
        for (Wormhole wormhole : start.getOutgoingWormholes()) {
            if (wormhole.getDestinationStar().equals(end)) {
                return wormhole;
            }
        }
        return null;
    }

    public void travelToPlanet(Spaceship spaceship, Planet destinationPlanet) {
        Star currentStar = spaceship.getCurrentStar();

        // Check if the spaceship is in the same star system as the destination planet
        if (currentStar == null || !currentStar.equals(destinationPlanet.getStar())) {
            throw new RuntimeException("Spaceship is not at the star of the destination planet.");
        }

        // Since travel within a star system is rapid, set the spaceship's current planet without a travel time
        spaceship.setCurrentPlanet(destinationPlanet);
        spaceshipRepository.save(spaceship);
    }
}
