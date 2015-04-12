package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.configuration.Configuration;

public class SensorSelectionPanel extends JPanel {

	private Configuration configuration;

	private File currentDirectory;

	private JList sensorList;

	private FileListModel model;

	public SensorSelectionPanel(final Configuration configuration) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		this.configuration = configuration;

		sensorList = new JList();
		model = new FileListModel();
		sensorList.setModel(model);

		final JFileChooser fc = new JFileChooser();
		if (currentDirectory != null)
			fc.setCurrentDirectory(currentDirectory);

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);

		final JButton openButton = new JButton("File...");
		openButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == openButton) {
					int returnVal = fc
							.showOpenDialog(SensorSelectionPanel.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File[] files = fc.getSelectedFiles();

						if (files.length > 0) {
							currentDirectory = files[0].getParentFile();

							for (File file : files)
								addToList(file);

							model.update();
						}
					}
				}
			}
		});

		JButton delete = new JButton("Remove");
		delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int[] selectedIndices = sensorList.getSelectedIndices();

				List<File> deleteFiles = new ArrayList<File>();

				for (int index : selectedIndices) {
					deleteFiles.add(model.getFile(index));
				}

				for (File file : deleteFiles) {
					removeFromList(file);
				}
				model.update();
			}
		});

		add(new JScrollPane(sensorList));
		add(openButton);
		add(delete);
	}

	protected void removeFromList(File file) {
		List<String> list = configuration
				.getList(ConfigurationPropertyConstants.SENSOR_FILES);
		list.remove(file.getAbsolutePath());
		configuration.setProperty(ConfigurationPropertyConstants.SENSOR_FILES,
				list);
	}

	protected void addToList(File file) {
		List<String> list = configuration
				.getList(ConfigurationPropertyConstants.SENSOR_FILES);
		list.add(file.getAbsolutePath());
		configuration.setProperty(ConfigurationPropertyConstants.SENSOR_FILES,
				list);
	}

	private class FileListModel extends DefaultListModel {

		public Object getElementAt(int index) {
			return configuration.getList(
					ConfigurationPropertyConstants.SENSOR_FILES).get(index);
		}

		public File getFile(int index) {
			return new File((String) getElementAt(index));
		}

		public void update() {
			fireContentsChanged(this, 0, getSize() - 1);
		}

		public int getSize() {
			return configuration.getList(
					ConfigurationPropertyConstants.SENSOR_FILES).size();
		}
	}
}
