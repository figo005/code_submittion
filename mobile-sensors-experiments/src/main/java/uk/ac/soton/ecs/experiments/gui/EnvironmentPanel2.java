package uk.ac.soton.ecs.experiments.gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorID;
import edu.uci.ics.jung.graph.util.Pair;

public class EnvironmentPanel2 extends Canvas implements EPanel {

	private static final double VERTEX_RADIUS = 1;
	private static final Color SENSOR_COLOR = Color.white;
	private static final double SENSOR_RADIUS = 2;
	private static final float STROKE_WIDTH = 0.2f;
	private static final boolean DRAW_SENSORS = true;
	private Map<Point2D, java.lang.Double> spatialField;
	private Map<SensorID, Location> sensorLocations;
	private Location evaderLocation;
	private Collection<Location> attackLocations;
	private AccessibilityGraphImpl graph;
	private Double maxSpatialField;
	private Map<SensorID, List<Location>> oldSensorLocations = new HashMap<SensorID, List<Location>>();

	public EnvironmentPanel2(Simulation simulation) {
		this.graph = simulation.getEnvironment().getAccessibilityGraph();
		repaint();
	}

	public void setAttackLocations(Collection<Location> locations) {
		this.attackLocations = locations;
		repaint();
	}

	public void setEvaderLocation(Location evaderLocation) {
		this.evaderLocation = evaderLocation;
		repaint();
	}

	public void setSensorLocations(Map<SensorID, Location> locations) {
		for (SensorID id : locations.keySet()) {
			if (!oldSensorLocations.containsKey(id)) {
				oldSensorLocations.put(id, new ArrayList<Location>());
			}

			oldSensorLocations.get(id).add(locations.get(id));
		}

		this.sensorLocations = locations;
		repaint();
	}

	public void setSpatialField(Map<Point2D, java.lang.Double> spatialField) {
		this.spatialField = spatialField;
		maxSpatialField = Collections.max(spatialField.values());

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle2D boundingBox = graph.getBoundingBox();

		double scaleX = getSize().getWidth() / boundingBox.getWidth();
		double scaleY = getSize().getHeight() / boundingBox.getHeight();

		double minScale = Math.min(scaleX, scaleY);

		AffineTransform transform = AffineTransform.getScaleInstance(minScale,
				minScale);

		transform.concatenate(AffineTransform.getTranslateInstance(-boundingBox
				.getMinX(), -boundingBox.getMinY()));
		g2.setTransform(transform);

		drawGraph(g2);

	}

	private void drawOldLocations(Graphics2D g2) {
		for (SensorID id : oldSensorLocations.keySet()) {

			List<Location> list = oldSensorLocations.get(id);

			for (int i = 1; i < list.size(); i++) {
				g2.setStroke(new BasicStroke(2 * STROKE_WIDTH));

				Point2D first = list.get(i - 1).getCoordinates();
				Point2D second = list.get(i).getCoordinates();

				g2.setColor(Color.BLACK);

				// drawArrow(g2, (int) first.getX(), (int) first.getY(),
				// (int) second.getX(), (int) second.getY());

				g2.setStroke(new BasicStroke(2 * STROKE_WIDTH));
				g2.drawLine((int) first.getX(), (int) first.getY(),
						(int) second.getX(), (int) second.getY());

				g2.setStroke(new BasicStroke(STROKE_WIDTH));
			}

		}
	}

