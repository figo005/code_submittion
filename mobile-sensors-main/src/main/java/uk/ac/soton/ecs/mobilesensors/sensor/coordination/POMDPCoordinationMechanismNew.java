package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javolution.util.FastMap;

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
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.BeliefFrame;
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

public class POMDPCoordinationMechanismNew extends
		AbstractCoordinationMechanism {
	
	private int locationsCount;
//	private int tau;
//	private double uncertaintyIncrement;
//	private final int eachMoveDuration=1;
//	private HealthyBelief healthyIni;
//	private RewardFrame rewardFrame;
//	private InformationValueFrame informationValueFrame;
	private final double gama=0.8;
//	private StandardTransitionFunctionNew transitionFunction;

	private Move nextAction;
//	private Iterator<AccessibilityRelation> currentPath;
	
	private Map<Integer, double[]> riskBeliefs = new HashMap<Integer, double[]>();
	
	private Map<Integer, double[]> informationBeliefs = new HashMap<Integer, double[]>();
	
	private  double markovRiskModel[][];
	
	private  double markovInformationModel[][];
	
	private BeliefFrame beliefFrame;

	public Move determineBestMove(double time) {

//		updateBeliefs(sensor.getLocation(), sensor.getRiskObservationForCurrentPosition(),
//				sensor.getInformationObservationForCurrentPosition());
		
//		nextAction=myopicPolicy(sensor.getLocation(),5-1);
		nextAction=myopicPolicy(sensor.getLocation());
//		if (currentPath == null || !currentPath.hasNext()) {
//
//			Location location = RandomUtils.getRandomElement(getGraph()
//					.getLocations());
//
//			List<AccessibilityRelation> shortestPath = getGraph()
//					.getShortestPath(getCurrentLocation(), location);
//
//			currentPath = shortestPath.iterator();
//
//			// we're already at the best location
//			if (shortestPath.isEmpty()) {
//				return new Move(getCurrentLocation());
//			}
//		}
//
//		AccessibilityRelation next = currentPath.next();
		
//		nextAction=new Move(next.getOther(getCurrentLocation()));

		
		return nextAction;
	}
	
	public void updateFrame()
	{
		updateBeliefs(sensor.getLocation(), sensor.getRiskObservationForCurrentPosition(),
				sensor.getInformationObservationForCurrentPosition());
		
		beliefFrame.updateMessage(riskBeliefs,informationBeliefs,
				getValuesFromBeliefStates(getGraph().getLocations(),riskBeliefs,informationBeliefs),
				getCurrentLocation().getID());
		
	}


	public void initialize(Simulation simulation) {
		

		
		riskBeliefs=getGraph().getRiskBeliefsIni();
		informationBeliefs=getGraph().getInformationBeliefsIni();		
		markovRiskModel=getGraph().getMarkovRiskModel();	
		markovInformationModel=getGraph().getMarkovInformationModel();
		locationsCount=getGrid().getGridPointCount();
		
		updateBeliefs(sensor.getLocation(), sensor.getRiskObservationForCurrentPosition(),
				sensor.getInformationObservationForCurrentPosition());
		beliefFrame=new BeliefFrame(riskBeliefs,informationBeliefs,
				getValuesFromBeliefStates(getGraph().getLocations(),riskBeliefs,informationBeliefs),
				getCurrentLocation().getID());

		
//		 TODO Auto-generated method stub

	}
	
	protected ObservationInformativenessFunction getInformativenessFunction() {
		ObservationInformativenessFunction informativenessFunction = sensor.getEnvironment().getInformativenessFunction();
		return informativenessFunction;
	}
	
	
	private Move myopicPolicy(Location location)
	{
		
		
//		for (Location neighbour : location.getNeighbours())
			
		Map<Integer, Double> valuesofNeighbours=getValuesFromBeliefStates(location.getNeighbours(),
				 riskBeliefs,informationBeliefs);
		

		double maxValue = Double.NEGATIVE_INFINITY;
		int bestLoationID=0;
//		Set<Integer> keys=valuesofNeighbours.keySet();

		Iterator iter = valuesofNeighbours.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			
			if ((Double)entry.getValue() > maxValue) {
				maxValue = (Double)entry.getValue();
				bestLoationID = (Integer)entry.getKey();
			}

		} 
		


		
//		Map<Integer, Double> expectedValues=getValuesFromBeliefStates();
		
		
		return new Move(getGraph().getLocationFromID(bestLoationID));
	}
	
	private Move myopicPolicy(Location location, int backTrackingStepsNumber)
	{
		
		ArrayList<ArrayList<Location>> cur_pathes=new ArrayList<ArrayList<Location>>();
		
		for(Location neighbor:location.getNeighbours())
		{
			ArrayList<Location> temp=new ArrayList<Location>();
			temp.add(neighbor);
			cur_pathes.add(temp);
		}
		
//		ArrayList<ArrayList<S>> visited_states = new ArrayList<ArrayList<S>>(backTrackingStepsNumber);

		//First, find all possible paths.
		
		for (int steps_to_go = backTrackingStepsNumber; steps_to_go > 0 ; steps_to_go--)
		{
			ArrayList<ArrayList<Location>> found_pathes=new ArrayList<ArrayList<Location>>();
			
			for (ArrayList<Location> path : cur_pathes) {
				Location lastLocation=path.get(path.size()-1);
				
				List<Location> neighbours=lastLocation.getNeighbours();
				for (Location nextlocation : neighbours) {
					ArrayList<Location> newpath=new ArrayList<Location>();
														
					newpath.addAll(path);
					newpath.add(nextlocation);
					found_pathes.add(newpath);
				}
				
			}
			cur_pathes.clear();
			cur_pathes.addAll(found_pathes);
			
			
			
		}
		
		//Second, calculate expected value for each path. Meanwhile get the path of maxvalue;
		double maxValue = Double.NEGATIVE_INFINITY;
		ArrayList<Location> bestPath=new ArrayList<Location>();
		for (ArrayList<Location> path : cur_pathes) {
			
			ArrayList<BeliefState> currentLeafsBelief =new ArrayList<BeliefState>();
			
		
			currentLeafsBelief.add( new BeliefState(1.0,riskBeliefs,informationBeliefs));
			
			double pathExpReward=0;
			
			double discountfactor=1.0;
			
			for(Location node:path)
				
			{
//				Map<Double, BeliefState> foundLeafsBeliefs =new HashMap<Double, BeliefState>();
				ArrayList<BeliefState> foundLeafsBeliefs=new ArrayList< BeliefState>();
				
				
				for (int i = currentLeafsBelief.size() -1 ; i >= 0; i--) {
					{

						BeliefState belief = currentLeafsBelief.get(i);	
						

						
						pathExpReward+=discountfactor*belief.getProbability()*getValuesFromBeliefStates_oneLocation(node,belief.getRiskStateBielfSet(),belief.getInforamtionStateBielsfSet());
						
						for(int riskObservation=0;riskObservation <markovRiskModel.length ; riskObservation++)
						{
							BeliefState beliefNext;
							beliefNext=updateBeliefs(node, riskObservation, belief);
							foundLeafsBeliefs.add(beliefNext);
						}
					}
						
					}
 
				currentLeafsBelief.clear();
				currentLeafsBelief.addAll(foundLeafsBeliefs);
				
				discountfactor*=gama;
				
			}
			
			if (pathExpReward > maxValue) {
				maxValue = pathExpReward;
				bestPath = path;
			}
			


			
		}
		
		
			
		
		
		return new Move(bestPath.get(0));
		
		 
	}
	
	private void updateBeliefs(Location location, int riskObservation, int informationObservation)
	{
		
				
		for(int i = 0; i < locationsCount; i++)
		{
			if(i==location.getID())
			{
				riskBeliefs.put(i,markovRiskModel[riskObservation]);
				informationBeliefs.put(i,markovInformationModel[0]);
				
			}
			else
			{
				double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel);
				riskBeliefs.put(i,temp1);
				double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel);
				informationBeliefs.put(i,temp2);
			}						
		}		
	}
	
	private BeliefState updateBeliefs(Location location, int riskObservation, BeliefState beliefState)
	{
		Map<Integer, double[]> riskStateBielfs = new HashMap<Integer, double[]>();
		Map<Integer, double[]> inforamtionStateBielfs = new HashMap<Integer, double[]>();;

		riskStateBielfs.putAll(beliefState.getRiskStateBielfSet());
		inforamtionStateBielfs.putAll(beliefState.getInforamtionStateBielsfSet());
				
		for(int i = 0; i < locationsCount; i++)
		{
			if(i==location.getID())
			{
				riskStateBielfs.put(i,markovRiskModel[riskObservation]);
				inforamtionStateBielfs.put(i,markovInformationModel[0]);
				
			}
			else
			{
				double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel);
				riskStateBielfs.put(i,temp1);
				double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel);
				inforamtionStateBielfs.put(i,temp2);
			}						
		}		
		
		double beliefProbability=beliefState.getProbability()*beliefState.getRiskStateBielf(location)[riskObservation];

		
		return new BeliefState(beliefProbability,riskStateBielfs,inforamtionStateBielfs);
	}
	
	
	
	private Map<Integer, Double> getValuesFromBeliefStates(List<Location> locations,
			              Map<Integer, double[]>riskBeliefs, Map<Integer, double[]> informationBeliefs)
	{
		Map<Integer, Double> values =new HashMap<Integer, Double>();
		
		for(Location location:locations)
		{
			int i=location.getID();
			
			double reward=0;
			double cost=0;
			
			double informationbelief[]=informationBeliefs.get(i);
			double riskbelief[]=riskBeliefs.get(i);
			
			for(int j = 0; j < informationbelief.length; j++)
			{

				
				reward+=informationbelief[j]*j;
			}
			
			for(int j = 0; j < riskbelief.length; j++)
			{
				cost+=riskbelief[j]*j;
			}
			
			
			double product=1;
			if(location.getID()%3==0) product=1.5;
			if(location.getID()%5==0) product=2.0;
			
			double totalreward=product*0.25*reward-0.5*cost;
			values.put(i, totalreward);
			
		}
		
		return values;
		
	}
	
	private Double getValuesFromBeliefStates_oneLocation(Location location,
            Map<Integer, double[]>riskBeliefs, Map<Integer, double[]> informationBeliefs)

	{

		double reward=0;
		double cost=0;

		double informationbelief[]=informationBeliefs.get(location.getID());

		double riskbelief[]=riskBeliefs.get(location.getID());

		for(int j = 0; j < informationbelief.length; j++)

		{
	
			reward+=informationbelief[j]*j;

		}

		for(int j = 0; j < riskbelief.length; j++)

		{	
			cost+=riskbelief[j]*j;
		}
		
		double product=1;
		if(location.getID()%3==0) product=1.5;
		if(location.getID()%5==0) product=2.0;

		double totalreward=product*0.25*reward-0.5*cost;

		return totalreward;


	}
	
	


    public static double[][] multiplicar(double[][] A, double[][] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }
    public static double[] multiplicar(double[] A, double[][] B) {

        int aColumns = A.length;
//        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[] C = new double[bColumns];
//        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                C[j] = 0.00000;
            }
//        }

//        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[j] += A[k] * B[k][j];
                }
            }
//        }

        return C;
    }


}
