package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.StateNew;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.TransitionFunctionNew;
import aima.core.agent.Action;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.Policy;
import aima.core.probability.mdp.PolicyEvaluation;
import aima.core.probability.mdp.impl.LookupPolicy;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 657.<br>
 * <br>
 * 
 * <pre>
 * The backtracking algorithm for MDP
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Shaofei Chen
 * @author Ravi Mohan
 * 
 */
public class FiniteBackTracking<S extends StateNew, A extends Action> {

//	private MarkovDecisionProcess<S, A> mdp;
	int backTrackingStepsNumber;
	protected DirectedSparseMultigraph<S, StateTransitionNew<S,A>> graph = new DirectedSparseMultigraph<S, StateTransitionNew<S,A>>();
	private final TransitionFunctionNew<S,A> transitionFunction;
	private final double gama;
	private final double uncertaintyIncrement;
	/**
	 * Constructor.
	 * 
	 * @param policyEvaluation
	 *            the policy evaluation function to use.
	 */
	public FiniteBackTracking(TransitionFunctionNew<S,A> transitionFunction,
			int backTrackingStepsNumber,double gama,double uncertaintyIncrement) {
//		this.mdp = mdp;
		this.backTrackingStepsNumber=backTrackingStepsNumber;
		this.transitionFunction=transitionFunction;
		this.gama=gama;
		this.uncertaintyIncrement=uncertaintyIncrement;
	}
	public Collection<S> getSuccessors(S state) {
		return graph.getSuccessors(state);
	}
	public A getAction(S state,S succesor) {
		return graph.findEdge(state, succesor).getAction();
	}
	
	protected void addTransition(S state, A action,
			S successor, double probability) {
		StateTransitionNew<S,A> transition = new StateTransitionNew<S,A>(state, action,
				successor, probability);

		if (graph.containsEdge(transition))
			return;

		int edgeCount = graph.getEdgeCount();

		graph.addEdge(transition, state, successor);

//		Validate.isTrue(graph.getEdgeCount() == edgeCount + 1);
	}
	

	// function POLICY-ITERATION(mdp) returns a policy
	/**
	 * The policy iteration algorithm for calculating an optimal policy.
	 * 
	 * @param mdp
	 *            an MDP with states S, actions A(s), transition model P(s'|s,a)
	 * @return an optimal policy
	 */
	public A doBackTracking(S initialState) {
		

		graph = new DirectedSparseMultigraph<S, StateTransitionNew<S,A>>();
		ArrayList<S> cur_states=new ArrayList<S>();
		cur_states.add(initialState);
		
		//visited_states store all the states as steps of backTrackingStepsNumber visit
		ArrayList<ArrayList<S>> visited_states = new ArrayList<ArrayList<S>>(backTrackingStepsNumber);
		
		ArrayList<S> temp=new ArrayList<S>();
		temp.add(initialState);
		visited_states.add(temp);
		
		
		
		for (int steps_to_go = backTrackingStepsNumber; steps_to_go > 0 && cur_states != null; steps_to_go--)
		{
			ArrayList<S> found_states=new ArrayList<S>();
			for (S state : cur_states) {
			List<A> actions = transitionFunction.getActions(state);
			for(A action : actions)
			{
				Map<S, Double> successors = transitionFunction.transition(state, action);
				
				for (S successor : new TreeSet<S>(successors.keySet())) {
//					if (!graph.containsVertex(successor)) {
//						found_states.add(successor);					
//					}
					if(!found_states.contains(successor))
					{
					found_states.add(successor);
					addTransition(state, action, successor,
							successors.get(successor));
					}
				}					
						
			}
			}
			cur_states.clear();
			cur_states.addAll(found_states);
			visited_states.add(found_states);

			
		}
		
		Map<S, Double> Vmap = new HashMap<S, Double>();
		for (int depth = visited_states.size() -1 ; depth >= 0; depth--) {
			{
				cur_states = visited_states.get(depth);
				
				Map<S, Double> Vmap_currentdepth = new HashMap<S, Double>();
				

				
				

				for (S s : cur_states) {
					
//					Map<StateNew, Double> Vmap1 = new HashMap<StateNew, Double>();
//					int last[]={2,2,2,2,2,2,2,2,2,2};
//					StateNew ss = new StateNew(s.location,last,2);
//					StateNew ss1=new StateNew(s.location,last,2);
//					
//					Vmap1.put(ss,1.0);
//					Vmap1.put(ss1,2.0);
//					
					
					
					double vMaxDeta = 0;
					S sMax=null;
					
					for (S sDelta : getSuccessors(s)) {
						double vtemp;
						if(depth== visited_states.size() - 1)
							vtemp=reward(sDelta);							
						else
//							if(!Vmap.containsKey(sDelta))
//							{
//								vtemp=0;
//								if(visited_states.get(depth+1).contains(sDelta))
//									vtemp=0;
//							}
							vtemp=Vmap.get(sDelta);
						
						if (vtemp > vMaxDeta) {
							vMaxDeta = vtemp;
							sMax=sDelta;
						}
					}
					
					if (depth == 0)
						if(sMax==null)
							return null;
						else
							return(getAction(s,sMax));
					else
						Vmap_currentdepth.put(s, reward(s)+gama*vMaxDeta);		
				}
				
				Vmap.clear();//After each depth, clear Vmap, avoid two same state may occuer in different depth;
				Vmap=Vmap_currentdepth;
				
			}
		}
		return null;
	
	}

	public double reward(S s) {
			
		double informationValue=s.getLastVisitTime(s.getLocation())*uncertaintyIncrement;
		
		double value = 0.1*s.getHealthyLevelBelief().getExpectHealthy()*informationValue;
	
	return value;

	}

}