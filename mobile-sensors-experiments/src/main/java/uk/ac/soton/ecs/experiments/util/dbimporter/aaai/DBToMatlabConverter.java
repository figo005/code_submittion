package uk.ac.soton.ecs.experiments.util.dbimporter.aaai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.experiments.util.dbimporter.DBUtils;

public class DBToMatlabConverter {

	private static boolean showAll = true;

	static String value = "capturetime"; // patrollingloss

	public static void main(String[] args) throws Exception {
		Connection connection = DBUtils.openConnection();

		String query = "SELECT seed, name, experiment_set.id AS id, capturetime FROM experiment_set, experiment_result WHERE experiment_set.id=experiment_result.experiment_set_id";

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery(query);

		// seed -> id -> capturetime
		Map<Integer, Map<Integer, Integer>> captureTimes = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, String> ids = new HashMap<Integer, String>();

		while (result.next()) {
			int seed = result.getInt("seed");
			int id = result.getInt("id");
			int time = result.getInt("capturetime");
			String name = result.getString("name");
			name = name.replaceAll("sensor_", "");
			name = name.replaceAll("_pursuit_b32_4sensors", "");

			// if (time > 400)
			// time = 400;

			ids.put(id, name);

			if (!captureTimes.containsKey(seed)) {
				captureTimes.put(seed, new HashMap<Integer, Integer>());
			}

			Validate.isTrue(!captureTimes.get(seed).containsKey(ids));

			captureTimes.get(seed).put(id, time);
		}

		statement.close();

		write(captureTimes, ids);
	}

	private static void write(Map<Integer, Map<Integer, Integer>> captureTimes,
			Map<Integer, String> ids) {
		writeHeader(ids);

		System.out.println();

		writeData(captureTimes, ids);

		System.out.println();

		System.out.println("boxplot(data, 'labels', legend);");
		System.out.println("means = repmat(mean(data), size(data, 1), 1);");
		System.out.println("stds = repmat(std(data), size(data, 1), 1);");
		System.out.println("z = (data - means) ./ stds;");

		for (int i = 1; i < ids.size(); i++) {

		}
	}

	private static void writeData(
			Map<Integer, Map<Integer, Integer>> captureTimes,
			Map<Integer, String> ids) {
		System.out.println("data = [");

		int failed = 0;

		for (int seed : captureTimes.keySet()) {
			Map<Integer, Integer> results = captureTimes.get(seed);

			if (showAll || results.keySet().containsAll(ids.keySet())) {
				for (Integer id : ids.keySet()) {
					Integer value = results.get(id);
					if (value == null)
						System.out.print(String.format("%7s", "NaN"));
					else
						System.out.print(String.format("%7d", value));
				}
				System.out.println();
			} else {
				failed++;
			}

		}

		// System.out.println(failed + " failed " + " out of "
		// + captureTimes.size());

		System.out.println("];");
	}

	private static void writeHeader(Map<Integer, String> ids) {
		System.out.print("legend = {");

		boolean first = true;
		for (Integer id : ids.keySet()) {
			if (!first)
				System.out.print(", ");

			System.out.print("'" + ids.get(id) + "'");
			first = false;
		}
		System.out.println("};");
	}
}
