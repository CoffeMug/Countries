package sainsburys.countries.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sainsburys.countries.dao.Country;

@RestController
public class GetRegionService {

	private final String URL = "https://restcountries.eu/rest/v2/region/";

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping
	public Optional<Country> fetchRegionData(final String region) {

		ResponseEntity<Country[]> countries = restTemplate.getForEntity(URL + region, Country[].class);

		int totalPopulation = 0;
		int totalArea = 0;

		for (Country C : countries.getBody()) {
			totalPopulation += C.getPopulation();
			totalArea += C.getArea();
		}

		Country[] allCountries = countries.getBody();
		
		if (allCountries != null) {
			return Optional.ofNullable(calculateResult(totalArea, totalPopulation, allCountries.length));
		} else {
			return Optional.empty();
		}
 		
	}

	private Country calculateResult(int totalArea, int totalPopulation, int length) {
		Country result = new Country();
		result.setArea((double) totalArea / (double) length);
		result.setPopulation(totalPopulation / length);

		System.out.println("Average Area is " + result.getArea());
		System.out.println("Average population is " + result.getPopulation());
		
		return result;
	}
}
