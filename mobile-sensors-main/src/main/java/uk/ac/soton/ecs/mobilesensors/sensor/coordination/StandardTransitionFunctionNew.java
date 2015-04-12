package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.utils.ArrayUtils;

public class StandardTransitionFunctionNew extends
		DeterministicTransitionFunctionNew<StateNew,Move> {

	private final int locationsCount;
	private final int tau;
//	private final int clusterBudget;
//	private final Collection<Cluster<Location>> clusters;
	private final int eachMoveDuration;
//	private final int healthyDecrement;
//	private final Collection<Location> riskLocations;
	Map<String, Double> hurtProbabilityMap;
	private final double P_HaveHurt_HaveRisk;
	private final double P_NoHurt_HaveRisk;
	private final double P_HaveHurt_NoRisk; 
	private final double P_NoHurt_NoRisk;

	public StandardTransitionFunctionNew(int locationsCount,
			int tau, int eachMoveDuration, Map<String, Double> hurtProbabilityMap) {
		super();
		this.locationsCount = locationsCount;
		this.tau = tau;
		this.eachMoveDuration = eachMoveDuration;
		this.P_HaveHurt_HaveRisk=hurtProbabilityMap.get("P_HaveHurt_HaveRisk");
		this.P_NoHurt_HaveRisk=hurtProbabilityMap.get("P_NoHurt_HaveRisk");
		this.P_HaveHurt_NoRisk=hurtProbabilityMap.get("P_HaveHurt_NoRisk");
		this.P_NoHurt_NoRisk=hurtProbabilityMap.get("P_NoHurt_NoRisk");
	}

	public List<Move> getActions(StateNew state) {
		Location sensorLocation = state.getLocation();


		List<Move> actions=sensorLocation.getMoveOptions();

		return actions;
	}

	@Override
	public StateNew deterministicTransition(StateNew state,
			Move action) {
		int[] newVisitTimes = new int[locationsCount];
		int[] lastVisitTimes = state.getLastVisitTimes();
		HealthyBelief healthyBelief=state.getHealthyLevelBelief();
		HealthyBelief newHealthy;
		
//		if(riskLocations.contains(action.getTargetLocation()))
//		{
//			newHealthy=Math.max(healthy-healthyDecrement,0);
//			
//		}
//		else newHealthy=healthy;

		newHealthy=transitionHealthyBelief(healthyBelief,action.getTargetLocation().getRiskProbability());

		System.arraycopy(lastVisitTimes, 0, newVisitTimes, 0, locationsCount);



		for (int i = 0; i < newVisitTimes.length; i++) {
//			newVisitTimes[i] = Math.min(tau, lastVisitTimes[i]
//						+ eachMoveDuration);
			newVisitTimes[i] = lastVisitTimes[i]
					+ eachMoveDuration;
			
		}


		newVisitTimes[state.getLocation().getID()] = 1;

		StateNew state2 = new StateNew(action.getTargetLocation(), newVisitTimes,newHealthy);
		// HashMap<MultiSensorState, Double> result = new
		// HashMap<MultiSensorState, Double>();
		// result.put(state2, 1.0);

		return state2;
	}


	public HealthyBelief transitionHealthyBelief(HealthyBelief healthyBelief, double riskProbability) {
		Map<Integer, Double> beliefMap=healthyBelief.getBeliefMap();
		Map<Integer, Double> newBeliefMap=new TreeMap<Integer, Double>().descendingMap();
		
		
		double P_Hurt=riskProbability*P_HaveHurt_HaveRisk+(1-riskProbability)*P_HaveHurt_NoRisk;
		double P_NoHurt=1-P_Hurt;
		
		double basicPart;
		double transitionPart=0;		
		for(int belifLevel : beliefMap.keySet())
		{
			if(belifLevel!=0)			
				basicPart=beliefMap.get(belifLevel)*P_NoHurt;
			else
				basicPart=beliefMap.get(belifLevel);
			
			newBeliefMap.put(belifLevel, basicPart+transitionPart);
			
			if(belifLevel!=0)			
				transitionPart=beliefMap.get(belifLevel)*P_Hurt;
						
		}
		
		return new HealthyBelief(newBeliefMap);
	}
	
	public int getLocationsCount() {
		return locationsCount;
	}



	public int getTau() {
		return tau;
	}

}
