package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.Arrays;
import java.util.List;

public class GraphParameter {

	private List<String> paramNames;
	private String yAxisName;
	private String title;
	private String shortDescription;
	private boolean yAxisLogarithmic;

	public GraphParameter(String title, String shortDescription,
			String yAxisName, String... paramNames) {
		this(title, shortDescription, yAxisName, false, paramNames);
	}

	public GraphParameter(String title, String shortDescription,
			String yAxisName, boolean yAxisLogarithmic, String... paramNames) {
		this.title = title;
		this.yAxisName = yAxisName;
		this.shortDescription = shortDescription;
		this.paramNames = Arrays.asList(paramNames);
		this.yAxisLogarithmic = yAxisLogarithmic;
	}

	public List<String> getParamNames() {
		return paramNames;
	}

	public String getGetShortDescription() {
		return shortDescription;
	}

	public String getTitle() {
		return title;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public boolean isYAxisLogarithmic() {
		return yAxisLogarithmic;
	}

}
