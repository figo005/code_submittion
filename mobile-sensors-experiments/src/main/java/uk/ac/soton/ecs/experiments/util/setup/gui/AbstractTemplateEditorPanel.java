package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;

import uk.ac.soton.ecs.experiments.util.ConfigurationGenerator;

public abstract class AbstractTemplateEditorPanel extends JPanel implements
		TextListener {

	private JTable propertiesTable;
	private Configuration properties;

	public AbstractTemplateEditorPanel(Configuration properties, String title,
			String propertyName) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		FileFieldInput fileFieldInput = new FileFieldInput(title, propertyName,
				properties);
		add(fileFieldInput);
		this.properties = properties;

		fileFieldInput.addTextListener(this);

		propertiesTable = new JTable(new PropertiesModel());

		JScrollPane scrollPane = new JScrollPane(propertiesTable);

		add(scrollPane);
		update(fileFieldInput.getText());
	}

	public void textValueChanged(TextEvent e) {
		String fileName = ((TextField) e.getSource()).getText();

		update(fileName);
	}

	private void update(String fileName) {
		File file = new File(fileName);

		if (file.exists()) {
			String template;
			try {
				template = FileUtils.readFileToString(file);
				Set<String> parameterNames = ConfigurationGenerator
						.getParameterNames(template);

				propertiesTable.setModel(new PropertiesModel(parameterNames,
						properties, getConfigurationPropertyPrefix()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected abstract String getConfigurationPropertyPrefix();
}
