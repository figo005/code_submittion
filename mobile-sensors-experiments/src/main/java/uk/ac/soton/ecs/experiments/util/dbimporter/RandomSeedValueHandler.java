package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class RandomSeedValueHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.RANDOM_SEED;
	}

	public String handle(File directory) {
		File simulationFile = new File(directory, "simulation.xml");

		Pattern pattern = Pattern.compile("randomSeed.*value=.*\"",
				Pattern.MULTILINE);
		Matcher matcher;
		try {
			matcher = pattern.matcher(FileUtils
					.readFileToString(simulationFile));

			String result = null;

			if (matcher.find()) {
				result = matcher.group();
			}

			result = result.substring(0, result.lastIndexOf("\""));
			result = result.substring(result.lastIndexOf("\"") + 1, result
					.length());

			Double parseDouble = Double.parseDouble(result);
			if (parseDouble == Double.POSITIVE_INFINITY) {
				parseDouble = -1.0;
			}

			return parseDouble.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
