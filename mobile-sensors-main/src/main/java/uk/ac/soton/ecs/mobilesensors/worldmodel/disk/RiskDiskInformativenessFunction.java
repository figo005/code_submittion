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

import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.worldmodel.Observation;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationCoordinates;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import uk.ac.soton.ecs.mobilesensors.worldmodel.disk.UncertaintyMap;
import uk.ac.soton.ecs.mobilesensors.worldmodel.probpe.ProbabilityMap;
import uk.ac.soton.ecs.mobilesensors.worldmodel.pursuitevader.AbstractObservationInformativenessFunction;

/**
 * based on DiskInformativenessFunction,Add risk in it 
 * 
 * 
 * @author sc16g13
 * 
 */
public class RiskDiskInformativenessFunction extends
		AbstractObservationInformativenessFunction {

	private double radius;

	private UncertaintyMap uncertaintyMap;

	public double uncertaintyIncrement;



	private int currentTime;
	
	
	
	//currentRiskProbabilityMap is the bielf of risk in the map. 
	//As we modeled RiskProbability of the Class of Location, it is same of this currentRiskProbabilityMap,
	//We do not use it for the moment
	private ProbabilityMap currentRiskProbabilityMap;
	
	private  Point2D riskPoint1;
	private  Point2D riskPoint2;
	private  Point2D riskPoint3;
	private  Point2D riskPoint4;
	private  Point2D riskPoint5;
	
	private Collection<Location> riskPointsList;
	
	private double P_HaveHurt_HaveRisk=0.5; //P(hurt=Havehurt|risk=Haverisk)=0.3,  P(hurt=Nohurt|risk=Haverisk)=0.7)
	private double P_NoHurt_HaveRisk=0.5;
	private double P_HaveHurt_NoRisk=0.05; //P(hurt=Havehurt|risk=Norisk)=0.03,  P(hurt=Nohurt|risk=Norisk)=0.97
	private double P_NoHurt_NoRisk=0.95;
	
	
	public RiskDiskInformativenessFunction(Grid grid, double radius,
			double uncertaintyIncrement) {

		this.radius = radius;
		this.uncertaintyIncrement = uncertaintyIncrement;
		setGrid(grid);
		initialise();

	}

	public RiskDiskInformativenessFunction() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param grid
	 * @param radius2
	 * @param uncertaintyIncrement2
	 * @param uncertaintyMap2
	 */
	private RiskDiskInformativenessFunction(Grid grid, double radius2,
			double uncertaintyIncrement2, UncertaintyMap uncertaintyMap2, ProbabilityMap currentRiskProbabilityMap2, Collection<Location> riskPointsList2) {
		setGrid(grid);
		this.radius = radius2;
		this.uncertaintyIncrement = uncertaintyIncrement2;
		this.uncertaintyMap = uncertaintyMap2;
		
		this.currentRiskProbabilityMap=currentRiskProbabilityMap2;
		this.riskPointsList = riskPointsList2;
	}

	@Required
	public void setUncertaintyIncrement(double uncertaintyIncrement) {
		this.uncertaintyIncrement = uncertaintyIncrement;
	}
	
	public double getUncertaintyIncrement() {
		return uncertaintyIncrement;
	}
	public Map<Point2D, Double> getValues() {
		return uncertaintyMap.getValues();
	}

	@Required
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getInformativeness(Location location) {
		Collection<Point2D> gridPointsInRange = getGrid().getGridPoints(
				location, radius);
		
//		updateRiskBilief(location);
		
		return uncertaintyMap.getUncertaintyReduction(gridPointsInRange);
	}
	
	//hurtByRisk(modified by Shaofei)
	public boolean updateRiskBilief(Location location) {	
		//Add the initialise of the coordinates of the risk nodes


		//currentRiskProbabilityMap = getRiskProbabilityMapAfterObservations(location);
//		double tempRiskProbability=currentRiskProbabilityMap.getValue(location.getCoordinates());
		double tempRiskProbability=location.getRiskProbability();
		boolean beenHurt =false;
		Random rd1 = new Random();
		Random rd2 = new Random();

		double random1=rd1.nextDouble();
		if(riskPointsList.contains(location)&&(random1<=P_HaveHurt_HaveRisk))//We assume the risk of the environment will always attack the agent will it arrive the point.
			beenHurt=true;
		
		
		double random2=rd2.nextDouble();
		if((!riskPointsList.contains(location))&&(random2<=P_HaveHurt_NoRisk))
			beenHurt=true;
		
		
		
		
		
		if (beenHurt==true)
		{
			double P_Hurt=tempRiskProbability*P_HaveHurt_HaveRisk+(1-tempRiskProbability)*P_HaveHurt_NoRisk;
			tempRiskProbability=tempRiskProbability*P_HaveHurt_HaveRisk/P_Hurt;
		}
		else
		{				
			double P_NoHurt=tempRiskProbability*P_NoHurt_HaveRisk+(1-tempRiskProbability)*P_NoHurt_NoRisk;
			tempRiskProbability=tempRiskProbability*P_NoHurt_HaveRisk/P_NoHurt;
		}
		
//		currentRiskProbabilityMap.put(location.getCoordinates(), tempRiskProbability);
		location.setRiskProbability(tempRiskProbability);
		
		return beenHurt;
		
	}
	
	
	/**
	 * @param location
	 * @return
	 */


	public ObservationInformativenessFunction copy() {
		return new RiskDiskInformativenessFunction(getGrid(), radius,
				uncertaintyIncrement, uncertaintyMap.copy(), currentRiskProbabilityMap.copy(), riskPointsList);
	}

	public void clearHistory() {
		initialise();
	}

	public Collection<Observation> observe(Location location) {
		Collection<Point2D> gridPointsInRange = getGrid().getGridPoints(
				location, radius);

		uncertaintyMap.observe(gridPointsInRange);

		return new ArrayList<Observation>();
	}

	public void initialise() {
		
		riskPointsList=getGrid().getRiskLocations();
		uncertaintyMap = new UncertaintyMap(getGrid(), riskPointsList, uncertaintyIncrement);
	
		

		currentRiskProbabilityMap = new ProbabilityMap(getGrid());
		currentRiskProbabilityMap.createFlatPrior(5);
	}

	public boolean hasEventOccurred() {
		return false;
	}

	public double getObservationRange() {
		return radius;
	}

	public void progressTime(int time) {
		currentTime += time;
		uncertaintyMap.increaseUncertainty(time);
	}

	public double getInformativeness(Location location, Set<Location> locations) {
		Collection<Point2D> gridPointsInRange = new ArrayList<Point2D>();

		for (Location gridLocation : locations) {
			if (gridLocation.directDistanceSq(location) < radius * radius) {
				gridPointsInRange.add(gridLocation.getCoordinates());
			}
		}

		return uncertaintyMap.getUncertaintyReduction(gridPointsInRange);
	}

	public int getTau() {
//		return (int) (1.5 / uncertaintyIncrement);
		return (int) (1.0 / uncertaintyIncrement);

	}
	
	public Map<String, Double> getHurtProbability() {
		
		Map<String, Double> hurtProbabilityMap=new HashMap<String, Double>();
		hurtProbabilityMap.put("P_HaveHurt_HaveRisk", P_HaveHurt_HaveRisk);
		hurtProbabilityMap.put("P_NoHurt_HaveRisk", P_NoHurt_HaveRisk);
		hurtProbabilityMap.put("P_HaveHurt_NoRisk", P_HaveHurt_NoRisk);
		hurtProbabilityMap.put("P_NoHurt_NoRisk", P_NoHurt_NoRisk);

		
		return hurtProbabilityMap;

	}
}
