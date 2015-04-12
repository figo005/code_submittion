package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

public class CreateUberSubmitterFile implements BatchJob {

	private Configuration configuration;

	public CreateUberSubmitterFile(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Create uber submitter file";
	}

	public void run() throws Exception {
		File outputDirectory = DirectoryIndex.getOutputDirectory(configuration);

		File ubserSubmitterFile = new File(outputDirectory, DirectoryIndex
				.getUberSubmitterFileName(configuration));

		StringBuffer buffer = new StringBuffer();

		for (File sensorDirectory : DirectoryIndex
				.getSensorDirectories(configuration)) {
			String sensorDirectoryName = sensorDirectory.getName();

			buffer.append("echo " + sensorDirectoryName + "\n");
			buffer.append("cd " + sensorDirectoryName + "\n");
			buffer.append("sh submitter.sh \n");
			buffer.append("cd ..\n");
		}

		FileUtils.writeStringToFile(ubserSubmitterFile, buffer.toString());
	}

}
