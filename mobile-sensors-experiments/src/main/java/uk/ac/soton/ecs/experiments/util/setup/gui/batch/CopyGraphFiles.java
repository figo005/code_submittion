package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CopyGraphFiles implements BatchJob {

	private Configuration configuration;

	public CopyGraphFiles(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Copy graph files";
	}

	public void run() throws Exception {
		for (File sensorDirectory : DirectoryIndex
				.getSensorDirectories(configuration)) {
			File graphDir = new File(sensorDirectory, DirectoryIndex
					.getGraphDirectoryName());
			graphDir.mkdir();

			FileUtils
					.copyFileToDirectory(
							new File(
									configuration
											.getString(ConfigurationPropertyConstants.GRAPH_FILE_NAME)),
							graphDir);
		}
	}
}
