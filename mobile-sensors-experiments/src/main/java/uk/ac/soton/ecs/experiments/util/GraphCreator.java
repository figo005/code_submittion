package uk.ac.soton.ecs.experiments.util;

import java.io.File;
import java.io.IOException;

import uk.ac.soton.ecs.mobilesensors.configuration.AccessibilityGraphIO;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.gui.GraphGUI;

public class GraphCreator {
	public static void main(String[] args) throws IOException {
		// AccessibilityGraphImpl graph1 = GraphFactory
		// .createRectangularGridGraph(50, 6, 2, 0, 0);
		// AccessibilityGraphImpl graph2 = GraphFactory
		// .createRectangularGridGraph(6, 10, 2, 22, 8);
		// AccessibilityGraphImpl graph =
		// GraphFactory.createRectangularGridGraph(
		// 50, 50, 2, 0, 0);
		//
		// GraphFactory.removeLocations(graph, new Rectangle2D.Double(10, 10,
		// 31,
		// 31));
		//
		// graph.write("src/main/resources/graphs/inaccessible.txt");

		// AccessibilityGraphImpl graph = GraphFactory
		// .connect(graph1, graph2, 2.0);
		// graph = GraphFactory.connect(graph, graph3, 2.0);
		//
		// graph.write("src/main/resources/graphs/corridor_and_room.txt");

		AccessibilityGraphImpl graph = AccessibilityGraphIO.readGraph(new File(
				"src/main/resources/graphs/building32.txt"));

		// graph.addAccessibilityRelations(8.5);

		// List<LocationImpl> locations = graph.getLocations();
		//
		// for (int i = 0; i < locations.size(); i++) {
		// for (int j = i + 1; j < locations.size(); j++) {
		// LocationImpl location1 = locations.get(i);
		// LocationImpl location2 = locations.get(j);
		//
		// if (!location1.equals(location2)
		// && location1.directDistance(location2) < 8.5) {
		// graph.addAccessibilityRelation(location1, location2);
		// }
		// }
		// }
		//
		new GraphGUI(graph);

		System.out.println(graph.getBoundingBox());

		// graph.write(new File(
		// "src/main/resources/graphs/intel-berkeley-connected.txt"));
	}
}
