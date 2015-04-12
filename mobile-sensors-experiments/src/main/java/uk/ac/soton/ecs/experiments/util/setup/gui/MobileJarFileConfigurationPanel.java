package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.commons.configuration.Configuration;

import uk.ac.soton.ecs.experiments.util.setup.BuildMobileSensorJarFileCommand;

public class MobileJarFileConfigurationPanel extends JPanel implements
		ActionListener {

	private static final String REBUILD = "rebuild";
	private static final String REBUILD_DURING_BATCH = "rebuild_during_batch";
	private TextFieldInput mobileSensorsProjectDir;
	private Configuration properties;

	public MobileJarFileConfigurationPanel(Configuration properties) {

		this.properties = properties;

		// have label that checks existence and time of compilation
		// make sure the iridis script uses the same name as the jar file

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		mobileSensorsProjectDir = new TextFieldInput(
				"Mobile Sensors Project Directory",
				ConfigurationPropertyConstants.MOBILE_SENSORS_PROJECT_DIR,
				properties, BuildMobileSensorJarFileCommand.DEFAULT_PROJECT_DIR
						.getAbsolutePath());

		LastBuiltPanel lastBuilt = new LastBuiltPanel(mobileSensorsProjectDir);

		JPanel rebuildPanel = new JPanel();

		rebuildPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Rebuild"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		rebuildPanel.setLayout(new FlowLayout());

		JButton button = new JButton();
		button.setText("Rebuild Now");
		button.setActionCommand(REBUILD);
		button.addActionListener(this);
		rebuildPanel.add(button);

		JCheckBox checkBox = new JCheckBox("Rebuild during batch processing");
		checkBox.setActionCommand(REBUILD_DURING_BATCH);
		checkBox.addActionListener(this);

		rebuildPanel.add(checkBox);

		add(mobileSensorsProjectDir);
		add(lastBuilt);
		add(rebuildPanel);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(REBUILD)) {
			rebuildMobileJar();
		}
		if (e.getActionCommand().equals(REBUILD_DURING_BATCH)) {
			System.out.println("test");

			properties.setProperty(
					ConfigurationPropertyConstants.REBUILD_JAR_DURING_BATCH,
					((JCheckBox) e.getSource()).isSelected());
		}
	}

	private void rebuildMobileJar() {

		SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				Process buildJarFileProcess = BuildMobileSensorJarFileCommand
						.createBuildJarFileProcess(new File(
								mobileSensorsProjectDir.getText()));

				LogFileDialog dialog = new LogFileDialog(buildJarFileProcess
						.getInputStream(), "Rebuilding JAR file");

				dialog.setVisible(true);

				int waitFor = buildJarFileProcess.waitFor();

				if (waitFor == 0) {
					dialog.append("Build successfull");
				} else {
					dialog.append("Build failed");
				}

				return null;
			}
		};

		swingWorker.execute();

	}
}
