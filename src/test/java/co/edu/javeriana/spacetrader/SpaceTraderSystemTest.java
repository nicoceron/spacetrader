package co.edu.javeriana.spacetrader;

import co.edu.javeriana.spacetrader.model.*;
import co.edu.javeriana.spacetrader.repository.*;
import co.edu.javeriana.spacetrader.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("systemtest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpaceTraderSystemTest {
    private ChromeDriver driver;
    private WebDriverWait wait;
    String baseUrl;

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

    @Autowired
    private PlanetaryStockRepository planetaryStockRepository;

    @Autowired
    private ProductService productService;

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
        spaceship.setCredit(BigDecimal.valueOf(10000000));
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

        // Create and save PlanetaryStock for the planet
        Random random = new Random();
        Product product = new Product();
        product.setName("Test Product");
        product.setVolume(random.nextDouble());
        productService.saveOrUpdateProduct(product);

        PlanetaryStock planetaryStock = new PlanetaryStock();
        planetaryStock.setPlanet(planet);
        planetaryStock.setProduct(product);
        planetaryStock.setStock(random.nextInt(1001)); // Random stock between 0 and 1000
        planetaryStock.setDemandFactor(random.nextDouble() * 1000000); // Random demand factor between 0 and 1,000,000
        planetaryStock.setSupplyFactor(random.nextDouble() * 1000000); // Random supply factor between 0 and 1,000,000
        planetaryStockRepository.save(planetaryStock);
        System.out.println("Planetary Stock created for Planet ID: " + testPlanetId);

        // ChromeDriver options setup
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model
        // options.addArguments("--headless"); // Run in headless mode for debugging
        options.addArguments("--disable-gpu"); // Applicable to Windows OS only
        options.addArguments("--disable-extensions"); // Disable extensions
        options.addArguments("start-maximized"); // Start maximized
        options.addArguments("--remote-allow-origins=*");

        // Enable browser logging
        options.setCapability("goog:loggingPrefs", new HashMap<String, Object>() {{
            put(LogType.BROWSER, "ALL");
        }});

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        this.baseUrl = "http://localhost:4200";
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            // Print browser logs
            LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
            for (LogEntry log : logs) {
                System.out.println(log);
            }
            driver.quit();
        }
    }

    @Test
    void testGetClosestStars() {
        System.out.println("Testing endpoint: " + baseUrl + "/mechanic/navigation");
        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure that the endpoint is correct and accessible
        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        assertNotNull(body, "Body element should not be null");
        String responseText = body.getText();
        System.out.println("Response Text: " + responseText);
        assertTrue(responseText.contains("Closest Star"), "Response should contain 'Closest Star'");
    }

    @Test
    void testTravelToStar() {
        System.out.println("Testing navigation and travel functionality");

        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure that the closest stars list is loaded
        WebElement closestStarsList = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".stars-list")));
        assertNotNull(closestStarsList, "Closest stars list should be present");

        // Wait for the first star travel button to be clickable
        WebElement firstStarTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".star-item:first-child .travel-button")));
        assertNotNull(firstStarTravelButton, "First star travel button should be present");

        // Click the travel button to travel to the star
        firstStarTravelButton.click();
        System.out.println("Clicked on travel button for the first star");

        // Wait for the planets in the selected star to load
        WebElement planetsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planets-container")));
        assertNotNull(planetsContainer, "Planets container should be present after traveling to star");

        // Check that planets are loaded
        WebElement planetsList = planetsContainer.findElement(By.cssSelector(".planets-list"));
        assertNotNull(planetsList, "Planets list should be present in the selected star");

        System.out.println("Navigation and travel functionality test passed");
    }


    @Test
    void testTravelToPlanet() {
        System.out.println("Testing navigation and travel functionality");

        // Open the navigation page
        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure the closest stars list is loaded
        WebElement closestStarsList = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".stars-list")));
        assertNotNull(closestStarsList, "Closest stars list should be present");

        // Wait for and click the travel button for the first star
        WebElement firstStarTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".star-item:first-child .travel-button")));
        assertNotNull(firstStarTravelButton, "First star travel button should be present");
        firstStarTravelButton.click();
        System.out.println("Clicked on travel button for the first star");

        // Wait for the planets in the selected star to load
        WebElement planetsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planets-container")));
        assertNotNull(planetsContainer, "Planets container should be present after traveling to star");

        // Wait for and click the travel button for the first planet
        WebElement firstPlanetTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".planet-item:first-child .travel-button")));
        assertNotNull(firstPlanetTravelButton, "First planet travel button should be present");
        firstPlanetTravelButton.click();
        System.out.println("Clicked on travel button for the first planet");

        // Wait for navigation to trade component (optional verification step)
        wait.until(ExpectedConditions.urlContains("/mechanic/trade"));
        System.out.println("Navigated to the trade component");
    }

    @Test
    void testListPlanetaryStock() {
        System.out.println("Testing endpoint: " + baseUrl + "/mechanic/navigation");
        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure the closest stars list is loaded
        WebElement closestStarsList = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".stars-list")));
        assertNotNull(closestStarsList, "Closest stars list should be present");

        // Wait for and click the travel button for the first star
        WebElement firstStarTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".star-item:first-child .travel-button")));
        assertNotNull(firstStarTravelButton, "First star travel button should be present");
        firstStarTravelButton.click();
        System.out.println("Clicked on travel button for the first star");

        // Wait for the planets in the selected star to load
        WebElement planetsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planets-container")));
        assertNotNull(planetsContainer, "Planets container should be present after traveling to star");

        // Wait for and click the travel button for the first planet
        WebElement firstPlanetTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".planet-item:first-child .travel-button")));
        assertNotNull(firstPlanetTravelButton, "First planet travel button should be present");
        firstPlanetTravelButton.click();
        System.out.println("Clicked on travel button for the first planet");

        // Wait for navigation to trade component
        wait.until(ExpectedConditions.urlContains("/mechanic/trade"));

        // Ensure that the trade page is loaded and contains the planetary stock
        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        assertNotNull(body, "Body element should not be null");
        String responseText = body.getText();
        System.out.println("Response Text: " + responseText);
        assertTrue(responseText.contains("Planetary Stock"), "Response should contain 'Planetary Stock'");
    }




    @Test
    void testBuyProduct() {
        System.out.println("Testing endpoint: " + baseUrl + "/mechanic/navigation");
        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure the closest stars list is loaded
        WebElement closestStarsList = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".stars-list")));
        assertNotNull(closestStarsList, "Closest stars list should be present");

        // Wait for and click the travel button for the first star
        WebElement firstStarTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".star-item:first-child .travel-button")));
        assertNotNull(firstStarTravelButton, "First star travel button should be present");
        firstStarTravelButton.click();
        System.out.println("Clicked on travel button for the first star");

        // Wait for the planets in the selected star to load
        WebElement planetsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planets-container")));
        assertNotNull(planetsContainer, "Planets container should be present after traveling to star");

        // Wait for and click the travel button for the first planet
        WebElement firstPlanetTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".planet-item:first-child .travel-button")));
        assertNotNull(firstPlanetTravelButton, "First planet travel button should be present");
        firstPlanetTravelButton.click();
        System.out.println("Clicked on travel button for the first planet");

        // Wait for navigation to trade component
        wait.until(ExpectedConditions.urlContains("/mechanic/trade"));

        // Ensure the planetary stock table is loaded
        WebElement planetaryStockTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planetary-stock-container")));
        assertNotNull(planetaryStockTable, "Planetary stock container should be present");

        // Wait for and click the buy button for the first stock item
        WebElement firstBuyButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody tr:first-child .btn-success")));
        assertNotNull(firstBuyButton, "First buy button should be present");
        firstBuyButton.click();
        System.out.println("Clicked on buy button for the first stock item");

        // Wait for the alert and check if it contains the success message
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertNotNull(alert, "Alert should be present after buying stock");
        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);
        assertTrue(alertText.contains("Stock purchased successfully"), "Alert should contain 'Stock purchased successfully'");

        // Accept the alert
        alert.accept();
    }


    @Test
    void testSellProduct() {
        System.out.println("Testing endpoint: " + baseUrl + "/mechanic/navigation");
        driver.get(baseUrl + "/mechanic/navigation");

        // Ensure the closest stars list is loaded
        WebElement closestStarsList = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".stars-list")));
        assertNotNull(closestStarsList, "Closest stars list should be present");

        // Wait for and click the travel button for the first star
        WebElement firstStarTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".star-item:first-child .travel-button")));
        assertNotNull(firstStarTravelButton, "First star travel button should be present");
        firstStarTravelButton.click();
        System.out.println("Clicked on travel button for the first star");

        // Wait for the planets in the selected star to load
        WebElement planetsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planets-container")));
        assertNotNull(planetsContainer, "Planets container should be present after traveling to star");

        // Wait for and click the travel button for the first planet
        WebElement firstPlanetTravelButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".planet-item:first-child .travel-button")));
        assertNotNull(firstPlanetTravelButton, "First planet travel button should be present");
        firstPlanetTravelButton.click();
        System.out.println("Clicked on travel button for the first planet");

        // Wait for navigation to trade component
        wait.until(ExpectedConditions.urlContains("/mechanic/trade"));

        // Ensure the planetary stock table is loaded
        WebElement planetaryStockTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".planetary-stock-container")));
        assertNotNull(planetaryStockTable, "Planetary stock container should be present");

        // Wait for and click the sell button for the first stock item
        WebElement firstSellButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody tr:first-child .btn-danger")));
        assertNotNull(firstSellButton, "First sell button should be present");
        firstSellButton.click();
        System.out.println("Clicked on sell button for the first stock item");

        // Wait for the alert and check if it contains the success message
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertNotNull(alert, "Alert should be present after selling stock");
        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);
        assertTrue(alertText.contains("Stock sold successfully"), "Alert should contain 'Stock sold successfully'");

        // Accept the alert
        alert.accept();
    }

}
