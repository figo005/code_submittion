package uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;





import maxSumController.DiscreteVariableState;
import maxSumController.MaxSumSettings;
import maxSumController.Variable;
import maxSumController.discrete.DiscreteInternalVariable;
import maxSumController.discrete.DiscreteVariableDomain;
import maxSumController.discrete.OptimisedDiscreteMarginalMaximisation;
import maxSumController.discrete.VariableJointState;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorID;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.BeliefState;
import uk.ac.soton.ecs.mobilesensors.sensor.maxsum.MaxSumInternalMovementVariable;
import uk.ac.soton.ecs.mobilesensors.sensor.maxsum.MobileSensorMaxSumFunction;
import uk.ac.soton.ecs.mobilesensors.sensor.maxsum.centralised.AbstractPathsCentralisedController;
import uk.ac.soton.ecs.mobilesensors.sensor.maxsum.domain.MultiStepMove;
import uk.ac.soton.ecs.mobilesensors.sensor.maxsum.factory.MaxSumNodeFactory;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import boundedMaxSum.BoundedDiscreteMaxSumController;
import boundedMaxSum.BoundedInternalFunction;
import boundedMaxSum.LinkBoundedInternalFunction;

public class POMDPCentralisedController extends
		AbstractPathsCentralisedController {

//	private static final String LOG_FILE_NAME = "bounded_max_sum_metrics.txt";
//
//	private BoundedDiscreteMaxSumController controller;
//
//	private MaxSumSettings settings = null;
//
//	private int negotiationInterval;
//
//	private MaxSumNodeFactory<MultiStepMove> maxSumNodeFactory;
//
//	/**
//	 * Whether or not to construct the factor graph such that only the variables
//	 * and functions of the sensors that are within communication range are
//	 * connected.
//	 */
//	private boolean connectToSensorsInRangeOnly = false;
//
//	private BufferedWriter logWriter;
//
//	private int maxSumRuns;
//
//	private boolean debug = false;
//
//	private boolean loggingEnabled = false;
//
//	private boolean useBoundedMaxSum = false;
//
//	private double observationValue;
	
	
	
	private int locationsCount;
	private final double gama=0.95;
	private Map<Integer, double[]> riskBeliefs = new HashMap<Integer, double[]>();
	
	private Map<Integer, double[]> informationBeliefs = new HashMap<Integer, double[]>();
	
	private ArrayList<double[][]> markovInformationModel = new ArrayList<double[][]>();
	private ArrayList<double[][]> markovRiskModel = new ArrayList<double[][]>();
	
	private BeliefFrame beliefFrame;
	
	private AccessibilityGraphImpl graph;


	
//	@Required
//	public void setNegotiationInterval(int negotiationInterval) {
//		this.negotiationInterval = negotiationInterval;
//	}
//
//	@Required
//	public void setMaxSumNodeFactory(
//			MaxSumNodeFactory<MultiStepMove> maxSumNodeFactory) {
//		this.maxSumNodeFactory = maxSumNodeFactory;
//	}
	
	@Override
	protected Map<Sensor, List<Move>> getPaths() {



		Map<Sensor, List<Move>> moves = myopicPolicyWithoutObservation(8,8);
//		Map<Sensor, List<Move>> moves= jumpGreedy();
		
//		Map<Sensor, List<Move>> moves=new HashMap<Sensor, List<Move>>();
//		List<Move> visited=new ArrayList<Move>();
//		for (Sensor sensor : getSensors())
//		{
//		Move bestMove=myopicPolicy(sensor.getLocation(),visited);
//		List<Move> temp=new ArrayList<Move>();
//		temp.add(bestMove);
//		moves.put(sensor, temp);
//		visited.add(bestMove);
//		}
		

		return moves;
	}
	
	public void updateFrame()
	{
		updateBeliefs();
		
		beliefFrame.updateMessage(riskBeliefs,informationBeliefs,
				getValuesFromBeliefStates(graph.getLocations(),riskBeliefs,informationBeliefs),
				getSensorsLocationIDs());
		
	}
	
	public void initialController() {
		
		resetController();
//		this.graph = getSensors().get(0).getEnvironment().getAccessibilityGraph();
//		
//		this.riskBeliefs=graph.getRiskBeliefsIni();
//		this.informationBeliefs=graph.getInformationBeliefsIni();		
//		this.markovRiskModel=graph.getMarkovRiskModel();	
//		this.markovInformationModel=graph.getMarkovInformationModel();
//		this.locationsCount=graph.getLocationCount();
//		
//		
//		updateBeliefs();
//		
//		this.beliefFrame=new BeliefFrame(riskBeliefs,informationBeliefs,
//				getValuesFromBeliefStates(graph.getLocations(),riskBeliefs,informationBeliefs),
//				getSensorsLocationIDs());

	}
	
	public void resetController()
	{
		this.graph = getSensors().get(0).getEnvironment().getAccessibilityGraph();
		
		this.riskBeliefs=graph.getRiskBeliefsIni();
		this.informationBeliefs=graph.getInformationBeliefsIni();		
		this.markovRiskModel=graph.getMarkovRiskModel();	
		this.markovInformationModel=graph.getMarkovInformationModel();
		this.locationsCount=graph.getLocationCount();
		
		
		updateBeliefs();
		
		this.beliefFrame=new BeliefFrame(riskBeliefs,informationBeliefs,
				getValuesFromBeliefStates(graph.getLocations(),riskBeliefs,informationBeliefs),
				getSensorsLocationIDs());
		
	}

	
	protected ObservationInformativenessFunction getInformativenessFunction() {
		ObservationInformativenessFunction informativenessFunction = getSensors().get(0).getEnvironment().getInformativenessFunction();
		return informativenessFunction;
	}
	
//	private Move jumpGreedy()
//	{
//		
//		Map<Integer, Double> values=getValuesFromBeliefStates(graph.getLocations(),
//				 riskBeliefs,informationBeliefs);
//		int bestLoationID=values.
//		
//		return new Move(graph.getLocationFromID(bestLoationID));
//		
//	}
	
	private Move myopicPolicy(Location location, List<Move> visited)
	{

		List<Location> temp=new ArrayList<Location>();
		for(Location neighbour:location.getNeighbours())
		{
			if(!visited.contains(neighbour))
			{
				temp.add(neighbour);
			}
		}
		
		
		
		Map<Integer, Double> valuesofNeighbours=getValuesFromBeliefStates(temp,
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
		
		return new Move(graph.getLocationFromID(bestLoationID));
	}
	
	private Map<Sensor, List<Move>> myopicPolicyWithoutObservation(int backTrackingStepsNumber, int careNum)
	{
		Map<Sensor, ArrayList<ArrayList<Location>>> feasiblePathes = new HashMap<Sensor, ArrayList<ArrayList<Location>>>();
		
		ArrayList<Map<Sensor, ArrayList<Location>>> currentCombinePaths=new ArrayList<Map<Sensor, ArrayList<Location>>>(); //1.
		
		ArrayList<BeliefState> futureBelifs =new ArrayList<BeliefState>(); //belifes of each step of future D steps 
		
		
		BeliefState nextstepBelief=new BeliefState(riskBeliefs,informationBeliefs);
		
		
		Map<Sensor, List<Move>> combineBestMove=new HashMap<Sensor, List<Move>>();
		
		
		Map<Location, List<Integer>> visitedbyFormerAgents =new HashMap<Location, List<Integer>>();
		
		//initial a raw vector(1,0,0,...,0)
		double[] rawInformationBelief= new double[markovInformationModel.get(0).length];
		  
		for (int j = 0; j < rawInformationBelief.length; j++) {
		  rawInformationBelief[j]=0;
		}
		rawInformationBelief[0]=1;
		//  
		  
		
		for(int i = 0; i < backTrackingStepsNumber ; i++)
		{
			
			if(i!=0)
			{
				nextstepBelief= updateBeliefsWithoutObservations(nextstepBelief);
			}
						
			futureBelifs.add(nextstepBelief);
			
		}
		
		
		
		for (Sensor sensor : getSensors())
		{
			//1. find all the feasible paths for this agent
			ArrayList<ArrayList<Location>> cur_pathes=new ArrayList<ArrayList<Location>>();
			
			for(Location neighbor:sensor.getLocation().getNeighbours())
			{
				ArrayList<Location> temp=new ArrayList<Location>();
				temp.add(neighbor);
				cur_pathes.add(temp);
			}
		
			for (int i = 0; i < backTrackingStepsNumber-1 ; i++)
			{
				ArrayList<ArrayList<Location>> found_pathes=new ArrayList<ArrayList<Location>>();
				
				for (ArrayList<Location> path : cur_pathes) {
					Location lastLocation=path.get(path.size()-1);
					
					List<Location> neighbours=lastLocation.getNeighbours();
					for (Location nextlocation : neighbours) {
						if(!path.contains(nextlocation))
						{
							ArrayList<Location> newpath=new ArrayList<Location>();
							
							newpath.addAll(path);
							newpath.add(nextlocation);
							found_pathes.add(newpath);
						}
					}
					
				}
				cur_pathes.clear();
				cur_pathes.addAll(found_pathes);

			}
			
			//2. find best path and action for this agent
			double maxValue = Double.NEGATIVE_INFINITY;
			ArrayList<Location> bestPath=new ArrayList<Location>();
			
			for (ArrayList<Location> path : cur_pathes) {
				
				ArrayList<BeliefState> currentLeafsBelief =new ArrayList<BeliefState>();
							
				currentLeafsBelief.add( new BeliefState(1.0,riskBeliefs,informationBeliefs));
				
				double pathExpReward=0;
				
				double discountfactor=1.0;
				
				
				;
				
				for(int i = 0; i < backTrackingStepsNumber ; i++)
					
				{
					Location node=path.get(i);					  
					int nodeID=node.getID();  
					
					double penalty=0;
					

					  if(visitedbyFormerAgents.containsKey(node))
					  {
						  int t_j=-1; int t_m=500; int t_n=-1;
						  
						  for(Integer t_k:visitedbyFormerAgents.get(node))
						  {
							  if ((t_k<=i)&&(t_j<t_k))
							  {
								  t_j=t_k;
							  }
							  
							  if ((t_k>i)&&(t_m>t_k))
							  {
								  t_m=t_k;
							  }
							  
						  }
						  
						  if(t_j!=-1)
						  {
							  double[] curVector=rawInformationBelief;
							  futureBelifs.get(t_j).setInforamtionStateBielf(nodeID, rawInformationBelief);
							  for(int step = t_j+1; step < i+1 ; step++)
							  {
								  double[] tempVector;

								  tempVector=multiplicar(curVector,markovInformationModel.get(nodeID));
								  futureBelifs.get(step).setInforamtionStateBielf(nodeID, tempVector);

								  curVector=tempVector;
								  
							  }
							  
						  }
						  
						  
						  if((t_m<500))
						  {
							  List<Integer> indexes=new  ArrayList<Integer>();
							  
							  indexes.addAll(visitedbyFormerAgents.get(node)) ;
							  
							  int indexe_t_m=indexes.indexOf(t_m);
							  
							  for(Integer index=0;index<indexe_t_m;index++)
							  {
								  Integer index_t=indexes.get(index);
								  
								  if ((index_t<t_m)&&(index_t>t_n))
								  {
									  t_n=index_t;
								  }
							  }
							  
							  double penaltytemp=getPenalty(node, t_m,i,t_n);
							  if(penaltytemp>0)
							  penalty=discountfactor*penaltytemp;
							  else
							  {
								  int temp;
								  temp=0;
							  }
							  
						  }
						  

					  }
					  
	
					
					
					  double pathExpRewardtemp=discountfactor*getValuesFromBeliefStates_oneLocation(node,
							futureBelifs.get(path.indexOf(node)).getRiskStateBielfSet(),
							futureBelifs.get(path.indexOf(node)).getInforamtionStateBielsfSet());	
					
					if(pathExpRewardtemp<0)
					{
						int temp;
						  temp=0;
					}
					  
					  
					pathExpReward+=pathExpRewardtemp;
				
					discountfactor*=gama;
				
				}
				
				
				
				if (pathExpReward > maxValue) {
					maxValue = pathExpReward;
					bestPath.clear();
					bestPath.addAll(path) ;
				}
			}
			
			//3. update the belief impacted by the bestpath of this agent. 
			
//			updateFutureBelif(futureBelifs);
			for(int i = 0; i < careNum ; i++)
				
			{
				  				  
				  Location node=bestPath.get(i);
				  int nodeID=node.getID();
				  
				  
				  
				  
				  if(visitedbyFormerAgents.containsValue(node))
				  {
					  ArrayList<Integer> temp =new ArrayList<Integer>();
					  temp.addAll(visitedbyFormerAgents.get(node));
					  if(!temp.contains(i))
						  temp.add(i);
					  visitedbyFormerAgents.put(node, temp);
					  
//					  visitedbyFormerAgents.
					  
				  }
				  else
				  {
					  ArrayList<Integer> temp =new ArrayList<Integer>();
//					  temp.addAll(visitedbyFormerAgents.get(node));
//					  if(!temp.contains(i))
						  temp.add(i);
					  visitedbyFormerAgents.put(node, temp);
					  
				  }
//				  else
//				  {
////					  futureBelifs.get(i).setInforamtionStateBielf(nodeID, rawInformationBelief);
////					  for(int step = i+1; step < backTrackingStepsNumber ; step++)
////					  {
////						  double[] tempVector;
////
////						  tempVector=multiplicar(curVector,markovInformationModel);
////						  futureBelifs.get(step).setInforamtionStateBielf(nodeID, rawInformationBelief);
////
////						  curVector=tempVector;
////						  
////					  }
//					  
//					  visitedbyFormerAgents.get(node).add(i);
//				  }
				  		
			}
			
			Move bestMove=new Move(bestPath.get(0));
			List<Move> temp=new ArrayList<Move>();
			temp.add(bestMove);
			
			combineBestMove.put(sensor, temp);
			
						
		}
		
		
		
		
		return combineBestMove;
		
		
		 
	}
	


	private void updateBeliefs()
	{
		Map<Integer, Integer> riskObservations = new HashMap<Integer, Integer>();
		
		for (Sensor sensor : getSensors()) {
			riskObservations.put(sensor.getLocation().getID(), sensor.getRiskObservationForCurrentPosition());
		}
		
				
		for(int i = 0; i < locationsCount; i++)
		{
			if(riskObservations.containsKey(i))
			{
				riskBeliefs.put(i,markovRiskModel.get(i)[riskObservations.get(i)]);
				informationBeliefs.put(i,markovInformationModel.get(i)[0]);
				
			}
			else
			{
				double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel.get(i));
				riskBeliefs.put(i,temp1);
				double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel.get(i));
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
				riskStateBielfs.put(i,markovRiskModel.get(i)[riskObservation]);
				inforamtionStateBielfs.put(i,markovInformationModel.get(i)[0]);
				
			}
			else
			{
				double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel.get(i));
				riskStateBielfs.put(i,temp1);
				double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel.get(i));
				inforamtionStateBielfs.put(i,temp2);
			}						
		}		
		
		double beliefProbability=beliefState.getProbability()*beliefState.getRiskStateBielf(location)[riskObservation];

		
		return new BeliefState(beliefProbability,riskStateBielfs,inforamtionStateBielfs);
	}
	
	private BeliefState updateBeliefsWithoutObservations(BeliefState beliefState)
	{
		Map<Integer, double[]> riskStateBielfs = new HashMap<Integer, double[]>();
		Map<Integer, double[]> inforamtionStateBielfs = new HashMap<Integer, double[]>();;

		riskStateBielfs.putAll(beliefState.getRiskStateBielfSet());
		inforamtionStateBielfs.putAll(beliefState.getInforamtionStateBielsfSet());
				
		for(int i = 0; i < locationsCount; i++)
		{

			double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel.get(i));
			riskStateBielfs.put(i,temp1);
			double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel.get(i));
			inforamtionStateBielfs.put(i,temp2);
								
		}		
		
