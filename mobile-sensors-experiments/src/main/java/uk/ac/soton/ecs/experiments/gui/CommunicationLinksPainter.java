package uk.ac.soton.ecs.experiments.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Collection;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class CommunicationLinksPainter implements Paintable {

	private Simulation simulation;

	private VisualizationViewer<Location, AccessibilityRelation> visualisationViewer;

	public CommunicationLinksPainter(
			Simulation simulation,
			VisualizationViewer<Location, AccessibilityRelation> visualisationViewer) {
		this.simulation = simulation;
		this.visualisationViewer = visualisationViewer;
	}

	public boolean useTransform() {
		return true;
	}

	public void paint(Graphics g) {
		Collection<Sensor> sensorLocations = simulation.getSensors();

		for (Sensor sensor1 : sensorLocations) {
			for (Sensor sensor2 : sensorLocations) {
				if (!sensor1.equals(sensor2)
						&& simulation.getMessageInterchange().inRange(sensor1,
								sensor2)) {
					drawLine(g, sensor1.getLocation().getCoordinates(), sensor2
							.getLocation().getCoordinates());
				}
			}
		}
	}

	private void drawLine(Graphics g, Point2D point1, Point2D point2) {
		Color oldColor = g.getColor();

		MutableTransformer layoutTransformer = visualisationViewer
				.getRenderContext().getMultiLayerTransformer().getTransformer(
						Layer.LAYOUT);

		point1 = layoutTransformer.transform(point1);
		point2 = layoutTransformer.transform(point2);

		g.setColor(Color.black);
		g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2
				.getX(), (int) point2.getY());

		g.setColor(oldColor);
	}
}
