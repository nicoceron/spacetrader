package co.edu.javeriana.spacetrader;

import co.edu.javeriana.spacetrader.model.Player;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.repository.PlayerRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
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

    private Long testPlayerId;
    private Long testSpaceshipId;
    private Long testStarId;

    @BeforeEach
    void init() {
        // Create and save a Model
        Model model = new Model("Test Model", 5000, 100);
        modelRepository.save(model);

        // Create and save a Star
        Star star = new Star("Test Star", 0.0, 0.0, 0.0, true);
        starRepository.save(star);
        testStarId = star.getId();

        // Create and save a Spaceship
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Test Spaceship");
        spaceship.setModel(model);
        spaceship.setCredit(BigDecimal.valueOf(10000));
        spaceship.setCurrentStar(star);
        spaceshipRepository.save(spaceship);
        testSpaceshipId = spaceship.getId();

        // Create and save Player
        Player player = new Player("Test Player", "password123", "Pilot");
        playerRepository.save(player);
        testPlayerId = player.getId();
    }

    @Test
    void getClosestStars() {
        ResponseEntity<Star[]> response = restTemplate.getForEntity("/api/navigation/closest-stars/" + testSpaceshipId, Star[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void travelToStar() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Travel time: 10 days");

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/navigation/travel-to-star/" + testSpaceshipId + "/" + testStarId, null, Map.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(body.get("message"), response.getBody().get("message"));
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
