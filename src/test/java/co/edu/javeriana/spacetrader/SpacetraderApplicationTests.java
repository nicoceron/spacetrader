package co.edu.javeriana.spacetrader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest
class SpacetraderApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

}
