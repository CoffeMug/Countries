package countries.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
	
	private int population;

	private double area;
	
	public Country() {}
	
	public Country(int population, double area) {
		this.population = population;
		this.area = area;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public double getArea() {
		return area;
	}

}
