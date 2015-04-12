package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.InformationValueFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.RewardFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import uk.ac.soton.ecs.utils.RandomUtils;

public class GlobalRandomCoordinationMechanismNew2 extends
		AbstractCoordinationMechanism {
	private int locationsCount;
	private int tau;
	private double uncertaintyIncrement;
	private final int eachMoveDuration=1;
	private HealthyBelief healthyIni;
	private RewardFrame rewardFrame;
	private InformationValueFrame informationValueFrame;
	
	private StandardTransitionFunctionNew transitionFunction;

	private Move nextAction;
	private Iterator<AccessibilityRelation> currentPath;


	public Move determineBestMove(double time) {
		

		if (currentPath == null || !currentPath.hasNext()) {

			Location location = RandomUtils.getRandomElement(getGraph()
					.getLocations());

			List<AccessibilityRelation> shortestPath = getGraph()
					.getShortestPath(getCurrentLocation(), location);

			currentPath = shortestPath.iterator();

			// we're already at the best location
			if (shortestPath.isEmpty()) {
				return new Move(getCurrentLocation());
			}
		}

		AccessibilityRelation next = currentPath.next();
		
		nextAction=new Move(next.getOther(getCurrentLocation()));
		
		return nextAction;

	}

	public void initialize(Simulation simulation) {
		// TODO Auto-generated method stub
//		healthyIni=sensor.getHealthyIni();
//		tau = getInformativenessFunction().getTau();
//		uncertaintyIncrement=getInformativenessFunction().getUncertaintyIncrement();
		locationsCount=getGrid().getGridPointCount();
		

//		StateNew iniState=getInitialState(locationsCount);


		
//		transitionFunction=new StandardTransitionFunctionNew(locationsCount,
//				tau, eachMoveDuration);
//		
//		int lastVisitTimes[]=sensor.getCurrentState().getLastVisitTimes();	
//		double informationValue[] = new double[lastVisitTimes.length];
//		for(int i=0;i<lastVisitTimes.length;i++)
//			informationValue[i]=uncertaintyIncrement*lastVisitTimes[i];
//		rewardFrame = new RewardFrame(sensor.getObservationValue(),sensor.getRealHealth(),healthyIni);
//
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

	public void updateFrame() {
		// TODO Auto-generated method stub
		
	}
}
