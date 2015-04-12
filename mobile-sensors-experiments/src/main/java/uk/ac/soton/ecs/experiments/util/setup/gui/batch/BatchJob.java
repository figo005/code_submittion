package uk.ac.soton.ecs.experiments.util.setup.gui.batch;

public interface BatchJob {

	String getDescription();

	void run() throws Exception;

}
