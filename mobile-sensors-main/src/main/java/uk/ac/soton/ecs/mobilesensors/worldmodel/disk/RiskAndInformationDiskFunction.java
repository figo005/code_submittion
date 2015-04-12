package uk.ac.soton.ecs.mobilesensors.worldmodel.disk;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javolution.util.FastMap;

import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.BeliefState;
import uk.ac.soton.ecs.mobilesensors.worldmodel.Observation;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationCoordinates;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import uk.ac.soton.ecs.mobilesensors.worldmodel.disk.UncertaintyMap;
import uk.ac.soton.ecs.mobilesensors.worldmodel.probpe.ImplValueMap;
import uk.ac.soton.ecs.mobilesensors.worldmodel.probpe.ProbabilityMap;
import uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader.AbstractObservationInformativenessFunction;
import uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader.AbstractValueMap;

/**
 * based on DiskInformativenessFunction,Add risk in it 
 * 
 * 
 * @author sc16g13
 * 
 */
public class RiskAndInformationDiskFunction extends
		AbstractObservationInformativenessFunction {

	private double radius;

	private ImplValueMap valueMap;

	public double uncertaintyIncrement;


	private int currentTime;
	
	
	private Map<Integer, Integer> riskStates = new HashMap<Integer, Integer>();
	
	private Map<Integer, Integer> informationStates = new HashMap<Integer, Integer>();
	
	
//	private  double markovRiskModel[][];
	
//	private  double markovInformationModel[][];
	private ArrayList<double[][]> markovInformationModel = new ArrayList<double[][]>();
	private ArrayList<double[][]> markovRiskModel = new ArrayList<double[][]>();
	
	public double totalReward=0;
	
	
	private  ArrayList<Location> visited =new ArrayList<Location>();
	
	
	
	
	
	
	
	public RiskAndInformationDiskFunction(Grid grid, AccessibilityGraphImpl graph, double radius,
			double uncertaintyIncrement) {
		this.radius = radius;
		this.uncertaintyIncrement = uncertaintyIncrement;
		setGrid(grid);
		setGraph(graph);
		initialise();

	}

	public RiskAndInformationDiskFunction() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param grid
	 * @param radius2
	 * @param uncertaintyIncrement2
	 * @param uncertaintyMap2
	 */


	@Required
	public double getTotalRewardGathered() {		
		
		return totalReward;
	}
	
	public void setUncertaintyIncrement(double uncertaintyIncrement) {
		this.uncertaintyIncrement = uncertaintyIncrement;
	}
	
	public double getUncertaintyIncrement() {
		return uncertaintyIncrement;
	}
		
	public Map<Integer, Integer> getRiskStates() {		
		
		return riskStates;
	}
	
	public Map<Integer, Integer> getInformationStates() {		
		
		return informationStates;
	}
	
	public Map<Point2D, Double> getValues() {		
		
		return valueMap.getValues();
	}

	@Required
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getInformativeness(Location location) {
		Collection<Point2D> gridPointsInRange = getGrid().getGridPoints(
				location, radius);
		
//		updateRiskBilief(location);
		
//		return uncertaintyMap.getUncertaintyReduction(gridPointsInRange);
		return valueMap.getValue(location.getCoordinates());
	}
	
	//hurtByRisk(modified by Shaofei)
	public boolean updateRiskBilief(Location location) {	

		
		return true;
		
	}
	
	
	/**
	 * @param location
	 * @return
	 */


	public ObservationInformativenessFunction copy() {
		return new RiskAndInformationDiskFunction(getGrid(), getGraph(), radius,
				uncertaintyIncrement);
	}

	public void clearHistory() {
		initialise();
	}

	public Collection<Observation> observe(Location location) {
		
		visited.add(location);
	

		return new ArrayList<Observation>();
	}

	public void initialise() {

		
		riskStates=getGraph().getRiskStatesIni();
		informationStates=getGraph().getInformationStatesIni();		
		markovRiskModel=getGraph().getMarkovRiskModel();	
		markovInformationModel=getGraph().getMarkovInformationModel();	
		
//		uncertaintyMap = new UncertaintyMap(getGrid(), uncertaintyIncrement);
		
		
		valueMap=new ImplValueMap(getGrid(), getValuesFromInformationAndRisk() );


	}
	
	FastMap<Point2D, Double> getValuesFromInformationAndRisk()
	{
		FastMap<Point2D, Double> values =new FastMap<Point2D, Double>();;
		
		for(Location location:getGraph().getLocations())
		{
			double product=1;
//			if(location.getID()%3==0) product=1.1;
//			if(location.getID()%7==0) product=0.9;
				
			
			double reward=product*0.25*informationStates.get(location.getID())-0.5*riskStates.get(location.getID());
			values.put(location.getCoordinates(), reward);
			
		}
		
		return values;
		
	}

	public boolean hasEventOccurred() {
		return false;
	}

	public double getObservationRange() {
		return radius;
	}

	
	
	
	public void progressTime(int time) {
		currentTime += time;
		
		
		
		ArrayList<Location> added =new ArrayList<Location>();
		for(Location location:visited)
		{
			if(!added.contains(location))
			{
				totalReward+=valueMap.getValue(location.getCoordinates());
				informationStates.put(location.getID(), 0);
			}
			else
			{
				totalReward+=-0.5*riskStates.get(location.getID());
			}
			
			added.add(location);
		}
		visited.clear();
		
		
		
//		uncertaintyMap.increaseUncertainty(time);
		Random rd1 = new Random();
		Random rd2 = new Random();




		for(int i = 0; i < informationStates.size(); i++)
		{
			double random1=rd1.nextDouble();
			double temp=0;
			int state =informationStates.get(i); //Get the state of point i;
			
			for(int j = 0; j < markovInformationModel.get(i).length; j++)
			{
				temp+=markovInformationModel.get(i)[state][j]; //Get the state of point i;
				if(random1<=temp)//We assume the risk of the environment will always attack the agent will it arrive the point.
					{
						informationStates.put(i, j);
						break;
					}
				
			}
			
		}
		
		for(int i = 0; i < riskStates.size(); i++)
		{
			double random1=rd1.nextDouble();
			double temp=0;
			int state =riskStates.get(i); //Get the state of point i;
			
			for(int j = 0; j < markovRiskModel.get(i).length; j++)
			{
				temp+=markovRiskModel.get(i)[state][j]; //Get the state of point i;
				if(random1<=temp)//We assume the risk of the environment will always attack the agent will it arrive the point.
					{
					riskStates.put(i, j);
						break;
					}
				
			}
			
		}
		
		
		
		valueMap.setValues(getValuesFromInformationAndRisk());			
	}

	public double getInformativeness(Location location, Set<Location> locations) {
		Collection<Point2D> gridPointsInRange = new ArrayList<Point2D>();

		for (Location gridLocation : locations) {
			if (gridLocation.directDistanceSq(location) < radius * radius) {
				gridPointsInRange.add(gridLocation.getCoordinates());
			}
		}
		

//		return uncertaintyMap.getUncertaintyReduction(gridPointsInRange);
		return valueMap.getValue(location.getCoordinates());
	}

	public int getTau() {
//		return (int) (1.5 / uncertaintyIncrement);
		return (int) (1.0 / uncertaintyIncrement);

	}
	
	public Map<String, Double> getHurtProbability() {
		
		Map<String, Double> hurtProbabilityMap=new HashMap<String, Double>();
//		hurtProbabilityMap.put("P_HaveHurt_HaveRisk", P_HaveHurt_HaveRisk);
//		hurtProbabilityMap.put("P_NoHurt_HaveRisk", P_NoHurt_HaveRisk);
//		hurtProbabilityMap.put("P_HaveHurt_NoRisk", P_HaveHurt_NoRisk);
//		hurtProbabilityMap.put("P_NoHurt_NoRisk", P_NoHurt_NoRisk);

		
		return hurtProbabilityMap;

	}
}
