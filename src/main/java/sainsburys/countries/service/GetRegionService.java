package sainsburys.countries.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sainsburys.countries.dao.Country;
import sainsburys.countries.dao.ResultDao;


@RestController
public class GetRegionService {

	private final String URL = "https://restcountries.eu/rest/v2/region/";
	
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
	public ResultDao fetchRegionData(@RequestParam final String region) {
		
		if (!validRegions.contains(region.toLowerCase())) {
			throw new IllegalArgumentException("The get parameter is not valid!");
		}

		ResponseEntity<Country[]> countries = restTemplate.getForEntity(URL + region, Country[].class);

		int totalPopulation = 0;
		int totalArea = 0;

		for (Country C : countries.getBody()) {
			totalPopulation += C.getPopulation();
			totalArea += C.getArea();
		}

		Country[] allCountries = countries.getBody();
		
		if (allCountries != null) {
			return calculateResult(totalArea, totalPopulation, allCountries.length);
		} else {
			return new ResultDao();
		}
 		
	}

	private ResultDao calculateResult(int totalArea, int totalPopulation, int length) {
		ResultDao result = new ResultDao();
		result.setAverageArea((double) totalArea / (double) length);
		result.setAveragePopulation(totalPopulation / length);
		return result;
	}
}
