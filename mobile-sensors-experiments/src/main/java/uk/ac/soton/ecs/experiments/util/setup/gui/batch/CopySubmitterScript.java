package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;

public class CopySubmitterScript implements BatchJob {

	private Configuration configuration;

	public CopySubmitterScript(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Copy Iridis submitter script";
	}

	public void run() throws Exception {
		System.out.println("Copying submitter scripts to sensor directories");
		for (File sensorDir : DirectoryIndex
				.getSensorDirectories(configuration)) {
			File submitterFile = new File(
					configuration
							.getString(ConfigurationPropertyConstants.SUBMITTER_SCRIPT_FILENAME));

			FileUtils.copyFileToDirectory(submitterFile, sensorDir);
		}

	}

}
