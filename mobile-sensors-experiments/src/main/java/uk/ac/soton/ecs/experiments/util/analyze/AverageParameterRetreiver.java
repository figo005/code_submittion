package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public class AverageParameterRetreiver extends MultiValuedParameterRetreiver {

	public AverageParameterRetreiver(ColumnName columnName) {
		super(columnName);
	}

	@Override
	protected double computeValue(List<Double> values) {
		double sum = 0;
		double count = 0;

		for (Double value : values) {
			sum += value;
			count++;
		}

		return sum / count;
	}

	@Override
	protected String getParameterPostFix() {
		return "_avg";
	}

}
