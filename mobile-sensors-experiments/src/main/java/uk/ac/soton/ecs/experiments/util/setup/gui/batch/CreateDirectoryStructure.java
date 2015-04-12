package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CreateDirectoryStructure implements BatchJob {

	private Configuration configuration;

	public CreateDirectoryStructure(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Create directory structure";
	}

	public void run() throws Exception {
		System.out.println("Creating directory structure");
		File outputDir = new File(
				configuration
						.getString(ConfigurationPropertyConstants.OUTPUT_DIR));

		try {
			// FileUtils.forceDelete(outputDir);
		} catch (Exception e) {
			// never mind
		}

		outputDir.mkdir();

		List<String> sensorFilesNames = configuration
				.getList(ConfigurationPropertyConstants.SENSOR_FILES);

		for (String sensorFileName : sensorFilesNames) {
			System.out.println(sensorFileName);
			File sensorDir = DirectoryIndex.getSensorDirectory(configuration,
					sensorFileName);
			sensorDir.mkdir();
			new File(sensorDir, "logs").mkdir();
			FileUtils.writeStringToFile(new File(sensorDir, "name"),
					sensorDir.getName());
			FileUtils.writeStringToFile(new File(sensorDir, "experiment_name"),
					sensorDir.getName());
		}

	}

}
