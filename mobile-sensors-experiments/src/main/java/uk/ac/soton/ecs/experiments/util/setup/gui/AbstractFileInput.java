package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import org.apache.commons.configuration.Configuration;

public abstract class AbstractFileInput extends AbstractTextFieldInput {

	private File currentDirectory;

	private Properties properties;

	private String propertyName;

	public AbstractFileInput(String title, boolean directoriesOnly,
			String propertyName, Configuration properties) {
		super(title, propertyName, properties);

		currentDirectory = new File(textField.getText()).getParentFile();

		final JFileChooser fc = new JFileChooser();
		if (currentDirectory != null)
			fc.setCurrentDirectory(currentDirectory);

		if (directoriesOnly)
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		else
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		final JButton openButton = new JButton("File...");
		openButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == openButton) {
					int returnVal = fc.showOpenDialog(AbstractFileInput.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						currentDirectory = file.getParentFile();
						textField.setText(file.getAbsolutePath());
					}
				}
			}
		});

		add(openButton);
	}
}
