package co.edu.javeriana.spacetrader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
import co.edu.javeriana.spacetrader.repository.PlanetRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NavigationControllerIntegrationTest {

    private static final String SERVER_URL = "http://localhost:8080/api/navigation";

    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private StarRepository starRepository;
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private SpaceshipRepository spaceshipRepository;
    @Autowired
    private ModelRepository modelRepository;

    private Long spaceshipId;
    private Long starId;
    private Long planetId;

    @BeforeEach
    void init() {
        Star starAlpha = starRepository.save(new Star("Alpha", 0.0, 0.0, 0.0, true));
        Star starBeta = starRepository.save(new Star("Beta", 10.0, 10.0, 10.0, false));

        Planet planetA = planetRepository.save(new Planet("PlanetA", starAlpha));
        Planet planetB = planetRepository.save(new Planet("PlanetB", starBeta));

        Model model = modelRepository.findById(1L).orElseThrow(() -> new RuntimeException("Model not found"));
        Spaceship spaceshipOne = new Spaceship("Space Cruiser", model, new BigDecimal("10000.00"), starAlpha, planetA);
        spaceshipOne = spaceshipRepository.save(spaceshipOne);

        spaceshipId = spaceshipOne.getId();
        starId = starBeta.getId();
        planetId = planetB.getId();
    }
    //Check every star using assert equals method created in class
    @Test
    void getClosestStars() {
        ResponseEntity<List> response = rest.getForEntity(SERVER_URL + "/closest-stars/" + spaceshipId, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().size());
    }

    @Test
    void travelToStar() {
        ResponseEntity<Map> response = rest.postForEntity(SERVER_URL + "/travel-to-star/" + spaceshipId + "/" + starId, null, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void travelToPlanet() {
        ResponseEntity<Map> response = rest.postForEntity(SERVER_URL + "/travel-to-planet/" + spaceshipId + "/" + planetId, null, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getPlanetsInStar() {
        ResponseEntity<List> response = rest.getForEntity(SERVER_URL + "/planets/" + starId, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
