package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class GammaHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.GAMMA;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils.readLines(new File(directory,
					"gamma"));

			return lines.get(0);
		} catch (IOException e) {
			return null;
		}
	}
}
