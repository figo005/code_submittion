package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.File;

import org.apache.commons.configuration.Configuration;

public class ZipDirectory implements BatchJob {

	private Configuration configuration;

	public ZipDirectory(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Compress directory to tar.gz file";
	}

	public void run() throws Exception {
		File outputDirectory = DirectoryIndex.getOutputDirectory(configuration);

		String command = "tar cvfz " + outputDirectory.getName() + ".tgz "
				+ outputDirectory.getName();

		Process exec = Runtime.getRuntime().exec(command, null,
				outputDirectory.getParentFile());
		if (exec.waitFor() != 0) {
			throw new RuntimeException();
		}
	}

}
