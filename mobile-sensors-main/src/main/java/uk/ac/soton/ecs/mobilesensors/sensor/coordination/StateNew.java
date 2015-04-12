package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;

public class StateNew implements Comparable<StateNew>{

	private final int[] lastVisitTimes;

	protected Location location;
	
	private final HealthyBelief healthyLevelBelief;

//	public StateNew(Location location, int locationsCount) {
//		this(location, new int[locationsCount],10);
//	}

	public StateNew(Location location,
			int[] lastVisitTimes,HealthyBelief healthyLevelBelief) {
		this.location = location;
		this.lastVisitTimes = lastVisitTimes;
		this.healthyLevelBelief=healthyLevelBelief;
	}


	public int getLocationsCount() {
		return lastVisitTimes.length;
	}

	public Location getLocation() {
		return location;
	}

	public int[] getLastVisitTimes() {
		return lastVisitTimes;
	}

	public int getLastVisitTime(Location location) {
		return lastVisitTimes[location.getID()];
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < lastVisitTimes.length; i++) {
			builder.append(lastVisitTimes[i] + " ");
		}
		builder.append(location.toString());

		return builder.toString();
	}

	@Override
	public int hashCode() {
//		return new HashCodeBuilder().append(lastVisitTimes)
//				.append(location).append(healthyLevelBelief).toHashCode();
		
		//do not use healthyLevelBelief to distinguish different states,
		//because our state is not standard determined state, 
		//we add thehealthyLevel probability here.
		return new HashCodeBuilder().append(lastVisitTimes)
				.append(location).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StateNew) {
			StateNew state = (StateNew) obj;

			return Arrays.equals(state.lastVisitTimes, lastVisitTimes)
					//do not use healthyLevelBelief to distinguish different states,
					//because our state is not standard determined state, 
					//we add thehealthyLevel probability here.
//					&& (state.healthyLevelBelief==healthyLevelBelief) 
					&& state.location.equals(location);
		}

		return false;
	}

	public HealthyBelief  getHealthyLevelBelief() {
		return healthyLevelBelief;
	}

//	public int getSensorCount() {
//		return sensorStates.length;
//	}
//
//	public StateNew getSubstateSingleSensor(int i) {
//		return new StateNew(
//				new SensorPositionState[] { sensorStates[i] }, Arrays.copyOf(
//						lastVisitTimes, lastVisitTimes.length));
//	}

	public int compareTo(StateNew s) {
		StateNew state = (StateNew) s;

		for (int i = 0; i < getLocationsCount(); i++) {
			if (lastVisitTimes[i] != state.lastVisitTimes[i]) {
				return lastVisitTimes[i] - state.lastVisitTimes[i];
			}
		}
		
		int compareResult =location.getID()-state.getLocation().getID();

		if (compareResult != 0)
			return compareResult;


			int compareHealthyResult =healthyLevelBelief.compareTo(state.healthyLevelBelief);

			if (compareHealthyResult != 0)
				return compareHealthyResult;


		if (!equals(s)) {
			System.out.println("JKLDFJ");
			System.out.println(this);
			System.out.println(s);
			System.out.println(s.getHealthyLevelBelief());
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
