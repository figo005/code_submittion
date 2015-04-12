package uk.ac.soton.ecs.experiments.util.setup.gui;

import org.apache.commons.configuration.Configuration;

public class IridisConfigurationPanel extends AbstractTemplateEditorPanel {

	public IridisConfigurationPanel(Configuration properties) {
		super(properties, "Iridis Template File",
				ConfigurationPropertyConstants.IRIDIS_TEMPLATE_FILE);

		add(new FileFieldInput("Submitter Script",
				ConfigurationPropertyConstants.SUBMITTER_SCRIPT_FILENAME,
				properties));

		add(new FileFieldInput("SSHFS mounted iridis home directory",
				ConfigurationPropertyConstants.IRIDIS_HOME_DIR, properties));

	}

	@Override
	protected String getConfigurationPropertyPrefix() {
		return ConfigurationPropertyConstants.IRIDIS_TEMPLATE_PREFIX;
	}

}