//		double beliefProbability=beliefState.getProbability()*beliefState.getRiskStateBielf(location)[riskObservation];

		
		return new BeliefState(riskStateBielfs,inforamtionStateBielfs);
	}
	
	
	
//	private BeliefState updateBeliefs(ArrayList<Location> combineAction, ArrayList<Integer> CombineRiskObservation, BeliefState beliefState)
//	{
//		Map<Integer, double[]> riskStateBielfs = new HashMap<Integer, double[]>();
//		Map<Integer, double[]> inforamtionStateBielfs = new HashMap<Integer, double[]>();;
//
//		riskStateBielfs.putAll(beliefState.getRiskStateBielfSet());
//		inforamtionStateBielfs.putAll(beliefState.getInforamtionStateBielsfSet());
//				
//		for(int i = 0; i < locationsCount; i++)
//		{
//			if(i==location.getID())
//			{
//				riskStateBielfs.put(i,markovRiskModel[riskObservation]);
//				inforamtionStateBielfs.put(i,markovInformationModel[0]);
//				
//			}
//			else
//			{
//				double temp1[]=multiplicar(riskBeliefs.get(i),markovRiskModel);
//				riskStateBielfs.put(i,temp1);
//				double temp2[]=multiplicar(informationBeliefs.get(i),markovInformationModel);
//				inforamtionStateBielfs.put(i,temp2);
//			}						
//		}		
//		
//		double beliefProbability=beliefState.getProbability()*beliefState.getRiskStateBielf(location)[riskObservation];
//
//		
//		return new BeliefState(beliefProbability,riskStateBielfs,inforamtionStateBielfs);
//	}

	
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

