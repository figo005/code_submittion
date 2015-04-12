package uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;

public abstract class AbstractInformationAndRiskStateMap {
	protected FastMap<Point2D, Double> stateMap;
//	private int informationState;
//	
//	private int riskState;

	private final Grid grid;

	

	public AbstractInformationAndRiskStateMap(Grid grid, int riskState, int riskState) {
		this.riskState=riskState;
		this.riskState=riskState;
		this.grid = grid;
		this.riskPoints = null;		
		initialise();
	}
	
	public AbstractInformationAndRiskStateMap(Grid grid, Collection<Location> riskPoints, double value,double riskValue) {
		this.value = value;
		this.riskValue = riskValue;
		this.riskPoints = riskPoints;
		this.grid = grid;
		initialise();
//		AddRiskValue();
	}

	public AbstractInformationAndRiskStateMap(Grid grid, FastMap<Point2D, Double> map) {
		this.grid = grid;
		this.riskPoints = null;
		this.stateMap = map;
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
		stateMap = new FastMap<Point2D, Double>(getGrid()
				.getGridPointCount() * 4 / 3);
		Validate.isTrue(!getGrid().getGridPoints().isEmpty());
		reset(value);
	}

	protected void AddRiskValue() {
		for (Location riskPont : riskPoints) {
			double temp=stateMap.get(riskPont.getCoordinates());
			stateMap.put(riskPont.getCoordinates(), temp+riskValue);
		}
	}
	protected void reset(double value) {
		for (Point2D point : getGrid().getGridPoints()) {
			stateMap.put(point, value);
		}
	}
	public final Map<Point2D, Double> getValues() {
		return stateMap;
	}

	protected void addToUncertaintyMap(double constant) {
		for (Point2D point : stateMap.keySet()) {
			Double double1 = stateMap.get(point);
			stateMap.put(point, double1 + constant);
		}
	}
}
