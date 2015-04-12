package uk.ac.soton.ecs.experiments.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.LocationImpl;
import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;

public class SensorPickedListener implements ItemListener {

	private Simulation simulation;
	private EPanel environmentPanel;

	public SensorPickedListener(Simulation simulation,
			EPanel environmentPanel) {
		this.simulation = simulation;
		this.environmentPanel = environmentPanel;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getItem() instanceof LocationImpl) {
				LocationImpl location = (LocationImpl) e.getItem();

				Collection<Sensor> sensorsAtLocation = simulation
						.getSensorsAtLocation(location);

				for (Sensor sensor : sensorsAtLocation) {
					SensorDialogFactory
							.createInstance(sensor, environmentPanel);
				}
			}
		}
	}

}
