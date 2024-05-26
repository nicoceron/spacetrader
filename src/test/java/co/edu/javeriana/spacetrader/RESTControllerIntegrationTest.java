package co.edu.javeriana.spacetrader;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlayerRepository playerRepository;


    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private PlanetRepository planetRepository;

    private Long testPlayerId;
    private Long testSpaceshipId;
    private Long testStarId;
    private Long closestStarId;
    private Long testPlanetId;


    @BeforeEach
    void init() {
        // Create and save a Model
        Model model = new Model("Test Model", 5000, 100);
        modelRepository.save(model);

        // Create and save the closest star
        Star closestStar = new Star("Closest Star", 0.1, 0.1, 0.1, true);
        starRepository.save(closestStar);
        closestStarId = closestStar.getId();
        System.out.println("Closest Star ID: " + closestStarId);

        // Create and save a test star
        Star star = new Star("Test Star", 0.0, 0.0, 0.0, true);
        starRepository.save(star);
        testStarId = star.getId();
        System.out.println("Test Star ID: " + testStarId);

        // Create and save a Planet
        Planet planet = new Planet("Test Planet", star);
        planetRepository.save(planet);
        testPlanetId = planet.getId();
        System.out.println("Test Planet ID: " + testPlanetId);

        // Create and save a Spaceship
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Test Spaceship");
        spaceship.setModel(model);
        spaceship.setCredit(BigDecimal.valueOf(10000));
        spaceship.setCurrentStar(star);
        spaceship.setCurrentPlanet(planet);
        spaceshipRepository.save(spaceship);
        testSpaceshipId = spaceship.getId();
        System.out.println("Test Spaceship ID: " + testSpaceshipId);

        // Create and save Player
        Player player = new Player("Test Player", "password123", "Pilot");
        playerRepository.save(player);
        testPlayerId = player.getId();
        System.out.println("Test Player ID: " + testPlayerId);
    }

    @Test
    void getClosestStars() {
        // Make the API call to get the closest stars
        ResponseEntity<Star[]> response = restTemplate.getForEntity("/api/navigation/closest-stars/" + testSpaceshipId, Star[].class);

        // Assertions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        // Ensure that the closest star is the first in the list
        Star[] stars = response.getBody();
        System.out.println("Retrieved Star ID: " + stars[1].getId());
        Assertions.assertEquals(closestStarId, stars[1].getId());
    }


    @Test
    void travelToStar() {
        // Assuming the travel time to the closest star has been pre-calculated and expected to be 10 days
        double expectedTravelTime = 10;

        // Make the API call to travel to the closest star
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/navigation/travel-to-star/" + testSpaceshipId + "/" + closestStarId, null, Map.class);

        // Assertions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        // Ensure the response contains the correct travel time message
        String expectedMessage = "Travel time: " + expectedTravelTime + " days";
        Assertions.assertEquals(expectedMessage, response.getBody().get("message"));
    }


    @Test
    void createPlayer() {
        Player newPlayer = new Player("New Player", "newpassword", "Trader");
        ResponseEntity<Player> response = restTemplate.postForEntity("/api/players", newPlayer, Player.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("New Player", response.getBody().getName());
    }

    @Test
    void deletePlayer() {
        ResponseEntity<Void> response = restTemplate.exchange("/api/players/" + testPlayerId, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(playerRepository.findById(testPlayerId).isPresent());
    }

    @Test
    public void modifyName() {
        String newName = "Updated Player Name";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(newName, headers);

        ResponseEntity<Map> response = restTemplate.exchange("/api/players/" + testPlayerId + "/name", HttpMethod.PATCH, entity, Map.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().get("quantityOfModifiedRows"));

        Player updatedPlayer = playerRepository.findById(testPlayerId).orElse(null);
        Assertions.assertNotNull(updatedPlayer);
        Assertions.assertEquals(newName, updatedPlayer.getName());
    }
}