	private void drawGraph(Graphics2D g2) {
		if (graph == null)
			return;

		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		// draw edges
		for (AccessibilityRelation connection : graph.getEdges()) {
			Pair<Location> endpoints = graph.getEndpoints(connection);

			Point2D first = endpoints.getFirst().getCoordinates();
			Point2D second = endpoints.getSecond().getCoordinates();

			g2.setColor(Color.BLACK);
			g2.drawLine((int) first.getX(), (int) first.getY(), (int) second
					.getX(), (int) second.getY());

			// calculate intersection between circle and line
			// double deltaX = second.getX() - first.getX();
			// double deltaY = second.getY() - first.getY();
			//
			// double lengthSq = deltaX * deltaX + deltaY * deltaY;
			// double frac = 1 - Math.sqrt((SENSOR_RADIUS * SENSOR_RADIUS)
			// / lengthSq);
			//
			// double intersectionX = deltaX * frac + first.getX();
			// double intersectionY = deltaY * frac + first.getY();

			// g2.setColor(Color.YELLOW);
			// g2.fillOval((int) intersectionX - 3, (int) intersectionY - 3, 6,
			// 6);
		}

		drawOldLocations(g2);

		// draw locations
		for (Location location : graph.getVertices()) {

			if (isSensorLocation(location) && DRAW_SENSORS) {
				g2.setStroke(new BasicStroke(2 * STROKE_WIDTH));
				g2.setColor(SENSOR_COLOR);
				Ellipse2D vertex = new Ellipse2D.Double(location.getX()
						- SENSOR_RADIUS, location.getY() - SENSOR_RADIUS,
						2 * SENSOR_RADIUS, 2 * SENSOR_RADIUS);
				g2.setColor(Color.WHITE);
				g2.fill(vertex);
				g2.setColor(Color.BLACK);
				g2.draw(vertex);
				g2.setStroke(new BasicStroke(STROKE_WIDTH));
			}
			// else if (isOldSensorLocation(location))
			// {
			// g2.setStroke(new BasicStroke(2.0f));
			// g2.setColor(SENSOR_COLOR);
			// g2.setColor(Color.WHITE);
			// Ellipse2D vertex = new Ellipse2D.Double(location.getX()
			// - SENSOR_RADIUS / 2, location.getY() - SENSOR_RADIUS
			// / 2, SENSOR_RADIUS, SENSOR_RADIUS);
			// g2.fill(vertex);
			// g2.setColor(Color.BLACK);
			// g2.draw(vertex);
			// g2.setStroke(new BasicStroke(1.0f));
			// }
			else if (isAttackerLocation(location)) {
				g2.setStroke(new BasicStroke(2 * STROKE_WIDTH));
				g2.setColor(SENSOR_COLOR);
				g2.setColor(Color.WHITE);
				Rectangle2D vertex = new Rectangle2D.Double(location.getX()
						- SENSOR_RADIUS, location.getY() - SENSOR_RADIUS,
						2 * SENSOR_RADIUS, 2 * SENSOR_RADIUS);
				g2.fill(vertex);
				g2.setColor(Color.BLACK);
				g2.draw(vertex);
				g2.setStroke(new BasicStroke(STROKE_WIDTH));
			} else {
				Double locationVariance = null;

				if (spatialField != null)
					locationVariance = spatialField.get(location
							.getCoordinates());

				double radius;

				if (locationVariance == null)
					radius = 0.0;
				else
					radius = locationVariance / maxSpatialField * VERTEX_RADIUS;

				g2.setColor(Color.GRAY);

				Ellipse2D vertex = new Ellipse2D.Double(location.getX()
						- radius, location.getY() - radius, radius * 2,
						radius * 2);

				g2.fill(vertex);
				g2.setColor(Color.BLACK);
				g2.draw(vertex);
			}
		}

		g2.setStroke(oldStroke);
	}

	// private boolean isOldSensorLocation(Location location) {
	// return oldSensorLocations.contains(location);
	// }

	private boolean isAttackerLocation(Location location) {
		if (evaderLocation != null)
			return evaderLocation.equals(location);

		if (attackLocations != null) {
			return attackLocations.contains(location);
		}

		return false;
	}

	private boolean isSensorLocation(Location location) {
		if (sensorLocations == null)
			return false;

		return sensorLocations.values().contains(location);
	}

	public void setAccessibilityGraph(AccessibilityGraphImpl graph) {
		this.graph = graph;
	}

	public void addPickedStateListener(ItemListener listener) {
	}

	public Component getComponent() {
		return this;
	}

	public void resetViewer() {
	}

	public void saveToSVG() {
		// Get a DOMImplementation
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		String svgNamespaceURI = "http://www.w3.org/2000/svg";

		// Create an instance of org.w3c.dom.Document
		Document document = domImpl
				.createDocument(svgNamespaceURI, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Render into the SVG Graphics2D implementation
		paint(svgGenerator);

		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute

		OutputStream printWriter;
		try {
			printWriter = new FileOutputStream("test_"
					+ System.currentTimeMillis() + ".svg");

			Writer out = new OutputStreamWriter(printWriter, "UTF-8");
			svgGenerator.stream(out, useCSS);
			printWriter.flush();
			printWriter.close();

			System.out.println("Saved to " + printWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Draws an arrow on the given Graphics2D context
	 * 
	 * @param g
	 *            The Graphics2D context to draw on
	 * @param x
	 *            The x location of the "tail" of the arrow
	 * @param y
	 *            The y location of the "tail" of the arrow
	 * @param xx
	 *            The x location of the "head" of the arrow
	 * @param yy
	 *            The y location of the "head" of the arrow
	 */
	private void drawArrow(Graphics2D g, int x, int y, int xx, int yy) {
		float arrowWidth = 0.8f;
		float theta = 0.423f;
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		float[] vecLine = new float[2];
		float[] vecLeft = new float[2];
		float fLength;
		float th;
		float ta;
		float baseX, baseY;

		xPoints[0] = xx;
		yPoints[0] = yy;

		// build the line vector
		vecLine[0] = (float) xPoints[0] - x;
		vecLine[1] = (float) yPoints[0] - y;

		// build the arrow base vector - normal to the line
		vecLeft[0] = -vecLine[1];
		vecLeft[1] = vecLine[0];

		// setup length parameters
		fLength = (float) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1]
				* vecLine[1]);
		th = arrowWidth / (2.0f * fLength);
		ta = arrowWidth / (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

		// find the base of the arrow
		baseX = ((float) xPoints[0] - ta * vecLine[0]);
		baseY = ((float) yPoints[0] - ta * vecLine[1]);

		// build the points on the sides of the arrow
		xPoints[1] = (int) (baseX + th * vecLeft[0]);
		yPoints[1] = (int) (baseY + th * vecLeft[1]);
		xPoints[2] = (int) (baseX - th * vecLeft[0]);
		yPoints[2] = (int) (baseY - th * vecLeft[1]);

		g.setStroke(new BasicStroke(STROKE_WIDTH));

		g.drawLine(x, y, (int) baseX, (int) baseY);
		g.fillPolygon(xPoints, yPoints, 3);
	}
}
