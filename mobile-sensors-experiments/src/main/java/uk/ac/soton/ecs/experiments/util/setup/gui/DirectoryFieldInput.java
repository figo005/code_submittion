package uk.ac.soton.ecs.experiments.util.setup.gui;

import org.apache.commons.configuration.Configuration;

public class DirectoryFieldInput extends AbstractFileInput {

	public DirectoryFieldInput(String title, String propertyName,
			Configuration properties) {
		super(title, true, propertyName, properties);
	}
}
