package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.probability.mdp.ActionsFunction;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.RewardFunction;
import aima.core.probability.mdp.TransitionProbabilityFunction;
import aima.core.probability.mdp.impl.MDP;




import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Grid;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.RewardFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import uk.ac.soton.ecs.utils.RandomUtils;
/**
 * 
 * @author 
 * @author Shaofei Chen
 */
public class MDPFactory {

	/**
	 * Constructs an MDP that can be used to generate the utility values 
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return an MDP that can be used to generate the utility values detailed
	 *         in Fig 17.3.
	 */
	
//	private Grid grid;
	
	public static MarkovDecisionProcess<StateNew, Move> createMDP(
			final Sensor sensor,final List<StateNew> stateSpace,final StateNew initialstate,
			TransitionFunctionNew<StateNew, Move> transitionFunction,final double uncertaintyIncrement) {
		
       
		return new MDP<StateNew, Move>(stateSpace,
				initialstate, createActionsFunction(sensor),
				createTransitionProbabilityFunction(sensor, transitionFunction),
				createRewardFunction(sensor,uncertaintyIncrement));
	}
	
	
//	public static List<StateNew> getStateSpace(Sensor sensor)
//	{
//		Grid grid=sensor.getEnvironment().getGrid();
//		List<Location> locationSpace=grid.getLocations();
//		
//		final double healthyIni=1.0;
//		final double healthyDecrement=0.2;
////		final double timeIni=1.0;
//		final double timeDecrement=1.0;
//		final double tau=5.0;		
//		double[] lastVistTimeIni=new double[locationSpace.size()];
//		
//		
//		
//		return null;
//		
//	}
//		for(int i = 0; i < locationSpace.size(); i++)
//		{
//			lastVistTimeIni[i]=1;
//		}
//		
//		List<StateNew> stateSpace=new ArrayList<StateNew>();
//		for (Location location : locationSpace)
//		{
//			for (Location locationforID : locationSpace)
//			{
//				StateNew state=new StateNew()
//				stateSpace.
//			}
//
//		}
		
		

	
	/**
	 * Returns the allowed actions from a specified cell within the cell world
	 * described in Fig 17.1.
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return the set of actions allowed at a particular cell. This set will be
	 *         empty if at a terminal state.
	 */
	public static ActionsFunction<StateNew, Move> createActionsFunction(
			final Sensor sensor) {
//		final Set<Cell<Double>> terminals = new HashSet<Cell<Double>>();
//		terminals.add(cw.getCellAt(4, 3));
//		terminals.add(cw.getCellAt(4, 2));

		ActionsFunction<StateNew, Move> af = new ActionsFunction<StateNew, Move>() {

			public List<Move> actions(StateNew s) {
				// All actions can be performed in each cell
				// (except terminal states)
				if (s.getHealthyLevel()==0) {
					return null;
				}
				return s.getLocation().getMoveOptions();
			}
		};
		return af;
	}

	/**
	 * Figure 17.1 (b) Illustration of the transition model of the environment:
	 * the 'intended' outcome occurs with probability 0.8, but with probability
	 * 0.2 the agent moves at right angles to the intended direction. A
	 * collision with a wall results in no movement.
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return the transition probability function as described in figure 17.1.
	 */
	public static TransitionProbabilityFunction<StateNew, Move> createTransitionProbabilityFunction(
			final Sensor sensor,final TransitionFunctionNew<StateNew,Move> transitionFunction) {
		TransitionProbabilityFunction<StateNew, Move> tf = new TransitionProbabilityFunction<StateNew, Move>() {
//			private double[] distribution = new double[] { 0.8, 0.1, 0.1 };
			private double[] distribution = new double[] {1.0};
			public double probability(StateNew sDelta, StateNew s,
					Move a) {
				double prob = 0;

				Map<StateNew, Double> successors = transitionFunction
						.transition(s, a);
				
				Validate.isTrue(successors.size() ==1);
				for (StateNew successor : new TreeSet<StateNew>(successors.keySet())) {
					if (sDelta.equals(successor)) {
						prob += successors.get(successor);
					}
				}
				
//				List<StateNew> outcomes = possibleOutcomes(s, a);
//				Set<StateNew> outcomes=successors.keySet();
//				for (int i = 0; i < successors.size(); i++) {
//					if (sDelta.equals(successors.)) {
//						// Note: You have to sum the matches to
//						// sDelta as the different actions
//						// could have the same effect (i.e.
//						// staying in place due to there being
//						// no adjacent cells), which increases
//						// the probability of the transition for
//						// that state.
//						prob += distribution[i];
//					}
//				}

				return prob;
			}
			
//			private List<StateNew> possibleOutcomes(StateNew c,
//					Move a) {
//				// There can be three possible outcomes for the planned action
//				List<StateNew> outcomes = new ArrayList<StateNew>();
//				transitionFunction.get
//				outcomes.add(a.getTargetLocation());
//				outcomes.add(c);
////				outcomes.add(cw.result(c, a.getSecondRightAngledAction()));
//
//				return outcomes;
//			}
		};

		return tf;
	}

	/**
	 * 
	 * @return the reward function which takes the content of the cell as being
	 *         the reward value.
	 */
	public static RewardFunction<StateNew> createRewardFunction (
			final Sensor sensor, final double uncertaintyIncrement) {
		RewardFunction<StateNew> rf = new RewardFunction<StateNew>() {
			
			public double reward(StateNew s) {
				
				double informationValue=s.getLastVisitTime(s.getLocation())*uncertaintyIncrement;
				
				double value = s.getHealthyLevel()*informationValue;
				
				return value;
			}
		};
		return rf;
	}
}
