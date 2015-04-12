package uk.ac.soton.ecs.mobilesensors.sensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.mobilesensors.Environment;
import uk.ac.soton.ecs.mobilesensors.InitialPlacement;
import uk.ac.soton.ecs.mobilesensors.Move;
import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.SimulationEvent;
import uk.ac.soton.ecs.mobilesensors.communication.CommunicationModule;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.metric.LogWriter;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.CoordinationMechanism;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.HealthyBelief;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.NoOpCoordinationMechanism;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.StateNew;
import uk.ac.soton.ecs.mobilesensors.worldmodel.Observation;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;

public class Sensor implements LogWriter {
	protected Location location;
	
//	protected HealthyBelief healthyBelief;
	private final double[] ProbabilityDistributionIni={1.0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
	protected SensorID id;

	protected Environment environment;

	protected Simulation simulation;

	protected SensorLocationHistory history;

	protected CoordinationMechanism coordinationMechanism = new NoOpCoordinationMechanism();

	protected CommunicationModule communicationModule;

	protected double currentTime;

	protected File outputDirectory;

	protected static Log log = LogFactory.getLog(Sensor.class);

	private InitialPlacement initialPlacement;

	private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

	private final Collection<LogWriter> logWriters = new ArrayList<LogWriter>();

	private Collection<? extends Observation> observations;

	private double observationValue;
	
	private double observationValueTotal;
	
	private double discountFactor;
	
//	private double healthyDiscountFactor;
	

	private HealthyBelief healthyIni;
	
	
	private int realHealth=10;
	private final int healthyDecrement=1;
	
	private StateNew currentState;
	
	protected int riskObservationForCurrentPosition;
	
	protected int informationObservationForCurrentPosition;



	private boolean failed;

	public void setSensorID(SensorID id) {
		this.id = id;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public CommunicationModule getCommunicationModule() {
		return communicationModule;
	}

	public final void setOutputDirectory(File dir) {
		Validate.notNull(dir);
		Validate.isTrue(dir.exists());
		this.outputDirectory = new File(dir, "sensor" + getID());
		try {
			FileUtils.forceMkdir(outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

		for (LogWriter logWriter : logWriters) {
			logWriter.setOutputDirectory(this.outputDirectory);
		}
	}

	public final void setEnvironment(Environment environment) {
		Validate.notNull(environment);

		this.environment = environment;
	}

	public final Location getLocation() {
		return location;
	}
	
	public final int getInformationObservationForCurrentPosition() {
		return informationObservationForCurrentPosition;
	}
	public final int getRiskObservationForCurrentPosition() {
		return riskObservationForCurrentPosition;
	}
//	public final HealthyBelief getHealthyBelief() {
//		return healthyBelief;
//	}
	
	public final double getObservationValueTotal() {
		return observationValueTotal;
	}
	public final double getObservationValue() {
		return observationValue;
	}

	protected final ObservationInformativenessFunction getInformativenessFunction() {
		return getEnvironment().getInformativenessFunction();
	}

	private final void observeEnvironment() {
		double value = getInformativenessFunction().getInformativeness(
				getLocation());
		boolean beenHurt = getInformativenessFunction().updateRiskBilief(getLocation());


//		if(beenHurt==true)
//			realHealth=Math.max(realHealth-healthyDecrement,0);
		
//		observationValue += 0.1*getCurrentState().getHealthyLevelBelief().getExpectHealthy()*value * Math.pow(discountFactor, currentTime);
//		observationValue += 0.1*getCurrentState().getHealthyLevelBelief().getExpectHealthy()*value ;
		observationValue += value ;

		observations = getInformativenessFunction().observe(getLocation());
		
		riskObservationForCurrentPosition=getInformativenessFunction().getRiskStates().get(location.getID());
		informationObservationForCurrentPosition=getInformativenessFunction().getInformationStates().get(location.getID());

		
	}

	public void setDiscountFactor(double discountFactor) {
		this.discountFactor = discountFactor;
	}
	
//	public void setHealthyDiscountFactor(double healthyDiscountFactor) {
//		this.healthyDiscountFactor = healthyDiscountFactor;
//	}

	public double getDiscountFactor() {
		return discountFactor;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Sensor) {
			Sensor sensor = (Sensor) obj;
			return sensor.getID().equals(id);
		}
		return false;
	}

	public final SensorID getID() {
		return id;
	}

	public void setCommunicationModule(CommunicationModule module) {
		communicationModule = module;
	}

	public void handleTimerEvent(double time) {
		currentTime = time;
	}

	public void setInitialLocation(Location location) {
		this.location = location;
		history = new SensorLocationHistory(getID(), location);
	}
	
//	public void setInitialHealthy(int healthy) {
//		this.healthy = healthy;
//	}

	@Required
	public void setCoordinationMechanism(CoordinationMechanism mechanism) {
		this.coordinationMechanism = mechanism;
		mechanism.setSensor(this);
	}

	public CoordinationMechanism getCoordinationMechanism() {
		return coordinationMechanism;
	}

	public SensorLocationHistory getLocationHistory() {
		return history;
	}

	public final void initialize(Simulation simulation) {
		Validate.notNull(environment);
		Validate.notNull(location);
		Validate.notNull(id);
		Validate.notNull(history);
		Validate.notNull(coordinationMechanism);

		
		this.observationValueTotal=0;

//		this.healthyIni = new HealthyBelief(ProbabilityDistributionIni);
		
//		setCurrentState();

		
		this.simulation = simulation;
		coordinationMechanism.initialize(simulation);
		
		simulation.addLogWriter(this);
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void addLogWriter(LogWriter writer) {
		logWriters.add(writer);
	}

	public final void handleEndOfRound(int round, double timestep) {

	}

	public Simulation getSimulation() {
		return simulation;
	}

	public void doMove() {
		Move bestMove = coordinationMechanism
				.determineBestMove(getCurrentTime());
		Location targetLocation = bestMove.getTargetLocation();
		move(targetLocation);
	}

	private void move(Location newLocation) {
		if (failed)
			newLocation = location;

		history.addLocation(newLocation, getCurrentTime());

		log.info(String.format("Sensor %s moving from %s to %s", id,
				this.location, newLocation));
		this.location = newLocation;

		fireChangeEvents();
	}

	/**
	 * Run a single round
	 */
	public final List<? extends SimulationEvent> startRound() {
		log.debug("Sensor " + id + " starting new round");

		

		return Collections.singletonList(new SimulationEvent() {
			public List<? extends SimulationEvent> run() {
				doMove();
				
				observeEnvironment();
				
//				setCurrentState();
				coordinationMechanism.updateFrame();
				

				log.debug("Sensor " + id + " finished round");

				return Collections.emptyList();
			}
		});
	}

	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * The initialplacement of the simulation can be overridden by giving the
	 * sensors a initialplacement
	 * 
	 * @return
	 */
	public InitialPlacement getInitialPlacement() {
		return initialPlacement;
	}

	public final void setInitialPlacement(InitialPlacement placement) {
		this.initialPlacement = placement;
	}

	public final void finaliseLogs() throws Exception {
		for (LogWriter logWriter : logWriters) {
			logWriter.finaliseLogs();
		}

		FileUtils.writeStringToFile(new File(outputDirectory,
				"sensor_locations"), history.toString());

		FileUtils.writeStringToFile(new File(outputDirectory,
				"observation_value"), observationValue + "\n");
	}

	public final void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	private void fireChangeEvents() {
		for (ChangeListener listener : listeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}

	public Collection<? extends Observation> getObservations() {
		return observations;
	}

	@Override
	public String toString() {
		return id.toString();
	}

	public void fail() {
		failed = true;

	}

	public boolean hasFailed() {
		return failed;
	}
	
	public HealthyBelief getHealthyIni() {
		return healthyIni;
	}
	
	public int getRealHealth() {
		return realHealth;
	}
	
//	public int getHealthyDecrement() {
//		return healthyDecrement;
//	}

//	public void setHealthyDecrement(int healthyDecrement) {
//		this.healthyDecrement = healthyDecrement;
//	}
	
	public StateNew getCurrentState() {		
		return currentState;
	}
	
	public void setCurrentState() {
		
		int[] lastVisitTimes = currentState.getLastVisitTimes();
		int[] newVisitTimes = new int[lastVisitTimes.length];
		
		for (int i = 0; i < newVisitTimes.length; i++) {
			newVisitTimes[i] = lastVisitTimes[i]+1;
			
		}
		newVisitTimes[location.getID()] = 0;

		//update the current HealthyBelief use current realHealth
		currentState = new StateNew(location, newVisitTimes,new HealthyBelief(realHealth));
	}
	
	public void setCurrentState(StateNew iniState) {
		
		currentState = iniState;
	}
	
	public void delete() {
		
		coordinationMechanism.delete();
	}
	public void reset() {
		
		coordinationMechanism.reset();
	}
}
