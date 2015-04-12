package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class EndTimeValueHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.END_TIME;
	}

	public String handle(File directory) {
		try {
			return FileUtils
					.readFileToString(new File(directory, "endtime.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
