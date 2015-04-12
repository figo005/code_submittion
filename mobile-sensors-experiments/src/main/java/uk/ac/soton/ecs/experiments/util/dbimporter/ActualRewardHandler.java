package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class ActualRewardHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.ACTUAL_REWARD;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils.readLines(new File(directory,
					"expected_reward"));

			return StringUtils.join(lines, " ");
		} catch (IOException e) {
			return null;
		}
	}
}
