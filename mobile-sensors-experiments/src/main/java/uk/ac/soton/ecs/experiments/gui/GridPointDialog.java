package uk.ac.soton.ecs.experiments.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.SimulationEventListener;
import uk.ac.soton.ecs.mobilesensors.layout.LocationImpl;

public class GridPointDialog extends JFrame implements SimulationEventListener {

	private LocationImpl location;

	private Simulation simulation;

	// private XYSeries series1;

	private XYSeries series2;

	public GridPointDialog(LocationImpl location, Simulation simulation) {
		this.location = location;
		this.simulation = simulation;

		setTitle("Grid Point @ (" + location.getX() + ", " + location.getY()
				+ ")");

		simulation.addEventListener(this);

		// series1 = new XYSeries("Variance");
		series2 = new XYSeries("Variance");

		// for (int i = 0; i < simulation.getCurrentRound(); i++) {
		// double time = simulation.getTimeAtRound(i);
		// // series1.add(time, simulation.getRecordedVarianceAt(location,
		// // time));
		// series2.add(time, simulation.getVariance(location, time));
		// }

		update();
		XYSeriesCollection dataset = new XYSeriesCollection();
		// dataset.addSeries(series1);
		dataset.addSeries(series2);

		JFreeChart chart = ChartFactory.createXYLineChart("Variance", "Round",
				"Variance", dataset, PlotOrientation.VERTICAL, true, true,
				false);

		ChartPanel panel = new ChartPanel(chart);

		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	private void update() {
		double time = simulation.getTime();
		// series1.add(time, simulation.getRecordedVarianceAt(location, time));
		// TODO
		// series2.add(time, simulation.getVariance(location, time));
	}

	public void handleEndOfRound(Simulation source, int round, double timestep) {
		update();
	}

	public void handleEndOfSimulation(Simulation source) throws Exception {
		// TODO Auto-generated method stub

	}

	public void handleStartOfRound(Simulation source, int round, double timestep) {
		// TODO Auto-generated method stub

	}

	public void handleStartOfSimulation(Simulation source, double time) {
		update();

	}

}
