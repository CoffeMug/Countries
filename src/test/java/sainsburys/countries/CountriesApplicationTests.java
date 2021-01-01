package sainsburys.countries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import sainsburys.countries.controller.GetRegionController;
import sainsburys.countries.dao.ResultDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountriesApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private GetRegionController getRegionController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertNotNull(getRegionController);
	}

	@Test
	void testRegionServiceGetEurope() {
		ResponseEntity<ResultDao> country = getRegionController.fetchRegionData("Americas");
		assertNotNull(country);
		assertTrue(country.getBody().getAverageArea() > 0);
		assertTrue(country.getBody().getAveragePopulation() > 0);
		assertEquals(HttpStatus.OK, country.getStatusCode());
	}

	@Test
	void statusShouldBeOKSendingValidRegion() {

		ResponseEntity<ResultDao> entity = this.restTemplate
				.getForEntity("http://localhost:" + this.port + "/sainsbury-app/asia", ResultDao.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue(entity.getBody().getAverageArea() > 0);
		assertTrue(entity.getBody().getAveragePopulation() > 0);

	}
	
	@Test
	void statusShouldBeInternalServerErrorSendingInValidRegion() {

		ResponseEntity<ResultDao> entity = this.restTemplate
				.getForEntity("http://localhost:" + this.port + "/sainsbury-app/asian", ResultDao.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
		assertEquals(0, entity.getBody().getAverageArea());
		assertEquals(0, entity.getBody().getAveragePopulation());

	}
	
	
	@Test
	void statusShouldBeNotFoundSendingInValidGetParam() {

		ResponseEntity<ResultDao> entity = this.restTemplate
				.getForEntity("http://localhost:" + this.port + "/sainsbury-app/regi", ResultDao.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
		assertEquals(0, entity.getBody().getAverageArea());
		assertEquals(0, entity.getBody().getAveragePopulation());

	}
	
	@Test
	void statusShouldBeBadRequestSendingNoRegion() {
		ResponseEntity<ResultDao> entity = this.restTemplate
				.getForEntity("http://localhost:" + this.port + "/sainsbury-app/", ResultDao.class);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
		assertEquals(0, entity.getBody().getAverageArea());
		assertEquals(0, entity.getBody().getAveragePopulation());
	}

	@Test
	void testRegionServiceNonExistingRegion() {

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			getRegionController.fetchRegionData("blabla");
		});

		String expectedMessage = "The get parameter is not valid!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
