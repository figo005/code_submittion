package uk.ac.soton.ecs.experiments.util.setup;

import java.util.Map;

public class ListCommand implements WizardCommand {

	private Map<String, WizardCommand> commands;

	public ListCommand(Map<String, WizardCommand> commands) {
		this.commands = commands;
	}

	public void execute(String[] parameters) {
		for (String commandName : commands.keySet()) {
			System.out.println(commandName + ": "
					+ commands.get(commandName).getDescription());
		}
	}

	public String getCommand() {
		return "list";
	}

	public String getDescription() {
		return "List all available commands";
	}

}
