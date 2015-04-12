package uk.ac.soton.ecs.experiments.util.setup.gui;

import org.apache.commons.configuration.Configuration;

public class FileFieldInput extends AbstractFileInput {

	public FileFieldInput(String title, String propertyName,
			Configuration properties) {
		super(title, false, propertyName, properties);
	}
}
