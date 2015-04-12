package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import uk.ac.soton.ecs.experiments.util.ConfigurationGenerator;

public class ConfigurationGeneratorCommand implements WizardCommand {

	private File outputDirectory;
	private File template;
	private File configFile;
	private String extension;

	public void execute(String[] parameters) {
		outputDirectory = ConsoleUtil.getFileFromInput(
				"Output directory for files", outputDirectory);
		template = ConsoleUtil.getFileFromInput("Template file", template);
		configFile = ConsoleUtil.getFileFromInput("Configuration file",
				configFile);
		extension = ConsoleUtil.readLine("Outputfile extension: ", extension);
		System.out.println();
		System.out.println("Your choices: ");
		System.out.println("Output directory: " + outputDirectory);
		System.out.println("Template file: " + template);
		System.out.println("Configuration file: " + configFile);
		System.out.println("Extension: " + extension);

		if (ConsoleUtil.confirm("Proceed? ")) {
			try {
				ConfigurationGenerator.run(template, outputDirectory,
						configFile, extension);
			} catch (ConfigurationException e) {
				System.out.println("Configuration incorrect");
			} catch (IOException e) {
				System.out.println("IO Exception");
				e.printStackTrace();
			}
		} else
			System.out.println("Aborted");
	}

	public String getCommand() {
		return "confgen";
	}

	public String getDescription() {
		return "generate simulation and sensor configuration files based on templates";
	}
}
