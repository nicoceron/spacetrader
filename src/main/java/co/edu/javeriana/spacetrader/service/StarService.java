package co.edu.javeriana.spacetrader.service;
import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StarService {
    @Autowired
    private StarRepository starRepository;
    @Autowired
    private PlanetService planetService;


    // Retrieve a list of all stars
    public List<Star> findAllStars() {
        return starRepository.findAll();
    }

    // Find a star by its ID
    public Star findStarById(Long id) {
        return starRepository.findById(id).orElseThrow(() -> new RuntimeException("Star not found for this id :: " + id));
    }

    // Save or update a star
    public Star saveOrUpdateStar(Star star) {
        return starRepository.save(star);
    }

    // Delete a star by its ID
    @Transactional
    public void deleteStar(Long id) {
        Star star = starRepository.findById(id).orElseThrow(() -> new RuntimeException("Star not found for this id :: " + id));
        starRepository.delete(star);
    }

    @Transactional
    public void addPlanetToStar(Long starId, Planet planet) {
        Star star = starRepository.findById(starId)
                .orElseThrow(() -> new EntityNotFoundException("Star not found with id: " + starId));

        // Associate planet with star
        planet.setStar(star);
        star.addPlanet(planet);

        // Set inhabited status to true if the star has at least one planet
        if (!star.isInhabited() && !star.getPlanets().isEmpty()) {
            star.setInhabited(true);
        }

        // Save all
        planetService.saveOrUpdatePlanet(planet);
        starRepository.save(star);
    }

    public List<Star> findClosestStars(Star currentStar, int limit) {
        List<Star> allStars = starRepository.findAll();
        return allStars.stream()
                .filter(Star::isInhabited) // Filter to include only inhabited stars
                .sorted(Comparator.comparingDouble(s -> calculateDistance(currentStar, s)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double calculateDistance(Star star1, Star star2) {
        return Math.sqrt(Math.pow(star2.getX() - star1.getX(), 2)
                + Math.pow(star2.getY() - star1.getY(), 2)
                + Math.pow(star2.getZ() - star1.getZ(), 2));
    }



}
