package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;
import java.util.HashMap;

import uk.ac.soton.ecs.experiments.util.IridisScriptGenerator;

public class IridisScriptgeneratorCommand implements WizardCommand {

	private File outputDirectory;
	private File template;
	private File configurationFileDirectory;
	private String extension;

	public void execute(String[] parameters) {
		outputDirectory = ConsoleUtil.getFileFromInput(
				"Output directory for scripts", outputDirectory);
		template = ConsoleUtil.getFileFromInput("Script template file",
				template);
		configurationFileDirectory = ConsoleUtil.getFileFromInput(
				"Configuration file directory", configurationFileDirectory);
		extension = ConsoleUtil.readLine("Configuration file extensions: ",
				extension);
		System.out.println();
		System.out.println("Your choices: ");
		System.out.println("Output directory for scripts: " + outputDirectory);
		System.out.println("Script template file: " + template);
		System.out.println("Configuration file directory: "
				+ configurationFileDirectory);
		System.out.println("Configuration file extensions: " + extension);

		if (ConsoleUtil.confirm("Proceed? ")) {

			try {
				IridisScriptGenerator.run(new File("."), template,
						configurationFileDirectory, outputDirectory, extension,
						new HashMap<String, String>());
			} catch (Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		} else {
			System.out.println("Aborted");
		}
	}

	public String getCommand() {
		return "irgen";
	}

	public String getDescription() {
		return "Generate scripts for Iridis cluster based on template";
	}

}
