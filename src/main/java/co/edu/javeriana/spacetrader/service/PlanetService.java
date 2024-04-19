package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.repository.PlanetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private PlanetaryStockService planetaryStockService;

    // Retrieve all planets
    public List<Planet> findAllPlanets() {
        return planetRepository.findAll();
    }

    // Find a planet by ID
    public Planet findPlanetById(Long id) {
        return planetRepository.findById(id).orElseThrow(() -> new RuntimeException("Planet not found for this id :: " + id));
    }
    // Save or update a planet
    public void saveOrUpdatePlanet(Planet planet) {
        if (planet.getId() != 0) { // Check if id is not the default value
            Planet existingPlanet = findPlanetById(planet.getId());
            if (existingPlanet != null) {
                existingPlanet.setName(planet.getName());
                // If there are other fields that need to be copied from planet to existingPlanet, do so here
                planetRepository.save(existingPlanet);
            } else {
                throw new EntityNotFoundException("Planet with id " + planet.getId() + " not found.");
            }
        } else {
            // Handle the creation of a new Planet
            if (planet.getStar() == null) {
                throw new IllegalStateException("Star must be set for a new planet");
            }
            planetRepository.save(planet);
        }
    }


    // Delete a planet by ID
    @Transactional
    public void deletePlanet(Long id) {
        Planet planet = findPlanetById(id); // Ensures the planet exists before attempting to delete
        planetRepository.delete(planet);
    }


//    @jakarta.transaction.Transactional
//    @Transactional
//    public void addPlanetaryStockToPlanet(Long planetId, PlanetaryStock planetaryStock) {
//        Planet planet = findPlanetById(planetId);
//
//        //Set the planet of the planetaryStock
//        planetaryStock.setPlanet(planet);
//
//        // Add planetaryStock to the planet's list of stocks
//        planet.getProductsAvailable().add(planetaryStock);
//
//        // Save the planet
//        planetRepository.save(planet);
//        planetaryStockService.saveOrUpdatePlanetaryStock(planetaryStock);
//    }

}
