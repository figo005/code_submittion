package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ActualObservationValueHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.OBSERVATION_VALUE;
	}

	public String handle(File directory) {
		try {
			int sensorCount = Integer.parseInt(FileUtils
					.readFileToString(new File(directory, "sensor_count.txt")));

			String result = "";

			for (int i = 0; i < sensorCount; i++) {
				result += FileUtils
						.readLines(
								new File(directory, "sensor" + i
										+ "/observation_value")).get(0)
						+ " ";
			}

			return result;
		} catch (IOException e) {
			return null;
		}
	}
}
