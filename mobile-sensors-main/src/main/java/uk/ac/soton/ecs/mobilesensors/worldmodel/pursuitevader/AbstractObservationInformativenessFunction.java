package uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.worldmodel.Observation;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public abstract class AbstractObservationInformativenessFunction implements
		ObservationInformativenessFunction {

	private Grid grid;
	private AccessibilityGraphImpl graph;

	public final AccessibilityGraphImpl getGraph() {
		return graph;
	}

	public final void setGraph(AccessibilityGraphImpl graph) {
		this.graph = graph;
	}
	
	public final Grid getGrid() {
		return grid;
	}

	public final void setGrid(Grid grid) {
		this.grid = grid;
	}

	public final Point2D getMaximumInformativeLocation() {
		return getMaximumInformativenessLocation(getValues());
	}

	public final static Point2D getMaximumInformativenessLocation(
			Map<Point2D, Double> values) {
		double maxValue = Double.NEGATIVE_INFINITY;
		Point2D bestPoint = null;

		for (Point2D point : values.keySet()) {
			if (values.get(point) > maxValue) {
				bestPoint = point;
				maxValue = values.get(point);
			}
		}

		return bestPoint;
	}

	public Collection<Observation> observe(Collection<Location> locations) {
		Collection<Observation> observations = new ArrayList<Observation>();

		for (Location location : locations) 

				observations.addAll(observe(location));

		return observations;
	}
}
