package co.edu.javeriana.spacetrader.init;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import co.edu.javeriana.spacetrader.service.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

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

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        generateStarsAndPlanets(40000);
        createSpaceshipModels();
        List<Spaceship> spaceships = createAndSaveSpaceships(10);
        distributePlayersAmongSpaceships(100, spaceships);
        createProductSpecifications(500);
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

    private void generateStarsAndPlanets(int count) {
        Random random = new Random();
        List<Star> stars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Star star = new Star("Star_" + i, random.nextDouble() * 1000, random.nextDouble() * 1000, random.nextDouble() * 1000, false);
            if (random.nextDouble() <= 0.01) { // ~1% chance to have planets
                int planetsCount = random.nextInt(3) + 1; // 1 to 3 planets
                star.setInhabited(true);
                for (int j = 0; j < planetsCount; j++) {
                    Planet planet = new Planet("Planet_" + i + "_" + j, star);
                    star.addPlanet(planet);
                    star.setInhabited(true);
                }
            }
            stars.add(star);
        }
        starRepository.saveAll(stars);
    }

    public List<Spaceship> createAndSaveSpaceships(int count) {
        // Fetch all models from the database
        List<Model> allModels = modelService.findAllModels();
        if (allModels.isEmpty()) {
            throw new IllegalStateException("No models available to assign to spaceships.");
        }

        List<Spaceship> spaceships = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Spaceship spaceship = new Spaceship();
            spaceship.setName("Spaceship " + (i + 1));
            spaceship.setCredit(BigDecimal.valueOf(Math.random() * 10000));
            // Assign a random model from the list
            int randomModelIndex = (int) (Math.random() * allModels.size());
            Model randomModel = allModels.get(randomModelIndex);
            spaceship.setModel(randomModel);
            try {
                spaceship = spaceshipService.saveOrUpdateSpaceship(spaceship);
                spaceships.add(spaceship);
            } catch (ConstraintViolationException e) {
                System.err.println("Failed to save spaceship due to constraint violations: " + e.getMessage());
            }
        }
        return spaceships;
    }

    public void distributePlayersAmongSpaceships(int playersCount, List<Spaceship> spaceships) {
        String[] roles = {"Pilot", "Trader", "Captain"};
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player();
            player.setUser("Player" + i + 1);
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


}


