package uk.ac.soton.ecs.experiments.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JButton startButton;

	private final JButton stepButton;

	private final JButton stopButton;

	private final JButton svgButton;

	private final JButton fasterButton;
	private final JButton slowerButton;
	private final JButton resetButton;

	public ControlPanel() {
		LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		add(startButton);

		stepButton = new JButton("Step");
		stepButton.setActionCommand("step");
		add(stepButton);

		stopButton = new JButton("Stop");
		stopButton.setActionCommand("stop");
		add(stopButton);

		svgButton = new JButton("Save to SVG");
		svgButton.setActionCommand("svg");
		add(svgButton);

		fasterButton = new JButton("Faster");
		fasterButton.setActionCommand("faster");
		add(fasterButton);

		slowerButton = new JButton("Slower");
		slowerButton.setActionCommand("slower");
		add(slowerButton);
		
		resetButton = new JButton("Reset");
		resetButton.setActionCommand("Reset");
		add(resetButton);

	}

	public void addActionListener(ActionListener listener) {
		startButton.addActionListener(listener);
		stepButton.addActionListener(listener);
		stopButton.addActionListener(listener);
		svgButton.addActionListener(listener);
		slowerButton.addActionListener(listener);
		fasterButton.addActionListener(listener);
		resetButton.addActionListener(listener);
	}

}
