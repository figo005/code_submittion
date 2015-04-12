package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.setup.BuildMobileSensorJarFileCommand;

public class CopyJarFile implements BatchJob {

	private Configuration configuration;

	public CopyJarFile(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Copy jar file";
	}

	public void run() throws Exception {
		File mobileSensorJarFile = DirectoryIndex
				.getMobileSensorJarFile(configuration);

		System.out.println("Copying " + mobileSensorJarFile.getName()
				+ " to output directory");

		FileUtils.copyFile(mobileSensorJarFile, new File(DirectoryIndex
				.getOutputDirectory(configuration),
				BuildMobileSensorJarFileCommand.DEFAULT_DESTINATION_JAR_NAME));
	}
}
