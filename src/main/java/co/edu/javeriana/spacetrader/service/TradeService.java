package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.exception.InsufficientCreditsException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TradeService {

    private static final Logger logger = Logger.getLogger(TradeService.class.getName());

    @Autowired
    private PlanetaryStockRepository planetaryStockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private PlanetRepository planetRepository;

    public List<PlanetaryStock> listPlanetaryStock(Long planetId) {
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


    @Transactional
    public void buyProduct(Long spaceshipId, Long planetaryStockId, int quantity) {
        try {
            Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
                    .orElseThrow(() -> new RuntimeException("Spaceship not found for this id :: " + spaceshipId));

            PlanetaryStock planetaryStock = planetaryStockRepository.findById(planetaryStockId)
                    .orElseThrow(() -> new RuntimeException("PlanetaryStock not found for this id :: " + planetaryStockId));

            double price = planetaryStock.getBuyingPrice();
            BigDecimal totalCost = BigDecimal.valueOf(price * quantity);

            if (spaceship.getCredit().compareTo(totalCost) < 0) {
                throw new InsufficientCreditsException("Spaceship does not have enough credits.");
            }

            spaceship.setCredit(spaceship.getCredit().subtract(totalCost));
            planetaryStock.setStock(planetaryStock.getStock() - quantity);

            spaceshipRepository.save(spaceship);
            planetaryStockRepository.save(planetaryStock);

            logger.info("Purchase successful: Spaceship " + spaceshipId + " bought " + quantity + " units of stock " + planetaryStockId);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during purchase", e);
            throw e;
        }
    }

    @Transactional
    public void sellProduct(Long spaceshipId, Long planetaryStockId, int quantity) {
        try {
            Spaceship spaceship = spaceshipRepository.findById(spaceshipId)
                    .orElseThrow(() -> new RuntimeException("Spaceship not found for this id :: " + spaceshipId));

            PlanetaryStock planetaryStock = planetaryStockRepository.findById(planetaryStockId)
                    .orElseThrow(() -> new RuntimeException("PlanetaryStock not found for this id :: " + planetaryStockId));

            double price = planetaryStock.getSellingPrice();
            BigDecimal totalRevenue = BigDecimal.valueOf(price * quantity);

            if (planetaryStock.getStock() + quantity < 0) {
                throw new RuntimeException("Insufficient stock available for sale.");
            }

            spaceship.setCredit(spaceship.getCredit().add(totalRevenue));
            planetaryStock.setStock(planetaryStock.getStock() + quantity);

            spaceshipRepository.save(spaceship);
            planetaryStockRepository.save(planetaryStock);

            logger.info("Sale successful: Spaceship " + spaceshipId + " sold " + quantity + " units of stock " + planetaryStockId);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during sale", e);
            throw e;
        }
    }
}
