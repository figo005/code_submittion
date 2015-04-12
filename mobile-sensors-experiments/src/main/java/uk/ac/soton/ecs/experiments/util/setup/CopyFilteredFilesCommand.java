package uk.ac.soton.ecs.experiments.util.setup;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class CopyFilteredFilesCommand implements WizardCommand {

	private String filenamePattern;
	private File sourceDirectory;
	private File destinationDirectory;
	private FilenameFilter filenameFilter;

	public void execute(String[] parameters) {
		filenamePattern = ConsoleUtil.readLine("Filename Pattern: ",
				filenamePattern);
		sourceDirectory = ConsoleUtil.getFileFromInput("Source dir: ",
				sourceDirectory);
		destinationDirectory = ConsoleUtil.getFileFromInput("Dest dir: ",
				destinationDirectory);

		filenameFilter = new WildcardFileFilter(filenamePattern.split(","));
		// copy all
		try {
			copyFiles(sourceDirectory, destinationDirectory);
		} catch (IOException e) {
			System.err.println("Error copying directory");
			e.printStackTrace();
		}
	}

	private void copyFiles(File sourceDirectory, File destinationDirectory)
			throws IOException {
		System.out.println(sourceDirectory);

		// Validate.isTrue(sourceDirectory.isDirectory());
		// Validate.isTrue(destinationDirectory.exists());

		File[] matchingFiles = sourceDirectory.listFiles(filenameFilter);

		for (File file : matchingFiles) {
			if (!file.isDirectory()) {
				FileUtils.copyFile(file, new File(destinationDirectory, file
						.getName()));
			}
		}

		File[] directories = sourceDirectory
				.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		for (File directory : directories) {
			// Validate.isTrue(directory.isDirectory());

			File destinationSubDirectory = new File(destinationDirectory,
					directory.getName());
			FileUtils.forceMkdir(destinationSubDirectory);
			copyFiles(directory, destinationSubDirectory);
		}

	}

	public String getCommand() {
		return "copyfiltered";
	}

	public String getDescription() {
		return "Copy entire directory structure filtered for specified files";
	}

}
