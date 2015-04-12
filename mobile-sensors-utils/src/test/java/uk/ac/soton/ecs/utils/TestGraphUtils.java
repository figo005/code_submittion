package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.lang.math.RandomUtils;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class TestGraphUtils extends TestCase {

	private UndirectedSparseGraph<String, String> disconnectedGraph;

	@Override
	protected void setUp() throws Exception {
		disconnectedGraph = new UndirectedSparseGraph<String, String>();

		disconnectedGraph.addVertex("a");
		disconnectedGraph.addVertex("b");
	}

	public void testIsConnected1() throws Exception {
		assertFalse(GraphUtils.isConnected(disconnectedGraph));
	}

	public void testIsConnected2() throws Exception {
		disconnectedGraph.addEdge("", "a", "b");

		assertTrue(GraphUtils.isConnected(disconnectedGraph));
	}

	public void testIsBipartite() throws Exception {
		assertTrue(GraphUtils.isBipartite(disconnectedGraph));

		disconnectedGraph.addEdge("ab", "a", "b");
		assertTrue(GraphUtils.isColorable(disconnectedGraph, 2));
		assertTrue(GraphUtils.isBipartite(disconnectedGraph));

		disconnectedGraph.addVertex("c");
		disconnectedGraph.addEdge("ac", "a", "c");
		assertTrue(GraphUtils.isColorable(disconnectedGraph, 2));
		assertTrue(GraphUtils.isBipartite(disconnectedGraph));

		disconnectedGraph.addEdge("bc", "b", "c");
		assertFalse(GraphUtils.isColorable(disconnectedGraph, 2));
		assertFalse(GraphUtils.isBipartite(disconnectedGraph));
	}

	public void testIsBipartiteRandomizedGraphs() throws Exception {

		for (int i = 0; i < 25; i++) {
			List<Integer> verticesA = new ArrayList<Integer>();
			List<Integer> verticesB = new ArrayList<Integer>();

			UndirectedSparseGraph<Integer, Integer> graph = createRandomBipartiteGraph(
					verticesA, verticesB);

			assertTrue(GraphUtils.isColorable(graph, 2));
			assertTrue(GraphUtils.isBipartite(graph));

			Integer vertexA1 = verticesA.get(new Random().nextInt(verticesA
					.size()));
			Integer vertexA2 = verticesA.get(new Random().nextInt(verticesA
					.size()));
			Integer vertexB1 = verticesB.get(new Random().nextInt(verticesB
					.size()));

			if (!vertexA1.equals(vertexA2)) {
				graph.addEdge(RandomUtils.nextInt(), vertexA1, vertexA2);
				graph.addEdge(RandomUtils.nextInt(), vertexB1, vertexA1);
				graph.addEdge(RandomUtils.nextInt(), vertexB1, vertexA2);

				assertFalse(GraphUtils.isBipartite(graph));
				assertFalse(GraphUtils.isColorable(graph, 2));
			}

			// System.out.println(graph);
		}
	}

	public void testIsBipartiteRandomizedTwoComponentGraphs() throws Exception {

		for (int i = 0; i < 25; i++) {

			UndirectedSparseGraph<Integer, Integer> graph1 = createRandomBipartiteGraph(
					new ArrayList<Integer>(), new ArrayList<Integer>());

			List<Integer> verticesA = new ArrayList<Integer>();
			List<Integer> verticesB = new ArrayList<Integer>();
			UndirectedSparseGraph<Integer, Integer> graph2 = createRandomBipartiteGraph(
					verticesA, verticesB);

			UndirectedSparseGraph<Integer, Integer> graph = mergeGraphs(graph1,
					graph2);

			assertTrue(GraphUtils.isBipartite(graph));
			assertTrue(GraphUtils.isColorable(graph, 2));

			Integer vertexA1 = verticesA.get(new Random().nextInt(verticesA
					.size()));
			Integer vertexA2 = verticesA.get(new Random().nextInt(verticesA
					.size()));
			Integer vertexB1 = verticesB.get(new Random().nextInt(verticesB
					.size()));

			if (!vertexA1.equals(vertexA2)) {
				graph.addEdge(RandomUtils.nextInt(), vertexA1, vertexA2);
				graph.addEdge(RandomUtils.nextInt(), vertexB1, vertexA1);
				graph.addEdge(RandomUtils.nextInt(), vertexB1, vertexA2);

				assertFalse(GraphUtils.isBipartite(graph));
				assertFalse(GraphUtils.isColorable(graph, 2));
			}
		}
	}

	private UndirectedSparseGraph<Integer, Integer> mergeGraphs(
			UndirectedSparseGraph<Integer, Integer> graph1,
			UndirectedSparseGraph<Integer, Integer> graph2) {
		UndirectedSparseGraph<Integer, Integer> result = new UndirectedSparseGraph<Integer, Integer>();

		for (Integer vertex : graph1.getVertices()) {
			result.addVertex(vertex);
		}
		for (Integer vertex : graph2.getVertices()) {
			result.addVertex(vertex);
		}

		for (Integer edge : graph1.getEdges()) {
			result.addEdge(edge, graph1.getIncidentVertices(edge));
		}
		for (Integer edge : graph2.getEdges()) {
			result.addEdge(edge, graph2.getIncidentVertices(edge));
		}

		return result;
	}

	private UndirectedSparseGraph<Integer, Integer> createRandomBipartiteGraph(
			List<Integer> verticesA, List<Integer> verticesB) {
		UndirectedSparseGraph<Integer, Integer> graph = new UndirectedSparseGraph<Integer, Integer>();
		for (int i = 0; i < 10; i++) {
			Integer name = RandomUtils.nextInt();
			graph.addVertex(name);
			verticesA.add(name);
		}

		for (int j = 0; j < 10; j++) {
			Integer name = RandomUtils.nextInt();
			graph.addVertex(name);
			verticesB.add(name);
		}

		Random random = new Random();

		// connect random elements with eachother
		for (int i = 0; i < 20; i++) {
			Integer vertexA = verticesA.get(random.nextInt(verticesA.size()));
			Integer vertexB = verticesB.get(random.nextInt(verticesB.size()));

			Integer edgeName = vertexA + vertexB;

			if (!graph.containsEdge(edgeName)) {
				graph.addEdge(edgeName, vertexA, vertexB);
			}
		}

		return graph;
	}
}
