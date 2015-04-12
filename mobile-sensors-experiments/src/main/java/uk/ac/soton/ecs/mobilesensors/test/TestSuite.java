package uk.ac.soton.ecs.mobilesensors.test;

import uk.ac.soton.ecs.mobilesensors.configuration.Experiment;
import uk.ac.soton.ecs.mobilesensors.configuration.ExperimentRunner;

public class TestSuite {
	public static void main(String[] args) throws Exception {
		String outputDir = "demo";
		String sensorFile = "src/main/resources/sensor/sensor_max_sum.xml";
		String simulationFile = "src/main/resources/simulation/large-room.xml";
		int sensorCount = 2;

		Experiment experiment = ExperimentRunner.createExperiment(outputDir,
				sensorFile, simulationFile, sensorCount, false);
		experiment.initialize();

		experiment.runSingleRound();
		experiment.runSingleRound();
	}
}
