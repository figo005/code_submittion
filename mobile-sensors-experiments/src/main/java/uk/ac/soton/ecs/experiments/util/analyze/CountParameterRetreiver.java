package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

/**
 * Counts the number of results
 * 
 * @author rs06r
 * 
 */

public class CountParameterRetreiver extends MultiValuedParameterRetreiver {

	public CountParameterRetreiver(ColumnName columnName) {
		super(columnName);
	}

	@Override
	protected double computeValue(List<Double> values) {
		return values.size();
	}

	@Override
	protected String getParameterPostFix() {
		return "_count";
	}
}
