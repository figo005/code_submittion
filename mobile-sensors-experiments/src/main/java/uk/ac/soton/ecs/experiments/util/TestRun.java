package uk.ac.soton.ecs.experiments.util;

import uk.ac.soton.ecs.mobilesensors.configuration.Experiment;
import uk.ac.soton.ecs.mobilesensors.configuration.ExperimentRunner;

public class TestRun {
	public static void main(String[] args) throws Exception {
		System.setProperty("log4j.configuration", "log4j_mobile_sensors.xml");

		// String sensorFile =
		// "src/main/resources/sensor/sensor_max_sum_path_central_no_pruning.xml";
		String sensorFile = "src/main/resources/sensor/new/sensor_greedy.xml";

		String simulationFile = "src/main/resources/simulation/new/large-room-probabilistic_pursuit.xml";
		int sensorCount = 1;
		String prefix = "newexperiments/";

		int repetition = 1;

		runExperiment(simulationFile, prefix + "probpre", sensorFile,
				sensorCount, repetition);
	}

	private static void runExperiment(String simulationFile, String outputDir,
			String sensorFile, int sensorCount, int repetition) {
		for (int i = 0; i < repetition; i++) {
			try {
				runExperiment(simulationFile, outputDir, sensorFile,
						sensorCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void runExperiment(String simulationFile, String outputDir,
			String sensorFile, int sensorCount) throws Exception {

		Experiment experiment = ExperimentRunner.createExperiment(outputDir,
				sensorFile, simulationFile, sensorCount, false);
		experiment.initialize();

		experiment.getSimulation().addEventListener(new HeatMapListener());

		experiment.run();

	}
}
