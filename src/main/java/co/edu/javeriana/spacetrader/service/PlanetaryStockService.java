package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.repository.PlanetaryStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanetaryStockService {

    @Autowired
    private PlanetaryStockRepository planetaryStockRepository;

    // Retrieve all planetary stocks
    public List<PlanetaryStock> findAllPlanetaryStocks() {
        return planetaryStockRepository.findAll();
    }

    // Find a planetary stock by ID
    public PlanetaryStock findPlanetaryStockById(Long id) {
        return planetaryStockRepository.findById(id).orElseThrow(() -> new RuntimeException("PlanetaryStock not found for this id :: " + id));
    }

    // Save or update a planetary stock
    public PlanetaryStock saveOrUpdatePlanetaryStock(PlanetaryStock planetaryStock) {
        return planetaryStockRepository.save(planetaryStock);
    }

    // Delete a planetary stock by ID
    @Transactional
    public void deletePlanetaryStock(Long id) {
        PlanetaryStock planetaryStock = findPlanetaryStockById(id); // Ensures the stock exists before attempting to delete
        planetaryStockRepository.delete(planetaryStock);
    }

    // Calculate selling price for a planetary stock
    public double calculateSellingPrice(Long id) {
        PlanetaryStock stock = findPlanetaryStockById(id);
        return stock.getSellingPrice();
    }

    // Calculate buying price for a planetary stock
    public double calculateBuyingPrice(Long id) {
        PlanetaryStock stock = findPlanetaryStockById(id);
        return stock.getBuyingPrice();
    }
}
