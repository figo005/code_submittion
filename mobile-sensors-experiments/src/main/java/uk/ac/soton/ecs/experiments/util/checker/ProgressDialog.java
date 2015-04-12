package uk.ac.soton.ecs.experiments.util.checker;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {

	public ProgressDialog(Frame owner, String title) {
		super(owner);

		setTitle(title);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setString(title);
		progressBar.setIndeterminate(true);
		getContentPane().add(progressBar);
		Dimension size = owner.getSize();
		setLocation(new Point((int) size.getHeight() / 2,
				(int) size.getWidth() / 2));
		pack();
		setVisible(true);
	}

}
