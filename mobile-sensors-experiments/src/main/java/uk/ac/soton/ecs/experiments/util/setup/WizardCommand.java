package uk.ac.soton.ecs.experiments.util.setup;

public interface WizardCommand {

	String getCommand();

	String getDescription();

	void execute(String[] parameters);
}
