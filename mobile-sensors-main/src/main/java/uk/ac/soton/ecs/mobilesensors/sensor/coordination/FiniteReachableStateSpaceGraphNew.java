package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.MultiSensorAction;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.StateTransition;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.TransitionFunction;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class FiniteReachableStateSpaceGraphNew <S extends StateNew, A extends Move>{
	
	protected DirectedSparseMultigraph<S, StateTransitionNew<S,A>> graph = new DirectedSparseMultigraph<S, StateTransitionNew<S,A>>();
	private final TransitionFunctionNew<S,A> transitionFunction;
	private final int finiteStepsNumber;

	public FiniteReachableStateSpaceGraphNew(S initial, TransitionFunctionNew<S,A> function,int finiteStepsNumber) {
		Validate.notNull(function);
		this.transitionFunction = function;
		this.finiteStepsNumber=finiteStepsNumber;

		extend(initial);
	}
	public Collection<S> getSuccessors(S state) {
		return graph.getSuccessors(state);
	}

	public StateTransitionNew<S,A> getTransition(S state, MultiSensorAction action) {
		Collection<StateTransitionNew<S,A>> stateTransitions = getStateTransitions(state);

		for (StateTransitionNew<S,A> stateTransition : stateTransitions) {
			if (stateTransition.getAction().equals(action)) {
				return stateTransition;
			}
		}

		System.err.println("state " + state + " has no action " + action);
		System.err.println("available actions "
				+ transitionFunction.getActions(state));

		return null;

	}

	public Collection<StateTransitionNew<S,A>> getStateTransitions(S state) {
		return graph.getOutEdges(state);
	}

	public Collection<S> getStates() {
		return graph.getVertices();
	}

	public int getStateCount() {
		return graph.getVertexCount();
	}

	public int getActionCount() {
		return graph.getEdgeCount();
	}

	protected void addTransition(S state, A action,
			S successor, double probability) {
		StateTransitionNew<S,A> transition = new StateTransitionNew<S,A>(state, action,
				successor, probability);

		if (graph.containsEdge(transition))
			return;

		int edgeCount = graph.getEdgeCount();

		graph.addEdge(transition, state, successor);

		Validate.isTrue(graph.getEdgeCount() == edgeCount + 1);
	}

	public TransitionFunctionNew<S,A> getTransitionFunction() {
		return transitionFunction;
	}

	public boolean contains(S state) {
		return graph.containsVertex(state);
	}


	public Set<S> extend(S initial) {
		Collection<S> newStates = new HashSet<S>();
		newStates.add(initial);

		Set<S> addedStates = new TreeSet<S>();
		addedStates.add(initial);

		Collection<S> foundStates = new HashSet<S>();

		int oldStateCount = getStateCount();
		int stepCount = 0;
		while ((!newStates.isEmpty())&&stepCount<finiteStepsNumber) {
			
			stepCount++;
			for (S state : newStates) {
				if(state.getHealthyLevel()!=0)
				{
				List<A> actions = getTransitionFunction()
						.getActions(state);

				for (A action : actions) {
					Map<S, Double> successors = getTransitionFunction()
							.transition(state, action);

					Validate.isTrue(successors.size() == new TreeSet<S>(
							successors.keySet()).size());

					for (S successor : new TreeSet<S>(successors.keySet())) {
						if (!graph.containsVertex(successor)) {
							foundStates.add(successor);

							addedStates.add(successor);
							
							
						}

						addTransition(state, action, successor,
								successors.get(successor));
					}

				}

				Validate.isTrue(getStateTransitions(state).size() == actions
						.size());
				}
			}

			newStates.clear();
			newStates.addAll(foundStates);
			foundStates.clear();
		}

		int increase = getStateCount() - oldStateCount;
//		int error = increase - addedStates.size();
		if (increase != addedStates.size()) {
			System.out.println("=========");
			System.out.println(CollectionUtils.subtract(addedStates,
					getStates()));
			System.out.println(CollectionUtils.subtract(getStates(),
					addedStates));
			System.out.println(getStates());
			System.out.println(addedStates);

			for (S s : (Collection<S>) CollectionUtils.subtract(getStates(),
					addedStates)) {
				System.out.println("JDLKFJDKLF");
				System.out.println(s);
				System.out.println(addedStates.contains(s));
				for (S s1 : addedStates) {
					if (s.compareTo(s1) == 0) {
						System.out.println(s1 + " ==== " + s);
					}
				}
			}

			Validate.isTrue(increase == addedStates.size(), increase + " "
					+ newStates.size() + " " + getStates().size());
		}

		return addedStates;
	}
}
