package uk.ac.soton.ecs.experiments.util.setup.gui;

import org.apache.commons.configuration.Configuration;

public class ExperimentConfigurationPanel extends AbstractTemplateEditorPanel {

	public ExperimentConfigurationPanel(Configuration properties) {
		super(properties, "Simulation Template File",
				ConfigurationPropertyConstants.SIMULATION_TEMPLATE_FILE);

		add(new FileFieldInput("Graph File",
				ConfigurationPropertyConstants.GRAPH_FILE_NAME, properties));

	}

	@Override
	protected String getConfigurationPropertyPrefix() {
		return ConfigurationPropertyConstants.SIMULATION_TEMPLATE_PROPERTIES_PREFIX;
	}
}
