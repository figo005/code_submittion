package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;

public class ExpandedState implements Comparable<ExpandedState>{


	private final int expandedLevel;
	private final int locationIndex;
//	private final int parentIndex;
	private final double expandedProbability;
	private final int riskObservation;
	private final double expReward;


	public ExpandedState(int expandedLevel, int locationIndex,int parentIndex,
			double expandedProbability,int riskObservation,double expReward) {
		this.expandedLevel = expandedLevel;
		this.locationIndex = locationIndex;
//		this.parentIndex = parentIndex;
		this.expandedProbability = expandedProbability;
		this.riskObservation = riskObservation;
		this.expReward = expReward;
	}


	public int getExpandedLevel(int expandedLevel) {
		return expandedLevel;
	}
	
	public int getLocationIndex(int locationIndex) {
		return locationIndex;
	}
	public int getParentIndex(int parentIndex) {
		return parentIndex;
	}
	public double getExpandedProbability(double expandedProbability) {
		return expandedProbability;
	}
	public int getRiskObservation(int riskObservation) {
		return riskObservation;
	}
	
	public double getExpReward(double expReward) {
		return expReward;
	}
	
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(expandedLevel);
		builder.append(locationIndex);

		builder.append(parentIndex);
		builder.append(expandedProbability);
		builder.append(riskObservation);


		return builder.toString();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(expandedLevel).append(locationIndex).append(parentIndex)
				.append(expandedProbability).append(riskObservation).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExpandedState) {
			ExpandedState state = (ExpandedState) obj;

			return (state.expandedLevel==expandedLevel)
					&&(state.locationIndex==locationIndex)
					&&(state.parentIndex==parentIndex)
					&&(state.riskObservation==riskObservation);
		}

		return false;
	}


	public int compareTo(ExpandedState s) {
		ExpandedState state = (ExpandedState) s;

		
		int compareResult=0;
		

		if (state.expandedLevel!=expandedLevel)
			{compareResult=1;
			return compareResult;
			}
		
		if (state.locationIndex!=locationIndex)
		{compareResult=1;
		return compareResult;
		}
		if (state.parentIndex!=parentIndex)
		{compareResult=1;
		return compareResult;
		}
		if (state.riskObservation!=riskObservation)
		{compareResult=1;
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
