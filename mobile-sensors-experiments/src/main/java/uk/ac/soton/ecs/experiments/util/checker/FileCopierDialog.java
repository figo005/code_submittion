package uk.ac.soton.ecs.experiments.util.checker;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JDialog;

import ch.fhnw.filecopier.FileCopier;
import ch.fhnw.filecopier.FileCopierPanel;

public class FileCopierDialog extends JDialog {

	private FileCopierPanel fileCopierPanel;

	public FileCopierDialog(GUI gui) {
		super(gui);

		fileCopierPanel = new FileCopierPanel();

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(fileCopierPanel,
				GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(fileCopierPanel,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE).addContainerGap(19,
						Short.MAX_VALUE)));

		getContentPane().add(fileCopierPanel);
		setPreferredSize(new Dimension(350, 100));
		pack();
	}

	public void setFileCopier(FileCopier fileCopier) {
		fileCopierPanel.setFileCopier(fileCopier);
	}

}
