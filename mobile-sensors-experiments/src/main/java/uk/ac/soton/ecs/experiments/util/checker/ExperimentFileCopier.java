package uk.ac.soton.ecs.experiments.util.checker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.filecopier.CopyJob;

public class ExperimentFileCopier {

	private File directory;
	private File destinationDirectory;

	private static String[] relevantFiles = { ".*.txt", ".*.xml", ".*.txt.gz", "sensor.*/*" };

	public ExperimentFileCopier(File directory, File destinationDirectory) {
		this.directory = directory;
		this.destinationDirectory = destinationDirectory;
	}

	public List<CopyJob> getCopyJobs() {
		List<CopyJob> result = new ArrayList<CopyJob>();

//		for (File sensorDirectory : directory
//				.listFiles((FileFilter) DirectoryFileFilter.INSTANCE)) {
//			if (sensorDirectory.getName().startsWith("sensor")) {
//				System.err.println("found something");
//
//				File destinationDir = new File(destinationDirectory
//						.getAbsolutePath()
//						+ "/"
//						+ directory.getName()
//						+ "/"
//						+ sensorDirectory.getName() + "/sensor_locations");
//
//				CopyJob job = new CopyJob(true, destinationDirectory
//						.getAbsolutePath()
//						+ "/"
//						+ directory.getName()
//						+ "/"
//						+ sensorDirectory.getName() + "/sensor_locations",
//						sensorDirectory.getAbsolutePath() + "/"
//								+ "sensor_locations");
//
//				System.err.println(job);
//
//				result.add(job);
//			}
//		}

		result.add(new CopyJob(true, destinationDirectory.getAbsolutePath()
				+ "/" + directory.getName(), prependDirectory(relevantFiles)));

		return result;
	}

	private String[] prependDirectory(String[] files) {
		String[] result = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			result[i] = directory.getAbsolutePath() + "/" + files[i];
		}

		return result;
	}
}
