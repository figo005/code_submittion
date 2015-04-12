package uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;

public abstract class AbstractValueMap {
	protected FastMap<Point2D, Double> uncertaintyMap;
	private double value;
	
	private double riskValue;

	private final Grid grid;
	private final Collection<Location> riskPoints;
	

	public AbstractValueMap(Grid grid, double value) {
		setValue(value);
		this.grid = grid;
		this.riskPoints = null;		
		initialise();
	}
	
	public AbstractValueMap(Grid grid, Collection<Location> riskPoints, double value,double riskValue) {
		this.value = value;
		this.riskValue = riskValue;
		this.riskPoints = riskPoints;
		this.grid = grid;
		initialise();
//		AddRiskValue();
	}

	public AbstractValueMap(Grid grid, FastMap<Point2D, Double> map) {
		this.grid = grid;
		this.riskPoints = null;
		this.uncertaintyMap = map;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public void setRiskValue(double riskValue) {
		this.riskValue = riskValue;
	}

	public Grid getGrid() {
		return grid;
	}

	protected void initialise() {
		uncertaintyMap = new FastMap<Point2D, Double>(getGrid()
				.getGridPointCount() * 4 / 3);
		Validate.isTrue(!getGrid().getGridPoints().isEmpty());
		reset(value);
	}

	protected void AddRiskValue() {
		for (Location riskPont : riskPoints) {
			double temp=uncertaintyMap.get(riskPont.getCoordinates());
			uncertaintyMap.put(riskPont.getCoordinates(), temp+riskValue);
		}
	}
	protected void reset(double value) {
		for (Point2D point : getGrid().getGridPoints()) {
			uncertaintyMap.put(point, value);
		}
	}
	public final Map<Point2D, Double> getValues() {
		return uncertaintyMap;
	}
	public void setValues(FastMap<Point2D, Double> values) {
		uncertaintyMap=values;
	}
	
	protected void addToUncertaintyMap(double constant) {
		for (Point2D point : uncertaintyMap.keySet()) {
			Double double1 = uncertaintyMap.get(point);
			uncertaintyMap.put(point, double1 + constant);
		}
	}
}
