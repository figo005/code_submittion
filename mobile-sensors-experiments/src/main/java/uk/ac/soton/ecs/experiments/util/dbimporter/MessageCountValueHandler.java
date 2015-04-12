package uk.ac.soton.ecs.experiments.util.dbimporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class MessageCountValueHandler implements ConfigurationValueHandler {

	public ColumnName getColumnName() {
		return ColumnName.MESSAGE_COUNT;
	}

	public String handle(File directory) {
		File messageFile = new File(directory, "message_count.txt");

		if (messageFile.exists()) {

			try {
				return (String) FileUtils.readLines(messageFile).get(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
