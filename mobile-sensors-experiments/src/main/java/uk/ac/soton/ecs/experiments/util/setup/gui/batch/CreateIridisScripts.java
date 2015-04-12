package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.IridisScriptGenerator;
import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CreateIridisScripts implements BatchJob {

	private Configuration configuration;

	public CreateIridisScripts(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Create Iridis scripts";
	}

	public void run() throws Exception {

		for (File sensorDir : DirectoryIndex
				.getSensorDirectories(configuration)) {
			Map<String, String> additionalReplacements = getReplacementsConfiguration();
			additionalReplacements.put("sensor_file",
					ConfigurationPropertyConstants.SENSOR_CONFIGURATION_FILE);
			additionalReplacements.put("directory", sensorDir.getName());

			File template = new File(
					configuration
							.getString(ConfigurationPropertyConstants.IRIDIS_TEMPLATE_FILE));
			File outputDirectory = DirectoryIndex
					.getOutputDirectory(configuration);
			File configurationFileDirectory = DirectoryIndex
					.getSimulationOutputDir(configuration);
			String extension = "xml";

			IridisScriptGenerator.run(sensorDir, template,
					configurationFileDirectory, new File("scripts"), extension,
					additionalReplacements);

		}

	}

	/**
	 * Returns a configuration with (param, value) pairs for the simulation
	 * template file. These are prefixed with
	 * ConfigurationPropertyConstants.SIMULATION_TEMPLATE_PROPERTIES_PREFIX
	 * 
	 * @return
	 */
	private Map<String, String> getReplacementsConfiguration() {

		Map<String, String> result = new HashMap<String, String>();

		Configuration subset = configuration
				.subset(ConfigurationPropertyConstants.IRIDIS_TEMPLATE_PREFIX);

		Iterator<String> keys = subset.getKeys();

		while (keys.hasNext()) {
			String key = keys.next();
			result.put(key, subset.getString(key));
		}

		return result;
	}
}
