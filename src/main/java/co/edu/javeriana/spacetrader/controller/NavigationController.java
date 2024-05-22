package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.service.NavigationService;
import co.edu.javeriana.spacetrader.service.PlanetService;
import co.edu.javeriana.spacetrader.service.SpaceshipService;
import co.edu.javeriana.spacetrader.service.StarService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/navigation")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private StarService starService;

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private PlanetService planetService;

    // Endpoint to list the closest 10 stars to the spaceship's current location
    @GetMapping("/closest-stars/{spaceshipId}")
    public ResponseEntity<List<Star>> getClosestStars(@PathVariable Long spaceshipId) {
        Spaceship spaceship = spaceshipService.findSpaceshipById(spaceshipId);
        if (spaceship == null) {
            // Handle the case where the spaceship is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Star> closestStars = starService.findClosestStars(spaceship.getCurrentStar(), 10);
        return ResponseEntity.ok(closestStars);
    }


    // Endpoint to handle the travel to the selected star and show travel time
    @PostMapping("/travel-to-star/{spaceshipId}/{starId}")
    public ResponseEntity<Map<String, Object>> travelToStar(@PathVariable Long spaceshipId, @PathVariable Long starId) {
        try {
            Spaceship spaceship = spaceshipService.findSpaceshipById(spaceshipId);
            Star destinationStar = starService.findStarById(starId);
            double travelTime = navigationService.travelToStar(spaceship, destinationStar);

            Map<String, Object> body = new HashMap<>();
            body.put("message", "Travel time: " + travelTime + " days");
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    // Endpoint to show planets in the arrived star
    @GetMapping("/planets/{starId}")
    public ResponseEntity<?> getPlanetsInStar(@PathVariable Long starId) {
        try {
            Star star = starService.findStarById(starId);
            List<Planet> planets = star.getPlanets();
            return ResponseEntity.ok(planets);
        } catch (EntityNotFoundException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Star not found with id: " + starId);
            // Convert the builder to ResponseEntity and set the body
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
    }

    // Endpoint to travel to the selected planet
    @PostMapping("/travel-to-planet/{spaceshipId}/{planetId}")
    public ResponseEntity<Map<String, Object>> travelToPlanet(@PathVariable Long spaceshipId, @PathVariable Long planetId) {
        Map<String, Object> body = new HashMap<>();
        try {
            Spaceship spaceship = spaceshipService.findSpaceshipById(spaceshipId);
            Planet destinationPlanet = planetService.findPlanetById(planetId);

            if (!destinationPlanet.getStar().isInhabited()) {
                throw new RuntimeException("The destination planet is not habitable.");
            }

            navigationService.travelToPlanet(spaceship, destinationPlanet);
            body.put("message", "Spaceship " + spaceship.getName() + " has landed on planet " + destinationPlanet.getName());
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            body.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
