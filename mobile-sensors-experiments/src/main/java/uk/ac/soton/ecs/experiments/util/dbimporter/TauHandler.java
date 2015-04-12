package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TauHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.TAU;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils
					.readLines(new File(directory, "tau"));

			return lines.get(0);
		} catch (IOException e) {
			return null;
		}
	}
}
