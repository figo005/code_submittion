package uk.ac.soton.ecs.experiments.util;

import java.awt.geom.Point2D;
import java.io.File;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.GraphFactory;
import uk.ac.soton.ecs.mobilesensors.layout.GraphGridAdaptor;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.layout.gui.GraphGUI;

public class GridCreator {
	public static void main(String[] args) throws Exception {
		// RectangularGrid grid = new RectangularGrid(50, 50, 31, 0.0, 0.0);
		// grid.write(new File("src/main/resources/grids/"
		// + "rectgraph50-50-31.txt"));

		// createLargeRoom();
		create2Rooms();

		// GraphGridAdaptor graphGridAdaptor = new GraphGridAdaptor();
		// graphGridAdaptor.setGraph(graph);
		// graphGridAdaptor.afterPropertiesSet();
		// graphGridAdaptor.write(new File("src/main/resources/grids/"
		// + "multiple_rooms.txt"));

		// x=0.5,y=1.0,w=40.0,h=30.0
	}

	private static void create2Rooms() throws Exception {
		AccessibilityGraphImpl room1 = GraphFactory.createRectangularGridGraph(
				300, 300, 3, 3, 10, 10.0);
		AccessibilityGraphImpl room2 = GraphFactory.createRectangularGridGraph(
				300, 300, 3, 3, 10, 400.0);

		room1.copyFrom(room2);
		Location vertex1 = room1
				.getNearestLocation(new Point2D.Float(160, 310));
		Location vertex2 = room1
				.getNearestLocation(new Point2D.Float(160, 450));

		System.out.println(vertex1);
		System.out.println(vertex2);

		room1.addAccessibilityRelation(vertex1, vertex2);

		new GraphGUI(room1);

		room1.write("src/main/resources/graphs/2rooms.txt");

		room1.writeGrid("src/main/resources/grids/2rooms.txt");
	}

	private static void createLargeRoom() throws Exception {
		int gridResolution = 4;
		AccessibilityGraphImpl room1 = GraphFactory.createRectangularGridGraph(
				100, 100, gridResolution, 0, 0);

		room1.write("src/main/resources/graphs/large-room.txt");

		GraphGridAdaptor graphGridAdaptor = new GraphGridAdaptor();
		graphGridAdaptor.setGraph(room1);
		graphGridAdaptor.afterPropertiesSet();
		graphGridAdaptor.write(new File("src/main/resources/grids/"
				+ "large-room.txt"));
	}

	private static void create4Rooms() throws Exception {
		int gridResolution = 2;
		AccessibilityGraphImpl room1 = GraphFactory.createRectangularGridGraph(
				40, 30, gridResolution, 0, 0);

		AccessibilityGraphImpl corridor12 = GraphFactory
				.createRectangularGridGraph(2, 4, gridResolution, 42, 14);

		AccessibilityGraphImpl connect = GraphFactory.connect(room1,
				corridor12, gridResolution);

		AccessibilityGraphImpl room2 = GraphFactory.createRectangularGridGraph(
				40, 30, gridResolution, 46, 0);

		connect = GraphFactory.connect(connect, room2, gridResolution);

		AccessibilityGraphImpl room3 = GraphFactory.createRectangularGridGraph(
				40, 30, gridResolution, 0, 36);

		AccessibilityGraphImpl corridor13 = GraphFactory
				.createRectangularGridGraph(4, 2, gridResolution, 18, 32);

		connect = GraphFactory.connect(connect, room3, gridResolution);
		connect = GraphFactory.connect(connect, corridor13, gridResolution);

		AccessibilityGraphImpl room4 = GraphFactory.createRectangularGridGraph(
				40, 30, gridResolution, 46, 36);

		AccessibilityGraphImpl corridor34 = GraphFactory
				.createRectangularGridGraph(4, 2, gridResolution, 66, 32);
		connect = GraphFactory.connect(connect, corridor34, gridResolution);

		AccessibilityGraphImpl corridor24 = GraphFactory
				.createRectangularGridGraph(2, 4, gridResolution, 42, 50);
		connect = GraphFactory.connect(connect, corridor24, gridResolution);

		connect = GraphFactory.connect(connect, room4, gridResolution);

		System.out.println(connect.getBoundingBox());

		new GraphGUI(connect);

		connect.write("src/main/resources/graphs/4rooms-graph.txt");

		GraphGridAdaptor graphGridAdaptor = new GraphGridAdaptor();
		graphGridAdaptor.setGraph(connect);
		graphGridAdaptor.afterPropertiesSet();
		graphGridAdaptor.write(new File("src/main/resources/grids/"
				+ "4rooms-grid.txt"));

	}
}
