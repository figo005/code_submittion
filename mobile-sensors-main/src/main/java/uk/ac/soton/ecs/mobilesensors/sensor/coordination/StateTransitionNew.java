package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.soton.ecs.mobilesensors.Move;

public class StateTransitionNew<S extends StateNew, A> implements
		Comparable<StateTransitionNew<S,A>> {

	private S successor;
	private A action;
	private S state;
	private Integer hashCode;
	private Double reward;
	private double probability;

	public StateTransitionNew(S state, A action, S successor,
			double probability) {
		this.state = state;
		this.action = action;
		this.successor = successor;
		this.probability = probability;
	}

	public A getAction() {
		return action;
	}

	public S getState() {
		return state;
	}

	public S getSuccessor() {
		return successor;
	}

	public double getProbability() {
		return probability;
	}

	@Override
	public int hashCode() {
		if (hashCode == null)
			hashCode = new HashCodeBuilder().append(state).append(action)
					.append(successor).toHashCode();

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StateTransitionNew<?,?>) {
			StateTransitionNew<?,?> transition = (StateTransitionNew<?,?>) obj;

			return transition.action.equals(action)
					&& transition.state.equals(state)
					&& transition.successor.equals(successor);
		}
		return false;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	public int compareTo(StateTransitionNew<S,A> o) {
		return getSuccessor().compareTo(o.getSuccessor());
	}

	@Override
	public String toString() {
		return "{Action " + action + " | Successor: " + successor
				+ " | Reward: " + reward + "}";
	}
}
