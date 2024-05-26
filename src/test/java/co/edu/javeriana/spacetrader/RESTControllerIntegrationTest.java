package co.edu.javeriana.spacetrader;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

    @Autowired
    private WormholeRepository wormholeRepository;

    private Long testPlayerId;
    private Long testSpaceshipId;
    private Long testStarId;
    private Long closestStarId;
    private Long testPlanetId;

    /**
     * Initializes the test data before each test method is executed.
     */
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

        // Create and save a Wormhole between the test star and the closest star
        Wormhole wormhole = new Wormhole(star, closestStar);
        wormholeRepository.save(wormhole);
        System.out.println("Wormhole created between Test Star and Closest Star with travel time: " + wormhole.getTravelTime());
    }

    /**
     * Test for GET mapping to retrieve the closest stars to the current star of the spaceship.
     */
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

    /**
     * Test for POST mapping to initiate travel to the closest star.
     */
    @Test
    void travelToStar() {
        // Make the POST request to the endpoint
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/navigation/travel-to-star/" + testSpaceshipId + "/" + closestStarId, null, Map.class);

        System.out.println("Response: " + response);
        System.out.println("Response Body: " + response.getBody());

        // Expected response body
        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("message", "Travel time: 0.17320508075688776 days");

        // Perform assertions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");

        // Assert that the response body matches the expected values
        Assertions.assertEquals(expectedResponseBody, response.getBody(), "Response body should match the expected values");

        // Additional assertions for individual keys, if needed
        Assertions.assertTrue(response.getBody().containsKey("message"), "Response body should contain the key 'message'");
        Assertions.assertEquals("Travel time: 0.17320508075688776 days", response.getBody().get("message"), "The message should match the expected travel time");
    }

    /**
     * Test for DELETE mapping to delete a player.
     */
    @Test
    void deletePlayer() {
        // Make the DELETE request to the endpoint
        ResponseEntity<Void> response = restTemplate.exchange("/api/player/" + testPlayerId, HttpMethod.DELETE, null, Void.class);

        // Assert the status code
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");

        // Check if the player exists after deletion
        boolean playerExists = playerRepository.findById(testPlayerId).isPresent();
        System.out.println("Player exists after deletion: " + playerExists);

        // Assert that the player does not exist
        Assertions.assertFalse(playerExists, "Player should not exist after deletion");
    }

    /**
     * Test for PATCH mapping to modify the name of a player.
     */
    @Test
    void modifyPlayerName() {
        String newName = "Updated Player Name";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(newName, headers);

        // Make the PATCH request to the endpoint
        ResponseEntity<Map> response = restTemplate.exchange("/api/player/" + testPlayerId + "/name", HttpMethod.PATCH, entity, Map.class);

        System.out.println("Response: " + response);
        System.out.println("Response Body: " + response.getBody());

        // Assert the status code
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");

        // Assert that the response body contains the key 'quantityOfModifiedRows'
        Assertions.assertTrue(response.getBody().containsKey("quantityOfModifiedRows"), "Response body should contain the key 'quantityOfModifiedRows'");
        Assertions.assertEquals(1, response.getBody().get("quantityOfModifiedRows"), "The number of modified rows should be 1");

        // Check if the player's name is updated
        Optional<Player> updatedPlayer = playerRepository.findById(testPlayerId);
        Assertions.assertTrue(updatedPlayer.isPresent(), "Player should exist after name modification");
        Assertions.assertEquals(newName, updatedPlayer.get().getName(), "Player's name should be updated");
    }

    /**
     * Test for PUT mapping to update a player's details.
     */
    @Test
    void updatePlayer() {
        Player updatedPlayerDetails = new Player("Updated Player", "newpassword123", "Captain");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Player> entity = new HttpEntity<>(updatedPlayerDetails, headers);

        // Make the PUT request to the endpoint
        ResponseEntity<Player> response = restTemplate.exchange("/api/player/" + testPlayerId, HttpMethod.PUT, entity, Player.class);

        System.out.println("Response: " + response);
        System.out.println("Response Body: " + response.getBody());

        // Assert the status code
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");

        // Assert that the updated player's details match the input details
        Assertions.assertEquals(updatedPlayerDetails.getName(), response.getBody().getName(), "Player's name should be updated");
        Assertions.assertEquals(updatedPlayerDetails.getPassword(), response.getBody().getPassword(), "Player's password should be updated");
        Assertions.assertEquals(updatedPlayerDetails.getRole(), response.getBody().getRole(), "Player's role should be updated");
    }
}
