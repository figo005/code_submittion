package uk.ac.soton.ecs.utils;

import org.apache.commons.lang.Validate;

/**
 * Calculates the entropy of a discrete random variable. Thus, the sum of the
 * sequence should add up to 1.0
 * 
 * @author rs06r
 * 
 */
public class Entropy {

	private double sum;
	private double entropy;
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

	public double getEntropy() {
		if (Math.abs(sum - 1.0) > 1e-6) {
			return Double.NaN;
		}

		return entropy;
	}

	public void add(double value) {
		sum += value;
		n++;

		if (value >= 0.0 && value <= 1.0)
			if (value != 0.0)
				entropy -= value * Math.log(value);

		// Validate.isTrue(value >= 0.0);
		// Validate.isTrue(value <= 1.0);

	}

}
