package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CopySensorFiles implements BatchJob {

	private Configuration configuration;

	public CopySensorFiles(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Copy sensor files";
	}

	public void run() throws Exception {
		List<String> sensorFileNames = configuration
				.getList(ConfigurationPropertyConstants.SENSOR_FILES);

		for (String sensorFileName : sensorFileNames) {
			File sensorFile = new File(sensorFileName);

			File sensorDirectory = DirectoryIndex.getSensorDirectory(
					configuration, sensorFileName);
			FileUtils.copyFileToDirectory(sensorFile, sensorDirectory);
			File sensorFileDest = new File(sensorDirectory, sensorFile
					.getName());
			sensorFileDest.renameTo(new File(sensorDirectory,
					ConfigurationPropertyConstants.SENSOR_CONFIGURATION_FILE));
		}
	}

}
