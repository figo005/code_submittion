package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CaptureTimeHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.CAPTURE_TIME;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils.readLines(new File(directory,
					"capturetime.txt"));

			if (lines.get(0).equals("null"))
				return "-1";

			return lines.get(0);
		} catch (IOException e) {
			return null;
		}
	}
}
