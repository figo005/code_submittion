package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.configuration.Configuration;

public class AbstractTextFieldInput extends JPanel implements TextListener {

	private Configuration properties;
	private String propertyName;

	protected TextField textField = new TextField(60);

	public AbstractTextFieldInput(String title, String propertyName,
			Configuration properties) {
		this(title, propertyName, properties, "");
	}

	public AbstractTextFieldInput(String title, String propertyName,
			Configuration properties, String defaultPropertyValue) {
		String defaultFileName = properties.getString(propertyName);

		if (defaultFileName == null || defaultFileName.equals("")) {
			defaultFileName = defaultPropertyValue;
		}

		this.properties = properties;
		this.propertyName = propertyName;

		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(title), BorderFactory.createEmptyBorder(5,
				5, 5, 5)));

		setLayout(new FlowLayout());
		textField.addTextListener(this);

		textField.setText(defaultFileName);
		properties.setProperty(propertyName, defaultFileName);

		add(textField);

	}

	public void textValueChanged(TextEvent e) {
		properties.setProperty(propertyName, ((TextField) e.getSource())
				.getText());
	}

	public void addTextListener(TextListener listener) {
		textField.addTextListener(listener);
	}

	public String getText() {
		return textField.getText();
	}

}
