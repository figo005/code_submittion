package uk.ac.soton.ecs.experiments.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import maxSumController.discrete.bb.TripleStore;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.layout.LocationImpl;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorID;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorLocationHistory;
import uk.ac.soton.ecs.utils.CompressionUtils;
import util.HashTripleStore;

public class SimulationReplayFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private EPanel environmentPanel;

	private Log log = LogFactory.getLog(SimulationReplayFrame.class);

	// private Experiment experiment;

	private int currentRound = 1;

	private List<SensorLocationHistory> sensorLocations;

	private Map<Integer, Collection<Location>> attackLocations;

	private SensorLocationHistory evaderLocations;

	private TripleStore<Integer, Point2D, Double> valueHistory;

	public static void main(String[] args) throws IOException,
			InterruptedException {
		new SimulationReplayFrame(new File("/tmp/test_407_1263922382373/"));
	}

	public SimulationReplayFrame(File logFileDirectory) throws IOException,
			InterruptedException {
		setPreferredSize(new Dimension(800, 800));

		Validate.isTrue(logFileDirectory.exists());

		AccessibilityGraphImpl graph = readGraph(logFileDirectory);

		log.info("Reading sensor locations");
		sensorLocations = readSensorLocations(logFileDirectory);

		log.info("Reading evader locations");
		evaderLocations = readEvaderLocations(logFileDirectory);

		log.info("Reading attack locations");
		attackLocations = readAttackLocations(logFileDirectory);

		if (evaderLocations == null) {
			System.err.println("No evader locations found");
		}

		log.info("Reading value history");
		valueHistory = loadValueHistory(logFileDirectory);

		if (valueHistory == null) {
			System.err.println("NULL");
		} else {
			log.info("Value history successfully read from file");
		}

		environmentPanel = new EnvironmentPanel(graph);

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(environmentPanel.getComponent(),
				BorderLayout.CENTER);

		ControlPanel controls = new ControlPanel();

		controls.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("step".equals(e.getActionCommand())) {
					runSingleRound();
				}
				if ("svg".equals(e.getActionCommand())) {
					// try {
					environmentPanel.saveToSVG();
					// } catch (IOException e1) {
					// e1.printStackTrace();
					// }
				}
			}
		});

		// add(controls, BorderLayout.EAST);

		pack();

		// setExtendedState(JFrame.MAXIMIZED_BOTH);

		runSingleRound();

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		environmentPanel.resetViewer();

		addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				environmentPanel.resetViewer();

			}

			public void componentResized(ComponentEvent e) {
				environmentPanel.resetViewer();

			}
		});

		System.in.read();

		for (int i = 1; i < valueHistory.getKeySet1().size(); i++) {
			runSingleRound();
			Thread.sleep(500);
		}
	}

	private Map<Integer, Collection<Location>> readAttackLocations(
			File logFileDirectory) throws IOException {
		File file = new File(logFileDirectory, "attacks.txt");

		BufferedReader reader = new BufferedReader(new FileReader(file));

		Map<Integer, Collection<Location>> result = new HashMap<Integer, Collection<Location>>();

		String line;

		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);
			int timeStep = scanner.nextInt();
			double x = scanner.nextDouble();
			double y = scanner.nextDouble();

			if (!result.containsKey(timeStep)) {
				result.put(timeStep, new ArrayList<Location>());
			}

			result.get(timeStep).add(new LocationImpl(x, y));
		}

		return result;
	}

	private TripleStore<Integer, Point2D, Double> loadValueHistory(
			File logFileDirectory) throws IOException {
		File file = new File(logFileDirectory, "spatialFieldValues.txt");

		if (!file.exists()) {
			File zipFile = new File(logFileDirectory,
					"spatialFieldValues.txt.gz");

			if (!zipFile.exists()) {
				return null;
			}

			CompressionUtils.gunzip(zipFile, file);
		}

		Validate.isTrue(file.exists());

		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;

		while ((line = reader.readLine()).startsWith("%"))
			;

		TripleStore<Integer, Point2D, Double> values = new HashTripleStore<Integer, Point2D, Double>();

		while ((line = reader.readLine()) != null) {
			Scanner scanner = new Scanner(line);

			int timeStep = (int) scanner.nextDouble();
			double x = scanner.nextDouble();
			double y = scanner.nextDouble();
			double value = scanner.nextDouble();

			values.put(timeStep, new Point2D.Double(x, y), value);
		}

		return values;
	}

	private SensorLocationHistory readEvaderLocations(File logFileDirectory)
			throws IOException {
		File file = new File(logFileDirectory, "evader_locations");

		if (file.exists())
			return SensorLocationHistory.readFromFile(file);
		else
			return null;
	}

	private List<SensorLocationHistory> readSensorLocations(File directory)
			throws IOException {
		int i = 0;

		File sensorDirectory;

		List<SensorLocationHistory> result = new ArrayList<SensorLocationHistory>();

		while ((sensorDirectory = new File(directory, "sensor" + i)).exists()) {
			result.add(SensorLocationHistory.readFromFile(new File(
					sensorDirectory, "sensor_locations")));
			i++;
		}

		return result;
	}

	private AccessibilityGraphImpl readGraph(File logFileDirectory)
			throws IOException {
		File graphFile = new File(logFileDirectory, "graph.txt");
		return AccessibilityGraphImpl.readGraph(graphFile);
	}

	private void runSingleRound() {
		Map<SensorID, Location> locations = new HashMap<SensorID, Location>();
		for (SensorLocationHistory history : sensorLocations) {
			locations.put(history.getID(), history.get(currentRound));
		}

		environmentPanel.setSensorLocations(locations);

		if (evaderLocations != null)
			environmentPanel.setEvaderLocation(evaderLocations
					.get(currentRound));

		if (attackLocations != null) {
			environmentPanel.setAttackLocations(attackLocations
					.get(currentRound));
		}

		Map<Point2D, Double> values = valueHistory.get(currentRound);

		environmentPanel.setSpatialField(values);

		currentRound++;
	}
}
