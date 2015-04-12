package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

public interface BatchRunnerListener {

	void batchJobFinished(BatchJob batchJob, int success);

}
