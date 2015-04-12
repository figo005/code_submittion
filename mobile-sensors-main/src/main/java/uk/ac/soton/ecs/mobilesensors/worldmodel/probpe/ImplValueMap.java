package uk.ac.soton.ecs.mobilesensors.worldmodel.probpe;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader.AbstractValueMap;

public class ImplValueMap extends AbstractValueMap {

	public ImplValueMap(Grid grid) {
		super(grid, 0.0);
	}
	public ImplValueMap(Grid grid, double value) {
		super(grid, value);
	}
	public ImplValueMap(Grid grid, FastMap<Point2D, Double> map) {
		super(grid, map);
	}

	public void createFlatPrior() {
		for (Point2D point : getGrid().getGridPoints()) {
			uncertaintyMap.put(point, 1.0 / getGrid().getGridPointCount());
		}
	}
	public void createFlatPrior(double count) {
		for (Point2D point : getGrid().getGridPoints()) {
			uncertaintyMap.put(point, count / getGrid().getGridPointCount());
		}
	}
	public ImplValueMap copy() {
		ImplValueMap copy = new ImplValueMap(getGrid());
		copy.uncertaintyMap = new FastMap<Point2D, Double>(uncertaintyMap);
		return copy;
	}

	public void normalise() {
		double sum = 0.0;

		for (Double p : uncertaintyMap.values()) {
			sum += p;
		}

		Validate.isTrue(sum >= 0.0);

		for (Point2D point : uncertaintyMap.keySet()) {
			Double previousP = uncertaintyMap.get(point);
			Validate.isTrue(!Double.isNaN(previousP));
			double newValue = previousP / sum;
			if (Double.isNaN(newValue)) {
				System.out.println(newValue);
				System.out.println(sum);
				System.out.println(previousP);
				System.out.println(uncertaintyMap);
			}

			Validate.isTrue(!Double.isNaN(newValue));

			uncertaintyMap.put(point, newValue);
		}
	}

	public Set<Point2D> getPoints() {
		return uncertaintyMap.keySet();
	}

	public double getValue(Point2D x) {
		return uncertaintyMap.get(x);
	}

	public void put(Point2D x, double d) {
		Validate.isTrue(uncertaintyMap.containsKey(x));
		Validate.isTrue(!Double.isNaN(d));
		Validate.isTrue(!Double.isInfinite(d));
		uncertaintyMap.put(x, d);
	}

	public void checkValidity() {
		double sum = 0.0;

		for (Double p : uncertaintyMap.values()) {
			sum += p;
		}

		Validate.isTrue(Math.abs(sum - 1.0) < 1e-8, sum + "");
	}

	public ImplValueMap multiply(double scalar) {
		ImplValueMap probabilityMap = new ImplValueMap(getGrid());

		Map<Point2D, Double> values = getValues();
		for (Point2D point : values.keySet()) {
			double value = values.get(point);
			probabilityMap.uncertaintyMap.put(point, value * scalar);
		}

		return probabilityMap;
	}

	public ImplValueMap plus(ImplValueMap other) {
		ImplValueMap probabilityMap = new ImplValueMap(getGrid());

		Map<Point2D, Double> values1 = getValues();
		Map<Point2D, Double> values2 = other.getValues();
		for (Point2D point : values1.keySet()) {
			double value1 = values1.get(point);
			double value2 = values2.get(point);
			probabilityMap.uncertaintyMap.put(point, value1 + value2);
		}

		return probabilityMap;
	}
}
