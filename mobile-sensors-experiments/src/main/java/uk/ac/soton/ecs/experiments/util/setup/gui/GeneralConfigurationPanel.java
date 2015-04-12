package uk.ac.soton.ecs.experiments.util.setup.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.commons.configuration.Configuration;

public class GeneralConfigurationPanel extends JPanel {

	public GeneralConfigurationPanel(Configuration properties) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		add(new TextFieldInput("Experiment Name",
				ConfigurationPropertyConstants.EXPERIMENT_ID, properties));

		add(new DirectoryFieldInput("Output Directory",
				ConfigurationPropertyConstants.OUTPUT_DIR, properties));

	}
}
