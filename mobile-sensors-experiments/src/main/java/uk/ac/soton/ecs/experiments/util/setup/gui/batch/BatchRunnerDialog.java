package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class BatchRunnerDialog extends JDialog implements BatchRunnerListener {

	private BatchRunner runner;

	private Map<BatchJob, JLabel> labels = new HashMap<BatchJob, JLabel>();

	public BatchRunnerDialog(BatchRunner runner) {
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		setTitle("Processing Jobs");
		this.runner = runner;

		for (BatchJob job : runner.getJobs()) {
			JLabel label = new JLabel(job.getDescription());
			labels.put(job, label);
			getContentPane().add(label);
		}

		runner.addListener(this);

		setPreferredSize(new Dimension(500, 500));
		pack();
	}

	public void batchJobFinished(BatchJob batchJob, int status) {
		if (status == BatchRunner.SUCCESS) {
			labels.get(batchJob).setForeground(Color.GREEN);
		}
		if (status == BatchRunner.FAILED) {
			labels.get(batchJob).setForeground(Color.RED);
		}
	}
}
