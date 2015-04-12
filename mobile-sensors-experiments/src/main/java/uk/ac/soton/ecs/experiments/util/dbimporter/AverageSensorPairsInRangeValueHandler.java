package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class AverageSensorPairsInRangeValueHandler implements
		ConfigurationValueHandler {

	private static final String FILE = "sensor_pairs_in_range.txt";

	public ColumnName getColumnName() {
		return ColumnName.AVERAGE_SENSOR_PAIRS_IN_RANGE;
	}

	public String handle(File directory) {
		File file = new File(directory, FILE);

		if (!file.exists())
			return null;

		try {
			List<String> lines = FileUtils.readLines(file);

			int count = 0;
			double sum = 0.0;

			for (String line : lines) {
				if (!line.startsWith("%")) {
					Scanner scanner = new Scanner(line);
					scanner.nextDouble();
					sum += scanner.nextDouble();
					count++;
				}
			}

			return "" + (sum /= count);

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}

	// public static void main(String[] args) {
	// AverageSensorPairsInRangeValueHandler averageRMSValueHandler = new
	// AverageSensorPairsInRangeValueHandler();
	//
	// System.out
	// .println(averageRMSValueHandler
	// .handle(new File(
	// "/mnt/data/experimentalResults/6benchmark-intel/prob/entropy_learning-prob/runs/runs/run00000")));
	// }
}
