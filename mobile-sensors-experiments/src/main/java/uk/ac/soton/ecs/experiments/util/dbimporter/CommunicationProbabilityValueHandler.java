package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class CommunicationProbabilityValueHandler implements
		ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.COMMUNICATION_PROB;
	}

	public String handle(File directory) {
		File simulationFile = new File(directory, "simulation.xml");

		Pattern pattern = Pattern.compile(
				"communicationProbability.*value=.*\"", Pattern.MULTILINE);
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

			return result;
		} catch (Exception e) {
			return null;
		}
	}
}
