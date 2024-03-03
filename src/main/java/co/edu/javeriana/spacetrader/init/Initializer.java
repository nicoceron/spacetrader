package co.edu.javeriana.spacetrader.init;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import co.edu.javeriana.spacetrader.service.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private StarService starService;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private ProductService productService;

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        generateStarsAndPlanets();
        List<Spaceship> spaceships = createAndSaveSpaceships(10);
        distributePlayersAmongSpaceships(100, spaceships);
        createProductSpecifications(500);
        createSpaceshipTypes(20);
    }

    private void generateStarsAndPlanets() {
        Random random = new Random();
        List<Star> stars = new ArrayList<>();
        for (int i = 0; i < 40000; i++) {
            Star star = new Star("Star_" + i, random.nextDouble() * 1000, random.nextDouble() * 1000, random.nextDouble() * 1000, false);
            if (random.nextDouble() <= 0.01) { // ~1% chance to have planets
                int planetsCount = random.nextInt(3) + 1; // 1 to 3 planets
                star.setInhabited(true);
                for (int j = 0; j < planetsCount; j++) {
                    Planet planet = new Planet("Planet_" + i + "_" + j, star);
                    star.getPlanets().add(planet);
                }
            }
            stars.add(star);
        }
        starRepository.saveAll(stars);
    }

    private List<Spaceship> createAndSaveSpaceships(int count) {
        List<Spaceship> spaceships = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Spaceship spaceship = new Spaceship();
            // Set the name to a unique value; adjust as necessary
            spaceship.setName("Spaceship " + (i + 1));
            // Ensure other required fields are set as well

            try {
                spaceship = spaceshipService.saveOrUpdateSpaceship(spaceship);
                spaceships.add(spaceship);
            } catch (ConstraintViolationException e) {
                // This is just a placeholder for exception handling
                System.err.println("Failed to save spaceship due to constraint violations: " + e.getMessage());
            }
        }
        return spaceships;
    }


    private void distributePlayersAmongSpaceships(int playersCount, List<Spaceship> spaceships) {
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player();
            player = playerService.saveOrUpdatePlayer(player);
            Spaceship assignedSpaceship = spaceships.get(i % spaceships.size());
            spaceshipService.assignPlayerToSpaceship(player.getId(), assignedSpaceship.getId());
        }
    }

    private void createProductSpecifications(int count) {
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            productService.saveOrUpdateProduct(product);
        }
    }

    private void createSpaceshipTypes(int count) {
        for (int i = 0; i < count; i++) {
            Spaceship spaceship = new Spaceship();
            spaceshipService.saveOrUpdateSpaceship(spaceship);
        }
    }
}


