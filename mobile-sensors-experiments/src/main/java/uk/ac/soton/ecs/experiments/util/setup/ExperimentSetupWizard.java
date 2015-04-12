package uk.ac.soton.ecs.experiments.util.setup;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

public class ExperimentSetupWizard {

	private Map<String, WizardCommand> commands = new HashMap<String, WizardCommand>();

	public static void main(String[] args) {
		new ExperimentSetupWizard().run();

		// String currentDirectory = System.getProperty("user.dir");
		//
		// System.out.println("Working directory: " + currentDirectory);
		//
		// Console console = System.console();
		// // read user name, using java.util.Formatter syntax :
		// String username = console.readLine("User Name? ");
		//
		// // read the password, without echoing the output
		// char[] password = console.readPassword("Password? ");

	}

	public void run() {
		initialize();

		while (true) {
			String[] commandLine = ConsoleUtil.readLine("> ").split(" ");
			String commandName = commandLine[0];

			WizardCommand command = commands.get(commandName);
			if (command != null) {
				command.execute((String[]) ArrayUtils.subarray(commandLine, 1,
						commandLine.length));
			} else {
				System.out.println(commandName + " not found");
			}
		}
	}

	private void initialize() {
		registerCommand(new ConfigurationGeneratorCommand());
		registerCommand(new ListDirectoryCommand());
		registerCommand(new ListCommand(commands));
		registerCommand(new IridisScriptgeneratorCommand());
		registerCommand(new CopyFilteredFilesCommand());
		registerCommand(new BuildMobileSensorJarFileCommand());
		registerCommand(new BuildExperimentDirectoryCommand());
		registerCommand(new ExitCommand());
	}

	private void registerCommand(WizardCommand command) {
		commands.put(command.getCommand(), command);
	}
}
