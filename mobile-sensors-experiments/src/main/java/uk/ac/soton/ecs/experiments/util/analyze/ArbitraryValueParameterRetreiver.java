package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public class ArbitraryValueParameterRetreiver extends
		MultiValuedParameterRetreiver {

	public ArbitraryValueParameterRetreiver(ColumnName columnName) {
		super(columnName);
	}

	@Override
	protected double computeValue(List<Double> values) {
		if (values.isEmpty()) {
			return Double.NaN;
		}

		return values.get(0);
	}

	@Override
	protected String getParameterPostFix() {
		return "";
	}
}
