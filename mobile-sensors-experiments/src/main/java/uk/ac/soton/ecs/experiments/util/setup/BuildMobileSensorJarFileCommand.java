package uk.ac.soton.ecs.experiments.util.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

public class BuildMobileSensorJarFileCommand implements WizardCommand {

	public static final String DEFAULT_DESTINATION_JAR_NAME = "experiments.jar";
	public static final File DEFAULT_PROJECT_DIR = new File(ConsoleUtil
			.getUserDirectory(), "/workspace/mobile-sensors");

	public static final String DEFAULT_SOURCE_JAR_NAME = "mobile-sensors-1.0-SNAPSHOT-jar-with-dependencies.jar";
	public File projectDirectory = DEFAULT_PROJECT_DIR;
	private String destinationJarFileName = DEFAULT_DESTINATION_JAR_NAME;
	private String jarFileName = DEFAULT_SOURCE_JAR_NAME;

	public void execute(String[] parameters) {
		projectDirectory = ConsoleUtil.getFileFromInput(
				"Directory of mobile-sensors project", projectDirectory);
		destinationJarFileName = ConsoleUtil.readLine("File name for jarfile",
				destinationJarFileName);

		File jarFile = getJarFile();

		System.out.println(jarFileName + " exists? " + jarFile.exists());

		try {
			if (!jarFile.exists())
				buildJarFile();
			else if (ConsoleUtil.confirm("Rebuild?"))
				buildJarFile();

			System.out.println(destinationJarFileName);

			copyJarFile(projectDirectory, jarFileName, destinationJarFileName);

		} catch (IOException e) {
			System.out.println("I/O Error");
			e.printStackTrace();
		}

	}

	private File getJarFile() {
		return getJarFile(projectDirectory, jarFileName);
	}

	public static File getJarFile(File projectDirectory, String jarFileName) {
		return new File(projectDirectory, "target/" + jarFileName);
	}

	public static void copyJarFile(File projectDirectory, String jarFileName,
			String destinationJarFileName) throws IOException {
		File destinationJar = new File(destinationJarFileName);

		System.out
				.println("Copying jar to " + destinationJar.getAbsolutePath());

		FileUtils.copyFile(getJarFile(projectDirectory, jarFileName),
				destinationJar);
	}

	private void buildJarFile() throws IOException {
		buildJarFile(projectDirectory);
	}

	public static Process createBuildJarFileProcess(File projectDirectory)
			throws IOException {
		String command = "mvn -f " + projectDirectory.getAbsolutePath()
				+ "/pom.xml clean assembly:assembly -Dmaven.test.skip";

		Process exec = Runtime.getRuntime().exec(command);
		return exec;
	}

	public static void buildJarFile(File projectDirectory) throws IOException {
		System.out.println("Building jarfile");

		Process exec = createBuildJarFileProcess(projectDirectory);
		InputStream inputStream = exec.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null)
			System.out.println(line);

		try {
			if (exec.waitFor() == 0)
				System.out.println("Building jarfile complete");
			else
				System.out.println("Something went wrong");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getCommand() {
		return "mobilejar";
	}

	public String getDescription() {
		return "build mobile sensors jar-file and copy to current directory";
	}

}
