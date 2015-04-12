package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.setup.BuildMobileSensorJarFileCommand;
import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;
import uk.ac.soton.ecs.experiments.util.setup.gui.LogFileDialog;

public class BuildMobileJar implements BatchJob {

	private Configuration configuration;

	public BuildMobileJar(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Build jar file";
	}

	public void run() throws Exception {
		if (configuration.getBoolean(
				ConfigurationPropertyConstants.REBUILD_JAR_DURING_BATCH, false)) {

			String projectDirectory = configuration
					.getString(ConfigurationPropertyConstants.MOBILE_SENSORS_PROJECT_DIR);

			Process buildJarFileProcess = BuildMobileSensorJarFileCommand
					.createBuildJarFileProcess(new File(projectDirectory));

			LogFileDialog dialog = new LogFileDialog(buildJarFileProcess
					.getInputStream(), "Building JAR file");

			dialog.setVisible(true);

			int waitFor = buildJarFileProcess.waitFor();

			if (waitFor != 0) {
				throw new Exception("Build failed");
			}
		}
	}

}
