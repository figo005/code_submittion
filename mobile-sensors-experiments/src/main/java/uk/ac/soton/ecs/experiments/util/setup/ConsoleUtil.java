package uk.ac.soton.ecs.experiments.util.setup;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class ConsoleUtil {
	private static Console console = System.console();
	private static File workingDirectory = new File(System
			.getProperty("user.dir"));
	private static File userDirectory = new File(System
			.getProperty("user.home"));

	public static String readLine(String prompt) {
		return readLine(prompt, null);
	}

	public static String readLine(String prompt, String defaultString) {
		String showPrompt = prompt;

		if (defaultString != null)
			showPrompt += " [default: " + defaultString + "]";

		String input = console.readLine(showPrompt);

		if (input != null && !input.isEmpty())
			return input;
		else if (defaultString != null)
			return defaultString;
		else
			return readLine(prompt, defaultString);

	}

	public static File getFileFromInput(String prompt, File defaultFile) {
		String showPrompt = prompt
				+ ((defaultFile == null) ? "" : " [default: " + defaultFile
						+ "] ") + " [options: ls] ";

		String readLine = console.readLine(showPrompt);

		if (readLine.equals("") && defaultFile != null)
			return defaultFile;

		if (readLine.equals("ls")) {
			listDirectory(workingDirectory);
			return getFileFromInput(prompt, defaultFile);
		} else {
			File file = new File(readLine);
			if (file.exists())
				return file;
			else {
				boolean create = confirm("File or directory '" + file
						+ "' does not exist. Create? ");

				if (create) {
					try {
						FileUtils.forceMkdir(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return file;
				} else {
					return getFileFromInput(prompt, defaultFile);
				}
			}
		}
	}

	public static void listDirectory(File directory) {
		if (!directory.exists()) {
			System.out.println("Directory " + directory + " does not exist");
			return;
		}

		String[] list = directory.list();
		Arrays.sort(list);

		for (String filename : list) {
			System.out.println(filename);
		}
	}

	public static boolean confirm(String prompt) {
		String readLine = console.readLine(prompt);
		return readLine.equals("y") || readLine.equals("yes");

	}

	public static File getWorkingDirectory() {
		return workingDirectory;
	}

	public static File getUserDirectory() {
		return userDirectory;
	}
}
