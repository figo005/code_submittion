package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;

public class ListDirectoryCommand implements WizardCommand {

	public void execute(String[] parameters) {
		File directory;
		if (parameters.length == 0)
			directory = ConsoleUtil.getWorkingDirectory();
		else
			directory = new File(parameters[0]);

		ConsoleUtil.listDirectory(directory);
	}

	public String getCommand() {
		return "ls";
	}

	public String getDescription() {
		return "list directory";
	}

}
