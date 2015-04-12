package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class SensorCountValueHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.SENSOR_COUNT;
	}

	public String handle(File directory) {
		try {
			return FileUtils.readFileToString(new File(directory,
					"sensor_count.txt"));
		} catch (Exception e) {
			System.err.println("sensor_count not defined");
		}

		return null;
	}

}
