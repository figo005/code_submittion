package uk.ac.soton.ecs.experiments.util.analyze;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.ac.soton.ecs.experiments.util.dbimporter.DBUtils;

public class ExperimentResultCollector {

	private static Collection<ExperimentParameterRetreiver> retreivers = new ArrayList<ExperimentParameterRetreiver>();

	public static Collection<ExperimentalResults> getExperimentGroups(
			ExperimentResultSet resultSet) throws Exception {

		Connection connection = DBUtils.openConnection();

		retreivers.addAll(resultSet.getParameterRetreivers());

		for (ExperimentParameterRetreiver retreiver : retreivers) {
			retreiver.setConnection(connection);
		}

		List<ExperimentalResults> experimentGroups = getExperimentGroups(connection);

		Collections.sort(experimentGroups);

		return experimentGroups;
	}

	private static List<ExperimentalResults> getExperimentGroups(
			Connection connection) throws SQLException {
		List<ExperimentalResults> experiment = new ArrayList<ExperimentalResults>();

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT * FROM experiments.experiment_set");

		while (resultSet.next()) {
			ExperimentalResults results = new ExperimentalResults(resultSet
					.getInt("id"), resultSet.getString("name"));

			for (ExperimentParameterRetreiver retreiver : retreivers) {
				results.setParameter(retreiver.getParameterName(), retreiver
						.getParameterValue(results));
			}

			experiment.add(results);
		}

		return experiment;
	}
}
