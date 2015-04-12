package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.ConfigurationGenerator;
import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CreateSimulationConfigFiles implements BatchJob {

	private Configuration configuration;

	public CreateSimulationConfigFiles(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Generate simulation configuration files";
	}

	public void run() throws Exception {
		System.out.println("Creating simulation configuration files");

		File simulationTemplateFile = new File(
				configuration
						.getString(ConfigurationPropertyConstants.SIMULATION_TEMPLATE_FILE));

		File simulationOutputDir = DirectoryIndex
				.getSimulationOutputDir(configuration);

		ConfigurationGenerator.run(simulationTemplateFile, simulationOutputDir,
				getReplacementsConfiguration(), "xml");

		for (File sensorDir : DirectoryIndex
				.getSensorDirectories(configuration)) {
			FileUtils.copyDirectoryToDirectory(simulationOutputDir, sensorDir);
		}

		FileUtils.deleteDirectory(simulationOutputDir);
	}

	/**
	 * Returns a configuration with (param, value) pairs for the simulation
	 * template file. These are prefixed with
	 * ConfigurationPropertyConstants.SIMULATION_TEMPLATE_PROPERTIES_PREFIX
	 * 
	 * @return
	 */
	private Configuration getReplacementsConfiguration() {
		return configuration
				.subset(ConfigurationPropertyConstants.SIMULATION_TEMPLATE_PROPERTIES_PREFIX);
	}

}
