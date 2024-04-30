package co.edu.javeriana.spacetrader;

import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.model.Planet;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.model.Star;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
import co.edu.javeriana.spacetrader.repository.PlanetRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;

@ActiveProfiles("system-test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NavigationControllerSystemTest {

    private ChromeDriver driver;
    private WebDriverWait wait;


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


    private String baseUrl;

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


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("-start--maximized");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.baseUrl = "http://localhost:4200";
    }

    @AfterEach
    void end() {
        driver.quit();
    }

}
