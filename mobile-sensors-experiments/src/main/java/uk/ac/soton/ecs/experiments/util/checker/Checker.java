package uk.ac.soton.ecs.experiments.util.checker;

import java.io.File;

import javax.swing.JFileChooser;

public class Checker {

	public static void main(String[] args) {

		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(null);

		File[] files = null;

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			files = fc.getSelectedFiles();

		} else {
			System.exit(0);
		}

		ExperimentsRootDirectory root = new ExperimentsRootDirectory(files);

		GUI gui = new GUI(root);

		gui.setVisible(true);
	}
}
