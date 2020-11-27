package sainsburys.countries;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import sainsburys.countries.dao.Country;
import sainsburys.countries.service.GetRegionService;

@SpringBootTest
class CountriesApplicationTests {
	
	@Autowired
	private GetRegionService getRegionService;

    @Test
    void contextLoads() {
    }

    @Test
    void testRegionServiceLoad() {
    	
    	assertNotNull(getRegionService);
    	
    }
    
    @Test
    void testRegionServiceGetEurope() {
    	Optional<Country> country = getRegionService.fetchRegionData("Americas");
    	
    	Country result = country.get();
    	assertNotNull(result);
    	assertNotEquals(0, result.getArea(), "Area must not be zero!");
    	assertTrue(result.getArea() > 0);
    	assertTrue(result.getPopulation() > 0);
    }
    
    @Test
    void testRegionServiceNonExistingRegion() {
    	
    	Exception exception = assertThrows(HttpClientErrorException.class, () -> {
    		getRegionService.fetchRegionData("blabla");
        });
     
        String expectedMessage = "Not Found";
        String actualMessage = exception.getMessage();
     
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
}
