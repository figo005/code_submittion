package uk.ac.soton.ecs.mobilesensors.layout;

import org.springframework.beans.factory.InitializingBean;

public class RiskGraphGridAdaptor extends GeneralGrid implements InitializingBean {

	private AccessibilityRiskGraphImpl graph;

	public void setGraph(AccessibilityRiskGraphImpl graph) {
		this.graph = graph;

	}

	public void afterPropertiesSet() throws Exception {
		for (Location location : graph) {
			gridPoints.add(location.getCoordinates());
		}

		initialize();
	}
}
