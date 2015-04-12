package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

// TYPE: STRING
public class StateCountBeforeHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.STATE_COUNT_BEFORE;
	}

	public String handle(File directory) {
		try {
			List<String> lines = FileUtils.readLines(new File(directory,
					"state_count_before"));

			return StringUtils.join(lines, " ");
		} catch (IOException e) {
			return null;
		}
	}
}
