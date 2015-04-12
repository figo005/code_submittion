package uk.ac.soton.ecs.experiments.util.setup.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.SwingWorker;

public class LogFileDialog extends JDialog {

	private TextArea textArea = new TextArea();

	private Runnable cleanUpRunner;

	public LogFileDialog(InputStream inputStream, String title) {
		this(inputStream, null, false, title);
	}

	public LogFileDialog(InputStream inputStream, Runnable cleanupRunner,
			final boolean closeOnFinish, String title) {
		setTitle(title);
		InputStreamReader isr = new InputStreamReader(inputStream);
		final BufferedReader br = new BufferedReader(isr);

		this.cleanUpRunner = cleanupRunner;

		getContentPane().add(textArea);
		textArea.setPreferredSize(new Dimension(500, 500));

		textArea.setEditable(false);
		textArea.setFont(Font.getFont(Font.MONOSPACED));

		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String line = null;
				while ((line = br.readLine()) != null)
					append(line);

				return null;
			}

			@Override
			protected void done() {
				if (cleanUpRunner != null)
					cleanUpRunner.run();

				if (closeOnFinish)
					setVisible(false);
			}

		}.execute();

		pack();
	}

	public void append(String string) {
		textArea.append(string + "\n");
	}

}
