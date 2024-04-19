package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.service.SpaceshipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spaceship")
public class SpaceshipController {
    @Autowired
    private SpaceshipService spaceshipService;

    @GetMapping("/api/spaceships/{spaceshipId}")
    public ResponseEntity<Spaceship> getSpaceshipById(@PathVariable Long spaceshipId) {
        Spaceship spaceship = spaceshipService.findSpaceshipById(spaceshipId);
        if (spaceship != null) {
            return ResponseEntity.ok(spaceship);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
