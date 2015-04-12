package uk.ac.soton.ecs.experiments.util.non_myopic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import uk.ac.soton.ecs.experiments.util.dbimporter.DBUtils;

public class NonMyopicCoordinateRewardResultsAnalyser {

	public final int experimentGroupID = 12;

	public static void main(String[] args) throws Exception {

		Connection connection = DBUtils.openConnection();

		String query = "SELECT tau, gamma, experiment_set.id AS id, name, "
				+ "sensor_count, uncertainty_increment, expected_reward, actual_reward "
				+ "FROM experiment_set, experiment_result WHERE tau = 200 and "
				+ "experiment_set.id=experiment_result.experiment_set_id AND experiment_set.experiment_group_id = 19 "
				+ "ORDER by sensor_count, id";

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery(query);

		System.out.println("START");

		BidiMap<String, Integer> names = new DualHashBidiMap<String, Integer>();

		while (result.next()) {
			int id = result.getInt("id");
			int tau = result.getInt("tau");
			double gamma = result.getInt("gamma");
			int sensorCount = result.getInt("sensor_count");

			double actualReward = sum(result.getString("actual_reward"));
			double expectedReward = sum(result.getString("expected_reward"));

			String name = result.getString("name");
			name = name.substring(0, name.length() - 13);
			double uncertaintyIncrement = result
					.getDouble("uncertainty_increment");

			int maxId = names.isEmpty() ? 0 : Collections.max(names.values());

			if (!names.keySet().contains(name))
				names.put(name, maxId + 1);

			id = names.get(name);

			System.out.println(String.format("%3d %3d %4d %10f %10f %10f",
					sensorCount, id, tau, uncertaintyIncrement, actualReward,
					expectedReward));
		}

		System.out.println();

		for (int i = 0; i < names.size(); i++) {
			System.out.println((i + 1) + " "
					+ names.inverseBidiMap().get(i + 1));
		}

		statement.close();

		// write(captureTimes, ids);
	}

	private static double sum(String string) {
		if (string == null)
			return 0;

		double result = 0;
		for (String s : string.split(" ")) {
			result += Double.parseDouble(s);
		}
		return result;
	}
}
