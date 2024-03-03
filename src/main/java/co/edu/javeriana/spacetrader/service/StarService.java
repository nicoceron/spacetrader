package co.edu.javeriana.spacetrader.service;
import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // Save the planet
        planetService.saveOrUpdatePlanet(planet);
    }



}
