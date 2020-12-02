package sainsburys.countries.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sainsburys.countries.dao.Country;
import sainsburys.countries.dao.ResultDao;

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

	@GetMapping("/sainsbury-app")
	@ResponseBody
	public ResponseEntity<ResultDao> fetchRegionData(@RequestParam final String region) {
		
		if (!validRegions.contains(region.toLowerCase())) {
			throw new IllegalArgumentException("The get parameter is not valid!");
		}

		ResponseEntity<Country[]> countries = restTemplate.getForEntity(URL + region, Country[].class);

		Country[] allCountries = countries.getBody();
		
		if (allCountries != null) {
			return new ResponseEntity<>(calculateResult(allCountries), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResultDao(), HttpStatus.NO_CONTENT);
		}
 		
	}

	private ResultDao calculateResult(final Country[] countries) {
		
		int totalPopulation = 0;
		double totalArea = 0;

		for (Country country : countries) {
			totalPopulation += country.getPopulation();
			totalArea += country.getArea();
		}
		
		ResultDao result = new ResultDao();
		result.setAverageArea(totalArea / (double) countries.length);
		result.setAveragePopulation(totalPopulation / countries.length);
		return result;
	}
}
