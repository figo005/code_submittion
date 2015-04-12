package uk.ac.soton.ecs.experiments.util.analyze;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.ColumnName;
import uk.ac.soton.ecs.experiments.util.dbimporter.DBUtils;
import uk.ac.soton.ecs.experiments.util.dbimporter.ResultsDBImporter;

public class BoundedMaxSumExperiments implements ExperimentResultSet {

	public static void main(String[] args) throws Exception {

		boolean outputFromDBOnly = true;
		if (!outputFromDBOnly) {
			String mainDirectory = "/media/iridis/";

			Connection connection = DBUtils.openConnection();

			ResultsDBImporter resultsDBImporter = new ResultsDBImporter(
					connection, mainDirectory);
			resultsDBImporter.truncate();

			resultsDBImporter.traverseDirectory();
		}
		new HTMLResultsPageOutputter("/media/login/public_html/results/")
				.output(new BoundedMaxSumExperiments());

		new MatlabTableOutputter("/home/rs06r/results")
				.output(new BoundedMaxSumExperiments());
	}

	public Collection<ExperimentParameterRetreiver> getParameterRetreivers() {
		Collection<ExperimentParameterRetreiver> result = new ArrayList<ExperimentParameterRetreiver>();

		result.add(new CountParameterRetreiver(ColumnName.ID));
		result
				.add(new ArbitraryValueParameterRetreiver(
						ColumnName.SENSOR_COUNT));

		for (ColumnName name : getDefaultColumnNames()) {
			result.add(new AverageParameterRetreiver(name));
			result.add(new StandardErrorInMeanParameterRetreiver(name));
		}

		return result;
	}

	private Collection<ColumnName> getDefaultColumnNames() {
		Collection<ColumnName> result = new ArrayList<ColumnName>();
		result.add(ColumnName.AVERAGE_RMS);
		result.add(ColumnName.BOUNDED_MS_APPROX_RATIO);
		result.add(ColumnName.BOUNDED_MS_FACTOR_GRAPH_VALUE);
		result.add(ColumnName.BOUNDED_MS_OPTIMAL_VALUE);
		result.add(ColumnName.BOUNDED_MS_TREE_VALUE);
		result.add(ColumnName.BOUNDED_MS_UPPER_BOUND);
		result.add(ColumnName.EXPANDED_PARTIAL_NODES);
		result.add(ColumnName.TOTAL_PARTIAL_NODES);
		result.add(ColumnName.FUNCTION_CALLS);
		result.add(ColumnName.CACHE_MISSES);
		result.add(ColumnName.MESSAGE_COUNT);
		result.add(ColumnName.MESSAGE_SIZE);

		return result;
	}

	public List<GraphParameter> getGraphParameters() {
		List<GraphParameter> result = new ArrayList<GraphParameter>();

		result.add(new GraphParameter("Bounded MS Statistics",
				"bounded_ms_stats", "Utility", ColumnName.BOUNDED_MS_TREE_VALUE
						+ "_avg", ColumnName.BOUNDED_MS_FACTOR_GRAPH_VALUE
						+ "_avg", ColumnName.BOUNDED_MS_OPTIMAL_VALUE + "_avg",
				ColumnName.BOUNDED_MS_UPPER_BOUND + "_avg"));

		result.add(new GraphParameter(
				"Branch and Bound Statistics (Pruning Technique 1)",
				"bb_stats", "Number", true, ColumnName.CACHE_MISSES + "_avg",
				ColumnName.FUNCTION_CALLS + "_avg",
				ColumnName.EXPANDED_PARTIAL_NODES + "_avg",
				ColumnName.TOTAL_PARTIAL_NODES + "_avg"));

		result.add(new GraphParameter("Solution Quality", "solution_quality",
				"RMSE", ColumnName.AVERAGE_RMS + "_avg"));

		result.add(new GraphParameter("Message Size", "message_size",
				"Total Number of Values", ColumnName.MESSAGE_SIZE + "_avg"));

		return result;
	}

	public List<TableParameter> getTableParameters() {
		List<TableParameter> result = new ArrayList<TableParameter>();

		result.add(new TableParameter("bounded_ms_stats",
				ColumnName.BOUNDED_MS_TREE_VALUE,
				ColumnName.BOUNDED_MS_FACTOR_GRAPH_VALUE,
				ColumnName.BOUNDED_MS_OPTIMAL_VALUE,
				ColumnName.BOUNDED_MS_UPPER_BOUND));

		result.add(new TableParameter("bb_stats", ColumnName.CACHE_MISSES,
				ColumnName.FUNCTION_CALLS, ColumnName.EXPANDED_PARTIAL_NODES,
				ColumnName.TOTAL_PARTIAL_NODES));

		result.add(new TableParameter("rmse", ColumnName.AVERAGE_RMS));

		result.add(new TableParameter("message_size", ColumnName.MESSAGE_SIZE));

		return result;
	}
}
