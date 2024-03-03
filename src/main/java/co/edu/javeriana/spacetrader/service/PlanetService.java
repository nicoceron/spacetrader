package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;
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
    public Planet saveOrUpdatePlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    // Delete a planet by ID
    @Transactional
    public void deletePlanet(Long id) {
        Planet planet = findPlanetById(id); // Ensures the planet exists before attempting to delete
        planetRepository.delete(planet);
    }

//    @jakarta.transaction.Transactional
    @Transactional
    public void addPlanetaryStockToPlanet(Long planetId, PlanetaryStock planetaryStock) {
        Planet planet = findPlanetById(planetId);

        //Set the planet of the planetaryStock
        planetaryStock.setPlanet(planet);

        // Add planetaryStock to the planet's list of stocks
        planet.getProductsAvailable().add(planetaryStock);

        // Save the planet
        planetRepository.save(planet);

        planetaryStockService.saveOrUpdatePlanetaryStock(planetaryStock);
    }

}
