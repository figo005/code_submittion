package uk.ac.soton.ecs.experiments.util.setup.gui;

import org.apache.commons.configuration.Configuration;

public class TextFieldInput extends AbstractTextFieldInput {

	public TextFieldInput(String title, String propertyName,
			Configuration properties) {
		super(title, propertyName, properties);
	}

	public TextFieldInput(String title, String propertyName,
			Configuration properties, String defaultPropertyValue) {
		super(title, propertyName, properties, defaultPropertyValue);
	}
}
