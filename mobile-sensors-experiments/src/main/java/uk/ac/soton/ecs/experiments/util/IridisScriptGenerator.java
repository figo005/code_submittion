package uk.ac.soton.ecs.experiments.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class IridisScriptGenerator {

	public static int processors = 8;

	public static final String simulationFileParameter = Matcher
			.quoteReplacement("$simulation_files");

	public static void main(String[] args) throws Exception {
		if (args.length != 4)
			System.out
					.println("Usage: template simulationConfigDir outputdir extension");

		File templateFile = new File(args[0]);
		File simulationConfigDir = new File(args[1]);
		File outputDir = new File(args[2]);
		String extension = args[3];

		run(new File("."), templateFile, simulationConfigDir, outputDir,
				extension, new HashMap<String, String>());
	}

	/**
	 * 
	 * @param templateFile
	 * @param simulationConfigDir
	 * @param outputDir
	 * @param extension
	 *            extension of the simulation files that are to be used
	 * @throws Exception
	 */
	public static void run(File relativeDir, File templateFile,
			File simulationConfigDir, File outputDir, String extension,
			Map<String, String> additionalReplacements) throws Exception {
		String template = FileUtils.readFileToString(templateFile);
		outputDir = new File(relativeDir, outputDir.getName());
		FileUtils.forceMkdir(outputDir);
		File simulationConfigurationDirectory = new File(relativeDir,
				simulationConfigDir.getName());

		System.out.println(simulationConfigurationDirectory);
		File[] configurationFiles = simulationConfigurationDirectory
				.listFiles(new ExtensionFileNameFilter(extension));

		System.out.println("Found " + configurationFiles.length
				+ " configuration files");

		int success = 0;

		int scriptFileCount = (int) Math.ceil(configurationFiles.length
				/ (double) processors);

		for (int i = 0; i < scriptFileCount; i++) {
			int firstConfigFile = i * processors;
			int lastConfigFile = Math.min((i + 1) * processors,
					configurationFiles.length) - 1;

			writeScript(firstConfigFile, lastConfigFile, configurationFiles,
					simulationConfigDir, additionalReplacements, template, i,
					outputDir);

			success++;
		}

		System.out.println("Wrote " + success + " script files");
	}

	private static void writeScript(int firstConfigFile, int lastConfigFile,
			File[] configurationFiles, File simulationConfigDir,
			Map<String, String> additionalReplacements, String template,
			int scriptIndex, File outputDir) throws IOException {
		String simulationFileParameterValue = "";

		for (int i = firstConfigFile; i <= lastConfigFile; i++) {
			String value = new File(simulationConfigDir.getName(),
					configurationFiles[i].getName()).toString();
			simulationFileParameterValue += value + " ";
		}

		String workingTemplate = template.replaceAll(simulationFileParameter,
				simulationFileParameterValue);

		for (String parameter : additionalReplacements.keySet()) {
			String value = additionalReplacements.get(parameter);
			workingTemplate = workingTemplate.replaceAll(Matcher
					.quoteReplacement("$" + parameter), value);
		}

		FileUtils.writeStringToFile(new File(outputDir, "script"
				+ StringUtils.leftPad(String.valueOf(scriptIndex), 5, '0')),
				workingTemplate);
	}
}
