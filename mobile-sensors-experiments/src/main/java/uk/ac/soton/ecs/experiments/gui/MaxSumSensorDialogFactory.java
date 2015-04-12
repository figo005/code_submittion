package uk.ac.soton.ecs.experiments.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import uk.ac.soton.ecs.mobilesensors.sensor.Sensor;

public class MaxSumSensorDialogFactory extends SensorDialogFactory implements
		ChangeListener {

	private JTable table;

	private Sensor sensor;

	private DefaultTableModel model;

	@Override
	public JFrame create(final Sensor sensor, EPanel panel) {
		this.sensor = sensor;
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());

		sensor.addChangeListener(this);

		table = new JTable(createTableModel());

		table.setPreferredScrollableViewportSize(new Dimension(300, 600));
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);

		frame.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JToggleButton fixSensorButton = new JToggleButton("Fix Sensor");
		fixSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO sensor.setFixed(!sensor.isFixed());
			}
		});

		JButton moveSensorButton = new JButton("Move Sensor");
		moveSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = new JOptionPane("Click on desired location",
						JOptionPane.INFORMATION_MESSAGE);

				// TODO sensor.setFixed(!sensor.isFixed());
			}
		});

		buttonPanel.setLayout(new GridBagLayout());

		buttonPanel.add(fixSensorButton);
		buttonPanel.add(moveSensorButton);

		frame.add(buttonPanel, BorderLayout.SOUTH);

		frame.setPreferredSize(new Dimension(300, 600));
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Details for sensor " + sensor.getID());

		return frame;
	}

	private TableModel createTableModel() {
		model = new DefaultTableModel();

		model.setColumnIdentifiers(new String[] { "Property", "Value" });

		model.addRow(new Object[] { "Class", "" });
		model.addRow(new Object[] { "Location", "" });
		model.addRow(new Object[] { "Fixed", "" });
		updateTable();

		return model;
	}

	private void updateTable() {
		model.setValueAt(sensor.getClass(), 0, 1);
		model.setValueAt(sensor.getLocation(), 1, 1);
		// TODO model.setValueAt(sensor.isFixed(), 2, 1);
	}

	public void stateChanged(ChangeEvent e) {
		updateTable();
	}

}
