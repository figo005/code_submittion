package uk.ac.soton.ecs.experiments.util.setup.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.commons.configuration.Configuration;

public class GraphGridPanel extends JPanel {

	public GraphGridPanel(Configuration properties) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// add(new TextFieldInput("Grid File",
		// ConfigurationPropertyConstants.GRID_FILE, properties));

		add(new DirectoryFieldInput("Graph File",
				ConfigurationPropertyConstants.GRAPH_FILE_NAME, properties));

	}

}
