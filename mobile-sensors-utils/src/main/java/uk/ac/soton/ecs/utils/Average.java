package uk.ac.soton.ecs.utils;

public class Average {

	private double sum;
	private int n;

	public int getN() {
		return n;
	}

	public double getSum() {
		return sum;
	}

	public double getAverage() {
		return sum / n;
	}

	public void add(double value) {
		sum += value;
		n++;
	}

}
