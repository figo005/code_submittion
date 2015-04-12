package uk.ac.soton.ecs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.mutable.MutableDouble;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class GraphUtils {

	public static <V, E> boolean isConnected(Graph<V, E> graph) {

		BFSDistanceLabeler<V, E> labeler = new BFSDistanceLabeler<V, E>();

		labeler.labelDistances(graph, graph.getVertices().iterator().next());

		return labeler.getUnvisitedVertices().isEmpty();
	}

	public static <V, E> boolean isBipartite(Graph<V, E> graph) {

		Set<Set<V>> transform = new WeakComponentClusterer<V, E>()
				.transform(graph);

		for (Set<V> component : transform) {
			Map<V, Boolean> setAssignment = new HashMap<V, Boolean>();

			if (!isBipartite(component.iterator().next(), graph, setAssignment,
					true)) {
				return false;
			}
		}

		return true;
	}

	private static <V, E> boolean isBipartite(V vertex, Graph<V, E> graph,
			Map<V, Boolean> setAssignment, boolean inSet1) {
		// vertex is assigned to a set
		if (setAssignment.containsKey(vertex)) {
			if (setAssignment.get(vertex) == inSet1)
				return true;
			else
				return false;
		}

		setAssignment.put(vertex, inSet1);

		for (V neighbour : graph.getNeighbors(vertex)) {
			if (!isBipartite(neighbour, graph, setAssignment, !inSet1)) {
				return false;
			}
		}

		return true;
	}

	public static <V, E> Map<V, Integer> getColoring(Graph<V, E> graph,
			int maxColors) {
		HashMap<V, Integer> coloring = new HashMap<V, Integer>();

		for (Set<V> component : new WeakComponentClusterer<V, E>()
				.transform(graph)) {
			if (!isColorable(RandomUtils.getRandomElement(component), graph,
					coloring, maxColors))
				return null;
		}

		Validate.isTrue(countConflicts(graph, coloring) == 0);

		return coloring;
	}

	public static <V, E> boolean isColorable(Graph<V, E> graph, int maxColors) {
		return getColoring(graph, maxColors) != null;
	}

	public static <V, E> boolean isColorable(V firstElement, Graph<V, E> graph,
			Map<V, Integer> coloring, int maxColors) {
		Validate.isTrue(!coloring.containsKey(firstElement));
		Queue<V> queue = new LinkedList<V>();
		queue.offer(firstElement);

		while (!queue.isEmpty()) {
			V element = queue.poll();

			Integer color = getValidColor(element, graph, coloring, maxColors);

			if (color == null)
				return false;

			coloring.put(element, color);

			for (V neighbour : graph.getNeighbors(element)) {
				if (!coloring.containsKey(neighbour))
					queue.offer(neighbour);
			}
		}

		return true;
	}

	public static <V, E> Integer getValidColor(V element, Graph<V, E> graph,
			Map<V, Integer> coloring, int maxColors) {

		Set<Integer> impossibleColors = new HashSet<Integer>();

		for (V neighbour : graph.getNeighbors(element)) {
			if (coloring.containsKey(neighbour)) {
				impossibleColors.add(coloring.get(neighbour));
			}
		}

		for (int i = 0; i < maxColors; i++) {
			if (!impossibleColors.contains(i)) {
				return i;
			}
		}

		return null;
	}

	public static <V, E> Map<V, Integer> getDSAColoring(Graph<V, E> graph,
			int maxColors) {
		return getDSAColoring(graph, maxColors, 50, 0.05, false);
	}

	public static <V, E> Map<V, Integer> getDSAColoring(Graph<V, E> graph,
			int maxColors, int maxIterations, double epsilon, boolean epsilonN) {
		return getDSAColoring(graph, maxColors, maxIterations,
				new MutableDouble(), epsilon, epsilonN);
	}

	public static <V, E> double getDSAColoringIterations(Graph<V, E> graph,
			int maxColors, int maxIterations, double epsilon, boolean epsilonN) {
		MutableDouble iterations = new MutableDouble();
		getDSAColoring(graph, maxColors, maxIterations, iterations, epsilon,
				epsilonN);
		return iterations.doubleValue();
	}

	private static <V, E> Map<V, Integer> getDSAColoring(Graph<V, E> graph,
			int maxColors, int maxIterations, MutableDouble iterations,
			double epsilon, boolean epsilonN) {
		Map<V, Integer> coloring = new HashMap<V, Integer>();

		if (graph.getVertexCount() == 0) {
			iterations = new MutableDouble(0.0);
			return coloring;
		}

		Validate.isTrue(maxColors >= 1);

		Random random = new Random();

		// initialise with random colors
		for (V element : graph.getVertices()) {
			coloring.put(element, random.nextInt(maxColors));
		}

		for (int i = 0; i < maxIterations; i++) {
			for (V randomElement : graph.getVertices()) {

				Integer bestColor = null;

				double exploreProbability = epsilonN ? epsilon / i : epsilon;

				if (random.nextDouble() < exploreProbability)
					bestColor = random.nextInt(maxColors);
				else {
					// get the color with the least number of conflicts

					// count conflicts
					Bag<Integer> colors = new HashBag<Integer>();
					for (V neighbour : graph.getNeighbors(randomElement)) {
						colors.add(coloring.get(neighbour));
					}

					int minConflicts = Integer.MAX_VALUE;

					List<Integer> randomPermutation = new ArrayList<Integer>();
					for (int c = 0; c < maxColors; c++) {
						randomPermutation.add(c);
					}

					Collections.shuffle(randomPermutation);

					for (int c : randomPermutation) {
						if (colors.getCount(c) < minConflicts) {
							bestColor = c;
							minConflicts = colors.getCount(c);
						}
					}
				}

				coloring.put(randomElement, bestColor);

				// System.out.println(countConflicts(graph, coloring));
				// System.out.println(coloring);

				if (countConflicts(graph, coloring) == 0) {

					iterations.setValue(i);
					return coloring;
				}
			}
		}

		iterations.setValue(-1.0);
		return coloring;
	}

	public static <V, E> int countConflicts(Graph<V, E> graph,
			Map<V, Integer> coloring) {
		int conflicts = 0;
		for (E edge : graph.getEdges()) {
			Pair<V> endpoints = graph.getEndpoints(edge);

			Integer color1 = coloring.get(endpoints.getFirst());
			Integer color2 = coloring.get(endpoints.getSecond());

			conflicts += (color1 == color2) ? 1 : 0;
		}

		return conflicts;
	}

	public static <V, E> UndirectedGraph<V, E> getSubgraph(
			UndirectedGraph<V, E> graph, Collection<V> elements) {
		UndirectedSparseGraph<V, E> result = new UndirectedSparseGraph<V, E>();

		// for (V vertex : elements) {
		// result.addVertex(vertex);
		// }

		for (E edge : graph.getEdges()) {
			Pair<V> endpoints = graph.getEndpoints(edge);

			if (elements.contains(endpoints.getFirst())
					&& elements.contains(endpoints.getSecond()))
				result.addEdge(edge, endpoints);
		}

		return result;
	}
}
