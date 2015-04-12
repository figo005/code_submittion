package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;

public class BeliefState implements Comparable<BeliefState>{


	private  double probability;
	private  Map<Integer, double[]> riskStateBielfs = new HashMap<Integer, double[]>();
	private  Map<Integer, double[]> inforamtionStateBielfs = new HashMap<Integer, double[]>();;
	


	public BeliefState(double probability, Map<Integer, double[]> riskStateBielfs, Map<Integer, double[]> inforamtionStateBielfs) {
		this.probability = probability;
		this.riskStateBielfs = riskStateBielfs;
		this.inforamtionStateBielfs = inforamtionStateBielfs;
	}
	
	public BeliefState(Map<Integer, double[]> riskStateBielfs, Map<Integer, double[]> inforamtionStateBielfs) {
		this.probability = 1.0;
		this.riskStateBielfs = riskStateBielfs;
		this.inforamtionStateBielfs = inforamtionStateBielfs;
	}

	public double getProbability() {
		return probability;
	}
	
	public double[] getRiskStateBielf(Location location) {
		return riskStateBielfs.get(location.getID());
	}
	
	public double[] getInforamtionStateBielf(Location location) {
		return inforamtionStateBielfs.get(location.getID());
	}
	public Map<Integer, double[]> getRiskStateBielfSet() {
		return riskStateBielfs;
	}
	
	public Map<Integer, double[]> getInforamtionStateBielsfSet() {
		return inforamtionStateBielfs;
	}
	
	
//	public double[] setRiskStateBielf(Location location) {
//		return riskStateBielfs.get(location.getID());
//	}
	
	public void setInforamtionStateBielf(int locationID, double[] beliefVector) {
		inforamtionStateBielfs.put(locationID, beliefVector);
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(probability);
		builder.append(riskStateBielfs);
		builder.append(inforamtionStateBielfs);
		
		return builder.toString();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(probability).append(riskStateBielfs)
				.append(inforamtionStateBielfs).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeliefState) {
			BeliefState state = (BeliefState) obj;

			return riskStateBielfs.equals(state.riskStateBielfs);
		}

		return false;
	}


	public int compareTo(BeliefState s) {
//		BeliefState state = (BeliefState) s;

		
		int compareResult=0;
		
		if (probability==s.probability)
		{compareResult=1;
		return compareResult;
		}
		
		if (riskStateBielfs.equals(s.riskStateBielfs))
			{compareResult=1;
			return compareResult;
			}
		
			
		if (inforamtionStateBielfs.equals(s.inforamtionStateBielfs))
			{		compareResult=1;
				return compareResult;
			}



		if (!equals(s)) {
			System.out.println("JKLDFJ");
			System.out.println(this);
			System.out.println(s);
//			System.out.println(s.getHealthyLevelBelief());
			System.out.println(hashCode());
			System.out.println(s.hashCode());
		}

		Validate.isTrue(equals(s));

		return 0;

	}

	// remove the state of sensor with given index
//	public void remove(int index) {
//		sensorStates = (SensorPositionState[]) ArrayUtils.remove(sensorStates,
//				index);
//	}
}
