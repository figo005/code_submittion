package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.ConfigurationGenerator;
import uk.ac.soton.ecs.experiments.util.IridisScriptGenerator;

public class BuildExperimentDirectoryCommand implements WizardCommand {

	private String sensorConfigurationFileName = "sensor.xml";
	private File sensorFileDir = new File("sensors");
	private File gridFileDir = new File("graphs");
	private File graphFileDir = new File("grids");
	private File simulationTemplateFile = new File("simulation.xml");
	private File replacementFile = new File("replacements.txt");
	private File iridisScriptTemplate = new File("iridis_script_template.txt");
	private String directoryPostFix = null;
	private File mobileSensorJar = new File("experiments.jar");

	private File outputDir = new File("output");
	private File scriptsOutputDir = new File("scripts");
	private File simulationOutputDir = new File("simulations");

	private File submitterScriptFile = new File(ConsoleUtil.getUserDirectory(),
			"/workspace/experiments/src/main/resources/scripts/submitter.sh");

	private Map<File, File> sensorDirs = new HashMap<File, File>();

	public void execute(String[] parameters) {
		directoryPostFix = ConsoleUtil.readLine(
				"Directory Postfix (ID for set of experiments) ",
				directoryPostFix);
		sensorFileDir = ConsoleUtil.getFileFromInput(
				"Directory with sensor definition files", sensorFileDir);
		gridFileDir = ConsoleUtil.getFileFromInput(
				"Directory with grid definition files", gridFileDir);
		graphFileDir = ConsoleUtil.getFileFromInput(
				"Directory with graph definition files", graphFileDir);
		simulationTemplateFile = ConsoleUtil.getFileFromInput(
				"Simulation template definition file", simulationTemplateFile);
		replacementFile = ConsoleUtil.getFileFromInput(
				"Replacement configuration file (for simulation template)",
				replacementFile);
		iridisScriptTemplate = ConsoleUtil.getFileFromInput(
				"Iridis script template file", iridisScriptTemplate);
		submitterScriptFile = ConsoleUtil.getFileFromInput(
				"Iridis submitter script file", submitterScriptFile);

		new BuildMobileSensorJarFileCommand().execute(new String[] {});

		mobileSensorJar = ConsoleUtil.getFileFromInput(
				"Name of Mobile Sensor JarFile", mobileSensorJar);

		try {
			performCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void performCommand() throws Exception {
		try {
			FileUtils.forceDelete(simulationOutputDir);
		} catch (Exception e) {
			// never mind
		}
		try {
			FileUtils.forceDelete(outputDir);
		} catch (Exception e) {
			// never mind
		}

		createDirectoryStructure();
		copySensorFiles();
		copyGraphsAndGrids();
		createSimulationConfigFiles();
		createIridisScripts();
		copyJar();
		copySubmitterScript();
	}

	private void copySubmitterScript() throws IOException {
		System.out.println("Copying submitter scripts to sensor directories");
		for (File sensorDir : sensorDirs.values()) {
			FileUtils.copyFileToDirectory(submitterScriptFile, sensorDir);
		}
	}

	private void createIridisScripts() throws Exception {
		// replace direcory by correct directory

		for (File sensorDir : sensorDirs.values()) {
			Map<String, String> additionalReplacements = new HashMap<String, String>();
			additionalReplacements.put("$sensor_file",
					sensorConfigurationFileName);
			additionalReplacements.put("$directory", sensorDir.getName());

			IridisScriptGenerator.run(sensorDir, iridisScriptTemplate,
					simulationOutputDir, scriptsOutputDir, "xml",
					additionalReplacements);
		}
	}

	private void copyJar() throws IOException {
		System.out.println("Copying " + mobileSensorJar.getName()
				+ " to sensor directories");
		for (File sensorDir : sensorDirs.values()) {
			FileUtils.copyFileToDirectory(mobileSensorJar, sensorDir);
		}
	}

	private void createSimulationConfigFiles() throws ConfigurationException,
			IOException {
		System.out.println("Creating simulation configuration files");

		ConfigurationGenerator.run(simulationTemplateFile, simulationOutputDir,
				replacementFile, "xml");

		for (File sensorDir : sensorDirs.values()) {
			FileUtils.copyDirectoryToDirectory(simulationOutputDir, sensorDir);
		}
	}

	private void copyGraphsAndGrids() throws IOException {
		System.out.println("Copying graphs and grids");
		for (File sensorDir : sensorDirs.values()) {
			FileUtils.copyDirectoryToDirectory(graphFileDir, sensorDir);
			FileUtils.copyDirectoryToDirectory(gridFileDir, sensorDir);
		}
	}

	private void copySensorFiles() throws IOException {
		System.out.println("Copying sensor configuration files");

		for (File sensorFile : sensorDirs.keySet()) {
			FileUtils.copyFileToDirectory(sensorFile, sensorDirs
					.get(sensorFile));
			File sensorFileDest = new File(sensorDirs.get(sensorFile),
					sensorFile.getName());
			sensorFileDest.renameTo(new File(sensorDirs.get(sensorFile),
					sensorConfigurationFileName));
		}
	}

	private void createDirectoryStructure() throws IOException {
		System.out.println("Creating directory structure");
		outputDir.mkdir();

		for (File sensorFile : sensorFileDir.listFiles()) {
			String sensorFileName = sensorFile.getName();
			String name = sensorFileName.substring(0, sensorFileName
					.lastIndexOf("."));
			File sensorDir = new File(outputDir, name + directoryPostFix);
			sensorDir.mkdir();
			FileUtils.writeStringToFile(new File(sensorDir, "name"), sensorDir
					.getName());

			sensorDirs.put(sensorFile, sensorDir);
		}
	}

	public String getCommand() {
		return "builddir";
	}

	public String getDescription() {
		return "Build directory with experiments, ready to be deployed";
	}

}
