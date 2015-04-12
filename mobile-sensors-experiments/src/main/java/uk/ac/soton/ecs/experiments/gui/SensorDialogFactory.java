package uk.ac.soton.ecs.experiments.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;

public abstract class SensorDialogFactory {

	private static Map<Class<? extends Sensor>, SensorDialogFactory> factories = new HashMap<Class<? extends Sensor>, SensorDialogFactory>();

	static {
		factories.put(Sensor.class, new MaxSumSensorDialogFactory());
	}

	public static JFrame createInstance(Sensor sensor,
			EPanel environmentPanel) {
		SensorDialogFactory factory = getFactory(sensor.getClass());

		if (factory != null) {
			return factory.create(sensor, environmentPanel);
		}

		return null;
	}

	private static SensorDialogFactory getFactory(Class<? extends Sensor> clazz) {
		Class<?> type = clazz;

		while (type != null) {
			if (factories.containsKey(type))
				return factories.get(type);

			type = type.getSuperclass();
		}

		return null;
	}

	public abstract JFrame create(Sensor sensor, EPanel panel);

}
