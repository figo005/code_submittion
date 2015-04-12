package uk.ac.soton.ecs.experiments.util.checker;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class ExperimentsRootDirectory {

	private List<File> rootDirectories = new ArrayList<File>();

	public ExperimentsRootDirectory(File... files) {
		for (File file : files) {
			rootDirectories.add(file);
		}
	}

	public Set<ExperimentDirectory> getExperimentDirectories() {
		Set<ExperimentDirectory> result = new HashSet<ExperimentDirectory>();

		for (File rootDirectory : rootDirectories) {
			result.addAll(getExperimentDirectories(rootDirectory));
		}

		return result;
	}

	private Set<ExperimentDirectory> getExperimentDirectories(File root) {
		System.out.println("Scanning " + root);

		File[] directories = root
				.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		Set<ExperimentDirectory> experiments = new HashSet<ExperimentDirectory>();

		for (File directory : directories) {
			if (ExperimentDirectory.isExperimentDirectory(directory)) {

				ExperimentDirectory experimentDirectory = new ExperimentDirectory(
						directory);

				experiments.add(experimentDirectory);
			} else {
				experiments.addAll(getExperimentDirectories(directory));
			}
		}

		return experiments;
	}
}
