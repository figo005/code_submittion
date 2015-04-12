package uk.ac.soton.ecs.experiments.util;

import javax.swing.JFrame;

import uk.ac.soton.ecs.experiments.gui.HeatMapFrame;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.SimulationEventListener;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public class HeatMapListener extends JFrame implements SimulationEventListener {

	private HeatMapFrame hmf;

	public HeatMapListener() {

	}

	public void handleEndOfRound(Simulation source, int round, double timestep) {
		ObservationInformativenessFunction informativenessFunction = source
				.getEnvironment().getInformativenessFunction();

		hmf.updateData(informativenessFunction.getValues());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void handleEndOfSimulation(Simulation source) throws Exception {
	}

	public void handleStartOfRound(Simulation source, int round, double timestep) {
	}

	public void handleStartOfSimulation(Simulation source, double time) {
		hmf = new HeatMapFrame(source.getEnvironment().getGrid());
		hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hmf.setSize(500, 500);
		hmf.setVisible(true);
	}

}
