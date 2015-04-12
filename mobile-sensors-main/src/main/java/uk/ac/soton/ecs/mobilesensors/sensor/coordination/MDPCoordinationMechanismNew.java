package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.Policy;
import aima.core.probability.mdp.impl.ModifiedPolicyEvaluation;
import aima.core.probability.mdp.search.PolicyIteration;
import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.FiniteBackTracking;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.HierarchicalState;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.MultiSensorState;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.ReachableStateSpaceGraph;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.RewardFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.InformationValueFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.SensorPositionState;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.StandardTransitionFunctionNew;
import uk.ac.soton.ecs.utils.RandomUtils;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.MDPFactory;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public class MDPCoordinationMechanismNew extends
		AbstractCoordinationMechanism {
	
	private int locationsCount;
	private int tau;
	private final int backTrackingStepsNumber=8;
	private final double gama=0.9;
	private double uncertaintyIncrement;
	private final int eachMoveDuration=1;
	private int healthyDecrement;
	private HealthyBelief healthyIni;
	private Collection<Location> riskLocations;

	private Iterator<AccessibilityRelation> currentPath;
	private RewardFrame rewardFrame;
	private InformationValueFrame informationValueFrame;
	
	private Policy<StateNew, Move> policy;
//	private StateNew currentState;
	private List<StateNew> stateSpace;
	private StandardTransitionFunctionNew transitionFunction;
	private FiniteReachableStateSpaceGraphNew<StateNew, Move> stateSpaceGraph;
	private FiniteBackTracking<StateNew, Move> finiteBackTracking;
	private Move nextAction;

	public Move determineBestMove(double time) {

	
		StateNew currentState = sensor.getCurrentState();
		nextAction=finiteBackTracking.doBackTracking(currentState); 
		
//		Collection<StateNew> successors=finiteBackTracking.getSuccessors(currentState);
//		
//		for(StateNew successor:successors)
//		{
//			if(successor.location.equals(nextAction.getTargetLocation()))
//			{
//				sensor.setCurrentState(successor);
//				break;
//			}
//		}
		
		int lastVisitTimes[]=currentState.getLastVisitTimes();
		int[] newVisitTimes = new int[lastVisitTimes.length];
		for (int i = 0; i < lastVisitTimes.length; i++) {

			newVisitTimes[i] = lastVisitTimes[i]
					+ eachMoveDuration;
			}


		newVisitTimes[currentState.getLocation().getID()] = 1;
		
		double informationValue[] = new double[lastVisitTimes.length];
		for(int i=0;i<lastVisitTimes.length;i++)
			informationValue[i]=uncertaintyIncrement*newVisitTimes[i];
		
		
		informationValueFrame.updateMessage(informationValue,nextAction.getTargetLocation().getID());
		rewardFrame.updateMessage(sensor.getObservationValue(), sensor.getRealHealth(),sensor.getCurrentState().getHealthyLevelBelief());
		
		return nextAction;
	}


	public void initialize(Simulation simulation) {
		
//		healthyIni=sensor.getHealthyIni();
//		healthyDecrement=sensor.getHealthyDecrement();
		tau = getInformativenessFunction().getTau();
		
		uncertaintyIncrement=getInformativenessFunction().getUncertaintyIncrement();
		
		riskLocations=getGrid().getRiskLocations();
		locationsCount=getGrid().getGridPointCount();


				
		transitionFunction=new StandardTransitionFunctionNew(locationsCount,
				tau, eachMoveDuration,getInformativenessFunction().getHurtProbability());
		

		
		StateNew iniState=getInitialState(locationsCount);


		sensor.setCurrentState(iniState);
		finiteBackTracking=new FiniteBackTracking<StateNew, Move>(transitionFunction,backTrackingStepsNumber,gama,uncertaintyIncrement);

		
		int lastVisitTimes[]=sensor.getCurrentState().getLastVisitTimes();		
		double informationValue[] = new double[lastVisitTimes.length];
		for(int i=0;i<lastVisitTimes.length;i++)
			informationValue[i]=uncertaintyIncrement*lastVisitTimes[i];
		
		
		
		
		rewardFrame = new RewardFrame(sensor.getObservationValue(),sensor.getRealHealth());

		informationValueFrame=new InformationValueFrame(informationValue,sensor.getCurrentState().getLocation().getID());
		sensor.getCurrentState().getLastVisitTimes()[0]=0;
//		sensor.setCurrentState();
		
//		 TODO Auto-generated method stub

	}
	
	public StateNew getInitialState(int locationsCount) {
		Location initialPosition = sensor.getLocation();
		int[] lastVisitTimes=new int[locationsCount];
		for(int i=0;i<locationsCount;i++)
		{
			lastVisitTimes[i]=tau;
		}
		
		StateNew state=new StateNew(initialPosition,lastVisitTimes,new HealthyBelief(sensor.getRealHealth()));

		return state;
	}
	
//	private StateNew getCurrentState() {
//		return currentState;
//	}
//	
//	protected void setCurrentState(StateNew next) {
//		currentState = next;
//	}
	
	protected ObservationInformativenessFunction getInformativenessFunction() {
		ObservationInformativenessFunction informativenessFunction = sensor.getEnvironment().getInformativenessFunction();
		return informativenessFunction;
	}
}
