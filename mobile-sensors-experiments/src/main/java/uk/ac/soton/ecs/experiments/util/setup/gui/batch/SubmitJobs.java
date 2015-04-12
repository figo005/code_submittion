package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.setup.SSHCommandExecutor;
import uk.ac.soton.ecs.experiments.util.setup.gui.LogFileDialog;

public class SubmitJobs implements BatchJob {

	private Configuration configuration;

	public SubmitJobs(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return "Submit jobs on the iridis cluster";
	}

	public void run() throws Exception {
		final SSHCommandExecutor commandExecutor = new SSHCommandExecutor();
		commandExecutor.setUserName("rs1f06");
		commandExecutor.setHostName("iridis2.soton.ac.uk");

		String uberSubmitterFileName = DirectoryIndex
				.getUberSubmitterFileName(configuration);

		commandExecutor.setCommand("sh " + uberSubmitterFileName);

		InputStream in = commandExecutor.executeInteractive();

		final LogFileDialog logFileDialog = new LogFileDialog(in,
				new Runnable() {
					public void run() {
						try {
							commandExecutor.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, true, "Starting jobs on Iridis");

		logFileDialog.setVisible(true);
	}
}
