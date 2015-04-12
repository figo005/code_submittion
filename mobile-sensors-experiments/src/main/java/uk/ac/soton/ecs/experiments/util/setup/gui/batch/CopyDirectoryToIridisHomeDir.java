package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.checker.FileCopierDialog;
import uk.ac.soton.ecs.experiments.util.setup.gui.ConfigurationPropertyConstants;
import ch.fhnw.filecopier.CopyJob;
import ch.fhnw.filecopier.FileCopier;

public class CopyDirectoryToIridisHomeDir implements BatchJob {

	private Configuration configuration;

	public CopyDirectoryToIridisHomeDir(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Copy experiment directory to iridis";
	}

	public void run() throws Exception {
		final FileCopierDialog fileCopierDialog = new FileCopierDialog(null);
		final FileCopier fileCopier = new FileCopier();

		fileCopierDialog.setFileCopier(fileCopier);

		String destination = configuration
				.getString(ConfigurationPropertyConstants.IRIDIS_HOME_DIR);
		String source = DirectoryIndex.getOutputDirectory(configuration)
				.getAbsolutePath()
				+ "/.*";

		final CopyJob copyJob = new CopyJob(true, destination, source);

		fileCopierDialog.setVisible(true);

		fileCopier.copy(copyJob);
		fileCopierDialog.setVisible(false);
	}
}
