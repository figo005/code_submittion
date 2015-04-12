package uk.ac.soton.ecs.mobilesensors.layout;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

public class GraphGridAdaptor extends GeneralGrid implements InitializingBean {

	private AccessibilityGraphImpl graph;

	public void setGraph(AccessibilityGraphImpl graph) {
		this.graph = graph;

	}

	public void afterPropertiesSet() throws Exception {
		for (Location location : graph) {
			gridPoints.add(location.getCoordinates());
		}

		initialize();
	}
	
	public Collection<Location> getRiskLocations() {
		return graph.getRiskLoactions();

	}
	
}
