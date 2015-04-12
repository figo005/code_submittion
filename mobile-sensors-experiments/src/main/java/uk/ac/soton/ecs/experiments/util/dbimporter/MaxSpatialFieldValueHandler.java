package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class MaxSpatialFieldValueHandler implements ConfigurationValueHandler {

	private static final String MAX_FIELD_TXT = "spatialFieldValues.txt_max";

	public ColumnName getColumnName() {
		return ColumnName.MAX_FIELD_VALUE;
	}

	public String handle(File directory) {
		File averageRMS = new File(directory, MAX_FIELD_TXT);

		if (!averageRMS.exists())
			return null;

		try {
			List<String> lines = FileUtils.readLines(averageRMS);

			int count = 0;
			double rmsSum = 0.0;

			for (String line : lines) {
				if (!line.startsWith("%")) {
					Scanner scanner = new Scanner(line);
					scanner.nextDouble();
					rmsSum += scanner.nextDouble();
					count++;
				}
			}

			return "" + (rmsSum /= count);

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

}
