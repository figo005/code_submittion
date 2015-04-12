package uk.ac.soton.ecs.experiments.gui;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1584424803344121019L;

	private File currentDirectory;

	private TextField fileNameTextField = new TextField(60);

	public FileChooser(String title, boolean directoriesOnly,
			String defaultFileName) {
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(title), BorderFactory.createEmptyBorder(5,
				5, 5, 5)));

		setLayout(new FlowLayout());

		add(fileNameTextField);
		fileNameTextField.setText(defaultFileName);

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
					int returnVal = fc.showOpenDialog(FileChooser.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						currentDirectory = file.getParentFile();
						fileNameTextField.setText(file.getAbsolutePath());
					}
				}
			}
		});

		add(openButton);

	}

	public File getFile() {
		return new File(fileNameTextField.getText());
	}
}
