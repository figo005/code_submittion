package uk.ac.soton.ecs.experiments.gui;

import org.apache.commons.lang.time.StopWatch;

import uk.ac.soton.ecs.mobilesensors.configuration.Experiment;

public class SimulationThread extends Thread {

	private final Experiment experiment;

	private boolean running = true;

	private double delay = 0;

	public SimulationThread(Experiment experiment, double delay) {
		this.experiment = experiment;
		setDelay(delay);
	}

	@Override
	public void run() {
		try {
			while (running) {
				StopWatch watch = new StopWatch();
				watch.start();
				experiment.runSingleRound();
				long sleepTime = (int) (delay * 1000 - watch.getTime());

				if (sleepTime > 0)
					Thread.sleep(sleepTime);

				if (experiment.isFinished())
					running = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopRunning() {
		running = false;
	}

	public void setDelay(double d) {
		this.delay = d;
	}
}
