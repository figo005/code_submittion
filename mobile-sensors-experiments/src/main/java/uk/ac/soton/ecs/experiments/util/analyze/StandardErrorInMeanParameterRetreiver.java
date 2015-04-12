package uk.ac.soton.ecs.experiments.util.analyze;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.StatUtils;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;

public class StandardErrorInMeanParameterRetreiver extends
		MultiValuedParameterRetreiver {

	public StandardErrorInMeanParameterRetreiver(ColumnName columnName) {
		super(columnName);
	}

	@Override
	protected double computeValue(List<Double> values) {
		Double[] array = values.toArray(new Double[] {});
		double[] primitive = ArrayUtils.toPrimitive(array);

		double variance = StatUtils.variance(primitive);
		double standardDeviation = Math.sqrt(variance);

		double standardErrorInMean = standardDeviation
				/ Math.sqrt(values.size());

		return standardErrorInMean;
	}

	@Override
	protected String getParameterPostFix() {
		return "_std_err_of_mean";
	}

}
