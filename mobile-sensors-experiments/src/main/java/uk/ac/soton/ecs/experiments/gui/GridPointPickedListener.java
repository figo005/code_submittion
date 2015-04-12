package uk.ac.soton.ecs.experiments.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.LocationImpl;

public class GridPointPickedListener implements ItemListener {

	private Simulation simulation;

	public GridPointPickedListener(Simulation simulation) {
		this.simulation = simulation;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getItem() instanceof LocationImpl) {
				LocationImpl location = (LocationImpl) e.getItem();

				new GridPointDialog(location, simulation);
			}
		}

	}

}
