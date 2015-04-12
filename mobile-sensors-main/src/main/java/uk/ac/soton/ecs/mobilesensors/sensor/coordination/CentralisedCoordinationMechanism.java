package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;

public class CentralisedCoordinationMechanism extends
		AbstractCoordinationMechanism {

	private CentralisedController controller;

	@Required
	public void setControllerFactory(
			CentralisedControllerFactory controllerFactory) {
		controller = controllerFactory.getController();
	}

	public Move determineBestMove(double time) {
		return controller.getBestMove(getSensor());
	}

	public void initialize(Simulation simulation) {
		controller.register(getSensor());
		controller.setSimulation(simulation);
	}

	public CentralisedController getController() {
		return controller;
	}

	public void updateFrame() {
		// TODO Auto-generated method stub
		controller.updateFrame();
	}

	public void delete() {
		// TODO Auto-generated method stub
		controller.deleteSensors();

	}
	
	public void reset() {
		// TODO Auto-generated method stub
		controller.resetController();
	}
}
