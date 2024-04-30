package co.edu.javeriana.spacetrader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TradeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private PlanetaryStockRepository planetaryStockRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long planetId;
    private Long spaceshipId;
    private Long stockId;

    @BeforeEach
    void setUp() {
        Star star = starRepository.save(new Star("Gamma", 0.0, 0.0, 0.0, true));
        Planet planet = new Planet("Gamma I", star);
        planet = planetRepository.save(planet);
        planetId = planet.getId();

        Product product = new Product("Hyperfuel", 2.0);
        product = productRepository.save(product);

        PlanetaryStock stock = new PlanetaryStock(planet, product, 100L, 1.5, 0.5);
        stock = planetaryStockRepository.save(stock);
        stockId = stock.getId();

        Spaceship spaceship = new Spaceship("Galactic Voyager", new Model(), new BigDecimal("15000"), star, planet);
        spaceship = spaceshipRepository.save(spaceship);
        spaceshipId = spaceship.getId();
    }

    @Test
    void listPlanetaryStockTest() {
        ResponseEntity<List> response = rest.getForEntity("/api/trade/planetary-stock/" + planetId, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void buyProductTest() {
        TransactionRequest transaction = new TransactionRequest(spaceshipId, stockId, 10);
        ResponseEntity<Map> response = rest.postForEntity("/api/trade/buy", transaction, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Purchase successful.", response.getBody().get("message"));
    }

    @Test
    void sellProductTest() {
        TransactionRequest transaction = new TransactionRequest(spaceshipId, stockId, 5);
        ResponseEntity<Map> response = rest.postForEntity("/api/trade/sell", transaction, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sale successful.", response.getBody().get("message"));
    }
}
