package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BatchRunner extends Thread {

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
	private List<BatchJob> batchJobs = new ArrayList<BatchJob>();
	private Collection<BatchRunnerListener> listeners = new ArrayList<BatchRunnerListener>();;

	public BatchRunner() {
		batchJobs = new ArrayList<BatchJob>();
	}

	public void addBatchJob(BatchJob job) {
		batchJobs.add(job);
	}

	@Override
	public void run() {

		for (BatchJob batchJob : batchJobs) {
			System.out.println("Running " + batchJob.getDescription());

			try {
				batchJob.run();
				notifyListeners(batchJob, SUCCESS);
			} catch (Exception e) {
				e.printStackTrace();
				notifyListeners(batchJob, FAILED);
				break;
			}
		}
	}

	private void notifyListeners(BatchJob batchJob, int success) {
		for (BatchRunnerListener listener : listeners) {
			listener.batchJobFinished(batchJob, success);
		}
	}

	public void addListener(BatchRunnerListener listener) {
		listeners.add(listener);

	}

	public Collection<BatchJob> getJobs() {
		return batchJobs;
	}
}
