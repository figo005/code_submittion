package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Collections;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.Move;

public abstract class DeterministicTransitionFunctionNew<S extends StateNew, A extends Move>
		implements TransitionFunctionNew<S, A> {

	public final Map<S, Double> transition(S state, A nextAction) {
		return Collections.singletonMap(deterministicTransition(state,
				nextAction), 1.0);
	}

	protected abstract S deterministicTransition(S state,
			A nextAction);
}
