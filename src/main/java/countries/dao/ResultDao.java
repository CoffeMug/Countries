package countries.dao;

public class ResultDao {

	private int averagePopulation;

	private double averageArea;

	public ResultDao(int averagePopulation, double averageArea) {
		this.averagePopulation = averagePopulation;
		this.averageArea = averageArea;
	}

	public int getAveragePopulation() {
		return averagePopulation;
	}

	public double getAverageArea() {
		return averageArea;
	}

}
