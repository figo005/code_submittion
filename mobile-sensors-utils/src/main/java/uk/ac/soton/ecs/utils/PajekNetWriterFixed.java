package uk.ac.soton.ecs.utils;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetWriter;

public class PajekNetWriterFixed<V, E> extends PajekNetWriter<V, E> {

	public void save(Graph<V, E> graph, Writer w, Transformer<V, String> vs,
			Transformer<E, Number> nev, Transformer<V, Point2D> vld)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(w);
		if (nev == null)
			nev = new Transformer<E, Number>() {
				public Number transform(E e) {
					return 1;
				}
			};
		writer.write("*Vertices " + graph.getVertexCount());
		writer.newLine();

		List<V> id = new ArrayList<V>(graph.getVertices());
		for (V currentVertex : graph.getVertices()) {
			// convert from 0-based to 1-based index
			int v_id = id.indexOf(currentVertex) + 1;
			writer.write("" + v_id);
			if (vs != null) {
				String label = vs.transform(currentVertex);
				if (label != null)
					writer.write(" \"" + label + "\"");
			}
			if (vld != null) {
				Point2D location = vld.transform(currentVertex);
				if (location != null)
					writer.write(" " + location.getX() + " " + location.getY());
			}
			writer.newLine();
		}

		Collection<E> d_set = new HashSet<E>();
		Collection<E> u_set = new HashSet<E>();

		boolean directed = graph instanceof DirectedGraph;

		boolean undirected = graph instanceof UndirectedGraph;

		// if it's strictly one or the other, no need to create extra sets
		if (directed)
			d_set.addAll(graph.getEdges());
		if (undirected)
			u_set.addAll(graph.getEdges());
		if (!directed && !undirected) // mixed-mode graph
		{
			u_set.addAll(graph.getEdges());
			d_set.addAll(graph.getEdges());
			for (E e : graph.getEdges()) {
				if (graph.getEdgeType(e) == EdgeType.UNDIRECTED) {
					d_set.remove(e);
				} else {
					u_set.remove(e);
				}
			}
		}

		// write out directed edges
		if (!d_set.isEmpty()) {
			writer.write("*Arcs");
			writer.newLine();
		}
		for (E e : d_set) {
			int source_id = id.indexOf(graph.getEndpoints(e).getFirst()) + 1;
			int target_id = id.indexOf(graph.getEndpoints(e).getSecond()) + 1;
			// float weight = nev.get(e).floatValue();
			float weight = nev.transform(e).floatValue();
			writer.write(source_id + " " + target_id + " " + weight);
			writer.newLine();
		}

		// write out undirected edges
		if (!u_set.isEmpty()) {
			writer.write("*Edges");
			writer.newLine();
		}
		for (E e : u_set) {
			Pair<V> endpoints = graph.getEndpoints(e);
			int v1_id = id.indexOf(endpoints.getFirst()) + 1;
			int v2_id = id.indexOf(endpoints.getSecond()) + 1;
			// float weight = nev.get(e).floatValue();
			float weight = nev.transform(e).floatValue();
			writer.write(v1_id + " " + v2_id + " " + weight);
			writer.newLine();
		}
		writer.close();
	}
}