//			if(location.getID()%3==0) product=1.1;
//			if(location.getID()%7==0) product=0.9;
			
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
//		if(location.getID()%3==0) product=1.1;
//		if(location.getID()%7==0) product=0.9;
		
		double totalreward=product*0.25*reward-0.5*cost;

		return totalreward;


	}
	private double getPenalty(Location location, int t_m, int t_current, int t_n)
	{
		double r_expected=0;
		double r_revised=0;
		int nodeID=location.getID();
		
		double temp1[];
		if(t_n==-1)  //t_m is not visited by a former agent
		{
			 temp1=informationBeliefs.get(location.getID());
		     
			 for(int times1= 0; times1<(t_m-1); times1++)
			 {
				 temp1=multiplicar(temp1,markovInformationModel.get(nodeID));
			 }
		}
		else
		{		
			 temp1=multiplicar(markovInformationModel.get(location.getID())[0],markovInformationModel.get(nodeID));
		     
			 for(int times1= 0; times1<(t_m-t_n-1); times1++)
			 {
				 temp1=multiplicar(temp1,markovInformationModel.get(nodeID));
			 }
		
		}
		
		 double temp2[]=multiplicar(markovInformationModel.get(nodeID)[0],markovInformationModel.get(nodeID));
	     
		 for(int times2= 0; times2<(t_current-t_m-1); times2++)
		 {
			 temp2=multiplicar(temp2,markovInformationModel.get(nodeID));
		 }
		
		 double penalty=0;
		 for(int j = 0; j < markovInformationModel.get(nodeID).length; j++)

		 {
		
			 penalty+=(temp1[j]-temp2[j])*j;

		 }
		

			double product=1;
//			if(location.getID()%3==0) product=1.1;
//			if(location.getID()%7==0) product=0.9; 
			
		 penalty=product*0.25*penalty;
	
		return penalty;
		
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


	public void handleEndOfRound(int round, double timestep) {

	}

//	public void setOutputDirectory(File outputDirectory) {
//		super.setOutputDirectory(outputDirectory);
//
//		try {
//			logWriter = new BufferedWriter(new FileWriter(new File(
//					outputDirectory, LOG_FILE_NAME)));
//
//			logWriter.write("% MaxSumRun TreeValue FactorGraphValue "
//					+ "OptimalValue UpperBound MessageSize ApproxRatio\n");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void finaliseLogs() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getRecomputeInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

}
