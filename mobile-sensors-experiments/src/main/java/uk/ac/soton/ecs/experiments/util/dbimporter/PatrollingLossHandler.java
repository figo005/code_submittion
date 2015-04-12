package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class PatrollingLossHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.PATROLLING_LOSS;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils.readLines(new File(directory,
					"globalutility.txt"));

			return lines.get(0);
		} catch (IOException e) {
			// System.out.println("Global util does not exist");
			return null;
		}
	}

}
