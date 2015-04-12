package uk.ac.soton.ecs.experiments.util;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFileNameFilter implements FilenameFilter {

	private String extension;

	public ExtensionFileNameFilter(String extension) {
		this.extension = extension;
	}

	public boolean accept(File dir, String name) {
		return name.endsWith(extension);
	}

}
