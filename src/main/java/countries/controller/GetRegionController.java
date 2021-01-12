package countries.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import countries.dao.Country;
import countries.dao.ResultDao;

@RestController
public class GetRegionController {

	private static final String URL = "https://restcountries.eu/rest/v2/region/";
	
	private static final Set<String> validRegions = new HashSet<>();
	
	static {
		validRegions.add("africa");
		validRegions.add("americas");
		validRegions.add("asia");
		validRegions.add("europe");
		validRegions.add("oceania");
	}

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/countries-app/{region}")
	public ResponseEntity<ResultDao> fetchRegionData(@PathVariable final String region) {
		
		if (!validRegions.contains(region.toLowerCase())) {
			throw new IllegalArgumentException("The get parameter is not valid!");
		}

		ResponseEntity<Country[]> countries = restTemplate.getForEntity(URL + region, Country[].class);

		Country[] allCountries = countries.getBody();
		
		if (allCountries != null && allCountries.length > 0) {
			return new ResponseEntity<>(calculateResult(allCountries), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResultDao(0, 0), HttpStatus.NO_CONTENT);
		}
 		
	}

	private ResultDao calculateResult(final Country[] countries) {
		
		int totalPopulation = 0;
		double totalArea = 0;

		for (Country country : countries) {
			totalPopulation += country.getPopulation();
			totalArea += country.getArea();
		}
		
		return new ResultDao(totalPopulation / countries.length, totalArea / (double) countries.length);
	}
}
