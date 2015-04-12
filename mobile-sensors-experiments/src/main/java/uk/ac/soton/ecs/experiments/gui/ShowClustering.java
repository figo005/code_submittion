package uk.ac.soton.ecs.experiments.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import uk.ac.soton.ecs.mobilesensors.configuration.AccessibilityGraphIO;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.ClusterGraphGUI;
import uk.ac.soton.ecs.mobilesensors.layout.ClusteredGraph;
import uk.ac.soton.ecs.mobilesensors.layout.GraphGridAdaptor;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.layout.Node;
import uk.ac.soton.ecs.mobilesensors.layout.TransitNode;
import uk.ac.soton.ecs.mobilesensors.layout.clustering.ClusterResult;
import uk.ac.soton.ecs.mobilesensors.layout.clustering.MinimiseMetricClusterer;
import uk.ac.soton.ecs.mobilesensors.layout.gui.GraphGUI;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.IntraClusterPatrollingStrategy;
import uk.ac.soton.ecs.mobilesensors.worldmodel.disk.DiskInformativenessFunction;

public class ShowClustering {
	public static void main(String[] args) throws Exception {
		AccessibilityGraphImpl graph = AccessibilityGraphIO.readGraph(new File(
				"src/main/resources/graphs/building32.txt"));

		MinimiseMetricClusterer clusterer = new MinimiseMetricClusterer();
		ClusterResult<Location> clustering = clusterer.clusterBiggest(graph, 6);
		ClusteredGraph<Location, AccessibilityRelation> clusteredGraph = new ClusteredGraph<Location, AccessibilityRelation>(
				graph, clustering);

		GraphGUI graphGUI = new GraphGUI(graph, clusteredGraph);
		ClusterGraphGUI.show(clusteredGraph, graphGUI.getClusterColorMap());

		GraphGridAdaptor graphGridAdaptor = new GraphGridAdaptor();
		graphGridAdaptor.setGraph(graph);
		graphGridAdaptor.afterPropertiesSet();

		DiskInformativenessFunction function = new DiskInformativenessFunction(
				graphGridAdaptor, 50, 0.005);

		IntraClusterPatrollingStrategy patrollingStrategy = new IntraClusterPatrollingStrategy(
				50, graph, function);
		IntraClusterPatrollingStrategy.MAX_LENGTH = 12;

		List<Cluster<Location>> clusters = clusteredGraph.getClusters();

		List<List<Location>> paths = new ArrayList<List<Location>>();

		for (Cluster<Location> cluster : clusters) {
			List<Node<Location>> neighbors = new ArrayList<Node<Location>>(
					clusteredGraph.getNeighbors(cluster));

			for (int i = 0; i < 2; i++) {
				Node<Location> node1 = neighbors.get(RandomUtils
						.nextInt(neighbors.size()));
				Node<Location> node2 = neighbors.get(RandomUtils
						.nextInt(neighbors.size()));

				Location location1 = ((TransitNode<Location>) node1)
						.getRepresentativeVertex(cluster);
				Location location2 = ((TransitNode<Location>) node2)
						.getRepresentativeVertex(cluster);

				List<Location> intraClusterPath = patrollingStrategy
						.getIntraClusterPath(cluster, location1, location2, 0);

				paths.add(intraClusterPath);
			}
		}

		// int[] indices = { 3, 7, 11, 15, 19, 23, 28, 29, 36, 41 };
		for (int i = 0; i < paths.size(); i++) {
			graphGUI.setPath(paths.get(i));
			Thread.sleep(1000);
		}

	}
}
