package sainsburys.countries.dao;

import java.io.Serializable;

public class ResultDao implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int averagePopulation;

	private double averageArea;
	
	public int getAveragePopulation() {
		return averagePopulation;
	}
	public void setAveragePopulation(int averagePopulation) {
		this.averagePopulation = averagePopulation;
	}
	public double getAverageArea() {
		return averageArea;
	}
	public void setAverageArea(double averageArea) {
		this.averageArea = averageArea;
	}

}
