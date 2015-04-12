package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import uk.ac.soton.ecs.experiments.util.setup.ConsoleUtil;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.BatchRunner;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.BatchRunnerDialog;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.BuildMobileJar;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CopyGraphFiles;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CopyJarFile;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CopySensorFiles;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CopySubmitterScript;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CreateDirectoryStructure;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CreateIridisScripts;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CreateSimulationConfigFiles;
import uk.ac.soton.ecs.experiments.util.setup.gui.batch.CreateUberSubmitterFile;

public class GUI extends JFrame implements ActionListener {
	private static final String START = "start";
	private static final String PRINT_CONFIG = "print_config";
	private File experimentPropertiesFile = new File(ConsoleUtil
			.getUserDirectory(), ".experiment_config");

	private PropertiesConfiguration properties;

	public GUI() throws IOException, ConfigurationException {
		setTitle("Experiment Setup");

		JTabbedPane tabbedPane = new JTabbedPane();

		getContentPane().setLayout(new BorderLayout());

		if (!experimentPropertiesFile.exists()) {
			experimentPropertiesFile.createNewFile();
		}

		properties = new PropertiesConfiguration(experimentPropertiesFile);

		JComponent panel0 = new GeneralConfigurationPanel(properties);
		tabbedPane.addTab("General", null, panel0);

		JComponent panel1 = new ExperimentConfigurationPanel(properties);
		tabbedPane.addTab("Simulation", null, panel1);

		JComponent panel2 = new IridisConfigurationPanel(properties);
		tabbedPane.addTab("Iridis", null, panel2);

		JComponent sensorSelectionPanel = new SensorSelectionPanel(properties);
		tabbedPane.addTab("Sensors", null, sensorSelectionPanel);

		// JComponent graphGridPanel = new GraphGridPanel(properties);
		// tabbedPane.addTab("Sensors", null, graphGridPanel);

		JComponent panel3 = new MobileJarFileConfigurationPanel(properties);
		tabbedPane.addTab("Jar File", null, panel3);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		getContentPane().add(createToolBar(), BorderLayout.PAGE_START);

		setPreferredSize(new Dimension(800, 800));

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				try {
					properties.save(experimentPropertiesFile);
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private Component createToolBar() {
		JToolBar toolBar = new JToolBar("Still draggable");
		JButton startButton = makeNavigationButton(null, START,
				"Start Processing", "Start");

		toolBar.add(startButton);

		JButton printButton = makeNavigationButton(null, PRINT_CONFIG, "",
				"Print");

		toolBar.add(printButton);

		return toolBar;
	}

	protected JButton makeNavigationButton(String imageName,
			String actionCommand, String toolTipText, String altText) {
		// Look for the image.
		String imgLocation = "images/" + imageName + ".gif";
		URL imageURL = GUI.class.getResource(imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found
			button.setText(altText);
			System.err.println("Resource not found: " + imgLocation);
		}

		return button;
	}

	public static void main(String[] args) throws ConfigurationException,
			IOException {
		GUI gui = new GUI();
		gui.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(START)) {
			startProcessing();
		}
		if (e.getActionCommand().equals(PRINT_CONFIG)) {
			Iterator<String> keys = properties.getKeys();

			while (keys.hasNext()) {
				String next = keys.next();
				System.out.println(next + " " + properties.getString(next));
			}
		}
	}

	private void startProcessing() {
		BatchRunner batchRunner = new BatchRunner();

		batchRunner.addBatchJob(new BuildMobileJar(properties));
		batchRunner.addBatchJob(new CreateDirectoryStructure(properties));
		batchRunner.addBatchJob(new CopySensorFiles(properties));
		batchRunner.addBatchJob(new CreateSimulationConfigFiles(properties));
		batchRunner.addBatchJob(new CopyGraphFiles(properties));
		batchRunner.addBatchJob(new CreateIridisScripts(properties));
		batchRunner.addBatchJob(new CopyJarFile(properties));
		batchRunner.addBatchJob(new CopySubmitterScript(properties));
		batchRunner.addBatchJob(new CreateUberSubmitterFile(properties));

		// batchRunner.addBatchJob(new TarExperimentDir(properties));

		// batchRunner.addBatchJob(new
		// CopyDirectoryToIridisHomeDir(properties));
		// batchRunner.addBatchJob(new SubmitJobs(properties));

		BatchRunnerDialog dialog = new BatchRunnerDialog(batchRunner);

		dialog.setVisible(true);

		batchRunner.start();
	}
}
