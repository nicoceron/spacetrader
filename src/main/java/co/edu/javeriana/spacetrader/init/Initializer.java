package co.edu.javeriana.spacetrader.init;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.PlanetaryStockRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import co.edu.javeriana.spacetrader.repository.WormholeRepository;
import co.edu.javeriana.spacetrader.service.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Configuration
@Profile("default")
@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private StarService starService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private ProductService productService;

    @Autowired
    StarRepository starRepository;

    @Autowired
    PlanetaryStockService planetaryStockService;

    @Autowired
    WormholeRepository wormholeRepository;

    @Autowired
    SpaceshipRepository spaceshipRepository;


    private Random random = new Random();

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        List<Star> stars = generateStarsAndPlanets(40000);
        List<Star> inhabitedStars = stars.stream().filter(Star::isInhabited).collect(Collectors.toList());
        initializeWormholes(inhabitedStars);
        createSpaceshipModels();
        List<Spaceship> spaceships = createAndSaveSpaceships(10, inhabitedStars);
        distributePlayersAmongSpaceships(100, spaceships);
        createProductSpecifications(500);
        initializePlanetaryStocksForPlanets();
    }


    private void createSpaceshipModels() {
        List<Model> models = new ArrayList<>();
        models.add(new Model("Overlord", 1.5, 3000.0));
        models.add(new Model("Mule", 2.0, 5000.0));
        models.add(new Model("Foundation", 2.2, 4500.0));
        models.add(new Model("Hail Mary", 3.0, 6000.0));
        models.add(new Model("Mothership", 2.8, 5500.0));
        models.add(new Model("Phoenix", 1.5, 500.0));
        models.add(new Model("Viking", 1.8, 200.0));
        models.add(new Model("Battlecruiser", 0.6, 10000.0));
        // Initialize the models in the database
        models.forEach(modelService::saveOrUpdateModel);
    }

    private List<Star> generateStarsAndPlanets(int count) {
        List<Star> stars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Star star = new Star("Star_" + i, random.nextDouble() * 1000, random.nextDouble() * 1000, random.nextDouble() * 1000, false);
            if (random.nextDouble() <= 0.01) {  // 1% chance to have planets
                star.setInhabited(true);
                int planetsCount = random.nextInt(3) + 1;  // 1 to 3 planets
                for (int j = 0; j < planetsCount; j++) {
                    Planet planet = new Planet("Planet_" + i + "_" + j, star);
                    star.addPlanet(planet);
                }
            }
            stars.add(star);
        }
        starRepository.saveAll(stars);
        return stars;  // Return the list of stars
    }


    public List<Spaceship> createAndSaveSpaceships(int count, List<Star> stars) {
        List<Model> allModels = modelService.findAllModels();
        if (allModels.isEmpty()) {
            throw new IllegalStateException("No models available to assign to spaceships.");
        }

        if (stars.stream().noneMatch(Star::isInhabited)) {
            throw new IllegalStateException("No inhabited stars available for spaceship initialization.");
        }

        List<Spaceship> spaceships = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Spaceship spaceship = new Spaceship();
            spaceship.setName("Spaceship " + (i + 1));
            spaceship.setCredit(BigDecimal.valueOf(random.nextDouble() * 1000000));

            // Choose a random model
            Model randomModel = allModels.get(random.nextInt(allModels.size()));
            spaceship.setModel(randomModel);

            // Assign a random inhabited star
            List<Star> inhabitedStars = stars.stream().filter(Star::isInhabited).collect(Collectors.toList());
            Star randomStar = inhabitedStars.get(random.nextInt(inhabitedStars.size()));
            spaceship.setCurrentStar(randomStar);

            // Assign a random planet from the selected star
            List<Planet> planets = randomStar.getPlanets();
            if (!planets.isEmpty()) {
                Planet randomPlanet = planets.get(random.nextInt(planets.size()));
                spaceship.setCurrentPlanet(randomPlanet);
            }

            try {
                // Save the spaceship entity
                spaceship = spaceshipRepository.save(spaceship);
                spaceships.add(spaceship);
            } catch (Exception e) {
                System.err.println("Failed to save spaceship due to: " + e.getMessage());
            }
        }
        return spaceships;
    }


    public void distributePlayersAmongSpaceships(int playersCount, List<Spaceship> spaceships) {
        String[] roles = {"Pilot", "Trader", "Captain"};
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player();
            player.setName("Player" + i + 1);
            player.setPassword("123");
            int randomRoleIndex = (int) (Math.random() * roles.length);
            player.setRole(roles[randomRoleIndex]);
            player = playerService.saveOrUpdatePlayer(player);
            Spaceship assignedSpaceship = spaceships.get(i % spaceships.size());
            spaceshipService.assignPlayerToSpaceship(player.getId(), assignedSpaceship.getId());
        }
    }

    private void createProductSpecifications(int count) {
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName("Product" + i + 1);
            product.setVolume(random.nextDouble());
            productService.saveOrUpdateProduct(product);
        }
    }

    private void initializePlanetaryStocksForPlanets() {
        Random random = new Random();
        List<Planet> allPlanets = planetService.findAllPlanets();
        List<Product> allProducts = productService.findAllProducts();
        List<PlanetaryStock> planetaryStocks = new ArrayList<>();

        for (Planet planet : allPlanets) {
            int numberOfStocks = random.nextInt(10) + 1; // Generate 1 to 10 stock items per planet

            for (int i = 0; i < numberOfStocks; i++) {
                Product randomProduct = allProducts.get(random.nextInt(allProducts.size())); // Select a random product
                PlanetaryStock stock = new PlanetaryStock();

                stock.setPlanet(planet);
                stock.setProduct(randomProduct);
                stock.setStock(random.nextInt(1001)); // Random stock between 0 and 1000
                stock.setDemandFactor(random.nextDouble() * 1000000); // Random demand factor between 0 and 1,000,000
                stock.setSupplyFactor(random.nextDouble() * 1000000); // Random supply factor between 0 and 1,000,000

                planetaryStocks.add(stock);
            }
        }

        for (PlanetaryStock stock: planetaryStocks){
            planetaryStockService.saveOrUpdatePlanetaryStock(stock);
        }
    }
    public void initializeWormholes(List<Star> inhabitedStars) {
        if (inhabitedStars.size() < 2) {
            throw new IllegalStateException("Not enough inhabited stars to form a network");
        }

        Collections.shuffle(inhabitedStars); // Shuffle to randomize connections
        // Create a simple loop to ensure all stars are connected at least in a ring
        for (int i = 0; i < inhabitedStars.size(); i++) {
            Star source = inhabitedStars.get(i);
            Star destination = inhabitedStars.get((i + 1) % inhabitedStars.size()); // Loop back to the first
            createAndSaveWormhole(source, destination);
        }
    }

    private void createAndSaveWormhole(Star source, Star destination) {
        Wormhole wormhole = new Wormhole(source, destination);
        wormholeRepository.save(wormhole);
    }

}





