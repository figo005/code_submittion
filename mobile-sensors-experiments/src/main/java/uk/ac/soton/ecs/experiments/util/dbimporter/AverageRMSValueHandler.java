package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class AverageRMSValueHandler implements ConfigurationValueHandler {

	private static final String AVERAGE_RMS_TXT = "average-RMS.txt";

	public ColumnName getColumnName() {
		return ColumnName.AVERAGE_RMS;
	}

	public String handle(File directory) {
		File averageRMS = new File(directory, AVERAGE_RMS_TXT);

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

	// public static void main(String[] args) {
	// AverageRMSValueHandler averageRMSValueHandler = new
	// AverageRMSValueHandler();
	//
	// System.out
	// .println(averageRMSValueHandler
	// .handle(new File(
	// "/mnt/data/experimentalResults/6benchmark-intel/prob/entropy_learning-prob/runs/runs/run00000")));
	// }
}
