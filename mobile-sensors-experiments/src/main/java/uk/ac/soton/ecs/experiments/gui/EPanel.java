package uk.ac.soton.ecs.experiments.gui;

import java.awt.Component;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorID;

public interface EPanel {

	public abstract void setSensorLocations(Map<SensorID, Location> locations);

	public abstract void setSpatialField(Map<Point2D, Double> spatialField);

	public abstract void setEvaderLocation(Location evaderLocation);

	public abstract void setAttackLocations(Collection<Location> locations);
	
	public abstract void setRiskLocations(Collection<Location> locations);
	

	
	public abstract void setAccessibilityGraph(AccessibilityGraphImpl graph);

	public abstract void resetViewer();

	public abstract void saveToSVG();

	public abstract void addPickedStateListener(ItemListener listener);

	public abstract Component getComponent();
	
	public abstract void setInformationStates(Map<Integer, Integer> informationStates);

	public abstract void setRiskStates(Map<Integer, Integer> riskStates);
	

	

}