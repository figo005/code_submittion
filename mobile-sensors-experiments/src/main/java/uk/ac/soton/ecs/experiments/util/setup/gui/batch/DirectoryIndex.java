package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.setup.BuildMobileSensorJarFileCommand;
import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class DirectoryIndex {

	public static File getSensorDirectory(Configuration configuration,
			String sensorFileName) {
		File outputDir = getOutputDirectory(configuration);

		sensorFileName = new File(sensorFileName).getName();
		String name = sensorFileName.substring(0, sensorFileName
				.lastIndexOf("."));
		File sensorDir = new File(outputDir, name
				+ getDirectoryPostFix(configuration));

		return sensorDir;
	}

	public static File getOutputDirectory(Configuration configuration) {
		return new File(configuration
				.getString(ConfigurationPropertyConstants.OUTPUT_DIR));
	}

	public static File getDirectoryPostFix(Configuration configuration) {
		return new File(configuration
				.getString(ConfigurationPropertyConstants.EXPERIMENT_ID));
	}

	public static File getSimulationOutputDir(Configuration configuration) {
		return new File(getOutputDirectory(configuration), "simulations");

	}

	public static List<String> getSensorDirectoryNames(
			Configuration configuration) {
		return configuration
				.getList(ConfigurationPropertyConstants.SENSOR_FILES);
	}

	public static Collection<File> getSensorDirectories(
			Configuration configuration) {
		Collection<File> result = new ArrayList<File>();

		for (String name : getSensorDirectoryNames(configuration)) {
			result.add(getSensorDirectory(configuration, name));
		}

		return result;
	}

	public static File getMobileSensorJarFile(Configuration configuration) {
		return BuildMobileSensorJarFileCommand
				.getJarFile(
						new File(
								configuration
										.getString(ConfigurationPropertyConstants.MOBILE_SENSORS_PROJECT_DIR)),
						BuildMobileSensorJarFileCommand.DEFAULT_SOURCE_JAR_NAME);

	}

	public static String getUberSubmitterFileName(Configuration configuration) {
		String experimentID = configuration
				.getString(ConfigurationPropertyConstants.EXPERIMENT_ID);

		return "submitall_" + experimentID + ".sh";

	}

	public static String getGraphDirectoryName() {
		return "graphs";
	}
}
