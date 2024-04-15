package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.repository.PlanetRepository;
import co.edu.javeriana.spacetrader.repository.PlanetaryStockRepository;
import co.edu.javeriana.spacetrader.repository.ProductRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradingService {

    @Autowired
    private PlanetaryStockRepository planetaryStockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private PlanetRepository planetRepository;

    public List<PlanetaryStock> listPlanetaryStock (Long planetId){
        Planet planet = planetRepository.findById(planetId)
                .orElseThrow(() -> new RuntimeException("Planet not found for this id :: " + planetId));

        List<PlanetaryStock> stocks = planet.getProductsAvailable();

        for (PlanetaryStock stock : stocks) {
            System.out.println("Product: " + stock.getProduct().getName() +
                    ", Buying Price: " + stock.getBuyingPrice() +
                    ", Selling Price: " + stock.getSellingPrice() +
                    ", Stock Available: " + stock.getStock());
        }

        return stocks;
    }

    // Transactional method for a spaceship buying a product
    @Transactional
    public void buyProduct(Long spaceshipId, Long planetaryStockId, int quantity) {
        Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
                .orElseThrow(() -> new RuntimeException("Spaceship not found for this id :: " + spaceshipId));

        PlanetaryStock planetaryStock = planetaryStockRepository.findById(planetaryStockId)
                .orElseThrow(() -> new RuntimeException("PlanetaryStock not found for this id :: " + planetaryStockId));

        double price = planetaryStock.getBuyingPrice();
        BigDecimal totalCost = BigDecimal.valueOf(price * quantity);

        // Check if the spaceship has enough credits
        if (spaceship.getCredit().compareTo(totalCost) < 0) {
            throw new RuntimeException("Spaceship does not have enough credits.");
        }

        // Update the spaceship's credits and stock quantity
        spaceship.setCredit(spaceship.getCredit().subtract(totalCost));
        planetaryStock.setStock(planetaryStock.getStock() - quantity);

        // Persist the changes
        spaceshipRepository.save(spaceship);
        planetaryStockRepository.save(planetaryStock);
    }

    // Transactional method for a spaceship selling a product
    @Transactional
    public void sellProduct(Long spaceshipId, Long planetaryStockId, int quantity) {
        Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
                .orElseThrow(() -> new RuntimeException("Spaceship not found for this id :: " + spaceshipId));

        PlanetaryStock planetaryStock = planetaryStockRepository.findById(planetaryStockId)
                .orElseThrow(() -> new RuntimeException("PlanetaryStock not found for this id :: " + planetaryStockId));

        double price = planetaryStock.getSellingPrice();
        BigDecimal totalRevenue = BigDecimal.valueOf(price * quantity);

        // Check if the planetary stock can accommodate the sold quantity
        if (planetaryStock.getStock() + quantity < 0) {
            throw new RuntimeException("Insufficient stock available for sale.");
        }

        // Update the spaceship's credits and stock quantity
        spaceship.setCredit(spaceship.getCredit().add(totalRevenue));
        planetaryStock.setStock(planetaryStock.getStock() + quantity);

        // Persist the changes
        spaceshipRepository.save(spaceship);
        planetaryStockRepository.save(planetaryStock);
    }
}
