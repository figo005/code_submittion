package uk.ac.soton.ecs.experiments.util.setup;

public class ExitCommand implements WizardCommand {

	public void execute(String[] parameters) {
		System.exit(0);
	}

	public String getCommand() {
		return "exit";
	}

	public String getDescription() {
		return "Exits the wizard";
	}

}
