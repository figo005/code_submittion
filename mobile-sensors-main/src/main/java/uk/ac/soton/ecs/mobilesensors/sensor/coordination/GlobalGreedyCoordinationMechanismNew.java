package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.InformationValueFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.RewardFrame;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public class GlobalGreedyCoordinationMechanismNew extends
		AbstractCoordinationMechanism {

//	private int locationsCount;
//	private int tau;
//	private double uncertaintyIncrement;
//	private final int eachMoveDuration=1;
//	private HealthyBelief healthyIni;
	private RewardFrame rewardFrame;
	private InformationValueFrame informationValueFrame;
	
//	private StandardTransitionFunctionNew transitionFunction;

	private Move nextAction;	
	
	
	
	public Move determineBestMove(double time) {
		
		StateNew currentState = sensor.getCurrentState();
		
		ObservationInformativenessFunction function = getInformativenessFunction();
		Point2D point = function.getMaximumInformativeLocation();
//		function.getValues();
		Location location = getGraph().getLocation(point);

		List<AccessibilityRelation> shortestPath = getGraph().getShortestPath(
				getCurrentLocation(), location);

		// we're already at the best location
		if (shortestPath.isEmpty()) {
			return new Move(getCurrentLocation());
		}
		nextAction=new Move(shortestPath.get(0).getOther(getCurrentLocation()));
		
		
//		informationValueFrame.updateMessage(informationValue,sensor.getCurrentState().getLocation().getID());
//		rewardFrame.updateMessage(sensor.getObservationValue(), sensor.getCurrentState().getHealthyLevelBelief());

		return nextAction;
	}

	public void initialize(Simulation simulation) {
		// TODO Auto-generated method stub
//		healthyIni=sensor.getHealthyIni();
		tau = getInformativenessFunction().getTau();
		uncertaintyIncrement=getInformativenessFunction().getUncertaintyIncrement();
		locationsCount=getGrid().getGridPointCount();
		StateNew iniState=getInitialState(locationsCount);


		sensor.setCurrentState(iniState);
		
		transitionFunction=new StandardTransitionFunctionNew(locationsCount,
				tau, eachMoveDuration);
		
		
		int lastVisitTimes[]=sensor.getCurrentState().getLastVisitTimes();	
		double informationValue[] = new double[lastVisitTimes.length];
		for(int i=0;i<lastVisitTimes.length;i++)
			informationValue[i]=uncertaintyIncrement*lastVisitTimes[i];
//		rewardFrame = new RewardFrame(sensor.getObservationValue(),healthyIni);

//		informationValueFrame=new InformationValueFrame(informationValue,sensor.getCurrentState().getLocation().getID());

	}
	public StateNew getInitialState(int locationsCount) {
		Location initialPosition = sensor.getLocation();
		int[] lastVisitTimes=new int[locationsCount];
		for(int i=0;i<locationsCount;i++)
		{
			lastVisitTimes[i]=tau;
		}
		StateNew state=new StateNew(initialPosition,lastVisitTimes,healthyIni);

		return state;
	}
	protected ObservationInformativenessFunction getInformativenessFunction() {
		ObservationInformativenessFunction informativenessFunction = sensor.getEnvironment().getInformativenessFunction();
		return informativenessFunction;
	}

}
