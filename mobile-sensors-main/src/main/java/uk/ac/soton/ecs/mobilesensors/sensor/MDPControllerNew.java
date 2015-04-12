package uk.ac.soton.ecs.mobilesensors.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.ClusteredGraph;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.layout.gui.GraphGUI;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.IntraClusterPatrollingStrategy;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.RewardFrame;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.SensorController;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.StateFrame;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public class MDPControllerNew  extends AbstractCentralisedController {

	
	

	protected int tau;


	private boolean gui = true;
	
	
	
	
	
	private static final boolean DEBUG = true;
	private boolean autoMode = false;
	private double[] expectedTotalReward;
	private int step;
	private double[] actualTotalReward;
	private boolean coordinate = false;
	private double[] initialStateValues;
	private int[] stateCountBefore;
	private boolean[] repaired;
	
	private int currentMoveIndex = 0;

	public MDPControllerNew() {
		// TODO Auto-generated constructor stub
	}



	@Override
	protected Map<Sensor, Move> computeMoves() {
		// TODO Auto-generated method stub

		Map<Sensor, Move> moves = new HashMap<Sensor, Move>();
			currentMoveIndex = 0;


			for (int i = 0; i < getSensors().size(); i++) {
				Sensor sensor = getSensors().get(i);
				
                Location currentlocation=sensor.getLocation(); 
                List<Move> moveOptions = sensor.getLocation().getMoveOptions();
                
                Move nextMove = getMoveForCurrentState(currentlocation, moveOptions);
				moves.put(sensor, nextMove);
				
//				ObservationInformativenessFunction informativenessFunction = getInformativenessFunction();

			}
			
			log.info("Recomputed paths:");

			if (log.isInfoEnabled()) {
				for (Sensor sensor : getSensors()) {
					log.info(sensor.getID() + " " + moves.get(sensor));
				}
			}

		
		
		return null;
	}
	private Move getMoveForCurrentState(Location location, List<Move> moveOptions) {
	
		Random randomNumber=new Random();	
		return moveOptions.get(randomNumber.nextInt(moveOptions.size()-1));
	}
	
	public void handleEndOfRound(int round, double timestep) {
		// TODO Auto-generated method stub

	}

	public void finaliseLogs() throws Exception {
		// TODO Auto-generated method stub

	}
	protected ObservationInformativenessFunction getInformativenessFunction() {
		return sensors. .getEnvironment().getInformativenessFunction();
	}


}
