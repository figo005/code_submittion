package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.List;
import java.util.Map;

import uk.ac.soton.ecs.mobilesensors.Move;

public interface TransitionFunctionNew<S,A> {

	Map<S, Double> transition(S state, A nextAction);
	


	List<A> getActions(S state);

}
