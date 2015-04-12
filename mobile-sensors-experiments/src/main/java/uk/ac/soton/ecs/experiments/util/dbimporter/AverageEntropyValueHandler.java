package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class AverageEntropyValueHandler implements ConfigurationValueHandler {

	private static final String AVERAGE_ENTROPY_TXT = "spatialFieldValues.txt_entropy";

	public ColumnName getColumnName() {
		return ColumnName.AVERAGE_ENTROPY;
	}

	public String handle(File directory) {
		File averageRMS = new File(directory, AVERAGE_ENTROPY_TXT);

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

			if (Double.isNaN(rmsSum))
				return null;

			return "" + (rmsSum / count);

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

}
