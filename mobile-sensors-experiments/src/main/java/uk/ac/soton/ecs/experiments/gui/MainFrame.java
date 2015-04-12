package uk.ac.soton.ecs.experiments.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.SimulationEventListener;
import uk.ac.soton.ecs.mobilesensors.configuration.Experiment;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.MultiSensorAction;
import uk.ac.soton.ecs.mobilesensors.worldmodel.ObservationInformativenessFunction;
import uk.ac.soton.ecs.mobilesensors.worldmodel.probpe.AbstractProbabilisticPursuitEvaderModel;

public class MainFrame extends JFrame implements SimulationEventListener {

	private static final long serialVersionUID = 1L;

	private final EPanel environmentPanel;
	private LablePane lablePane;
	
//	private final MessageTableModel messageTable;

	private final Experiment experiment;

	private SimulationThread runner;

	private double delay = .000000001;
	
	private double time=0;

	
	public void setInformationValue(double time) {
		this.time = time;
	}
	
	public SimulationThread getRunner() {
		return runner;
	}

	public MainFrame(Experiment experiment, boolean autostart) {
		this.experiment = experiment;

		experiment.getSimulation().addEventListener(this);

		// environmentPanel = new EnvironmentPanel2(experiment.getSimulation());
		environmentPanel = new EnvironmentPanel(experiment.getSimulation()
				.getEnvironment().getAccessibilityGraph());

		// addVertexClickListeners();
		setTitle("Simulation");

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(environmentPanel.getComponent(),
				BorderLayout.CENTER);

		ControlPanel controls = new ControlPanel();

		controls.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("start".equals(e.getActionCommand())) {
					startRunner();
				}
				if ("stop".equals(e.getActionCommand())) {
					stopRunner();
				}
				if ("step".equals(e.getActionCommand())) {
					runSingleRound();
				}
				if ("faster".equals(e.getActionCommand())) {
					delay *= 0.1;
					if (runner != null)
						runner.setDelay(delay);
				}
				if ("slower".equals(e.getActionCommand())) {
					delay *= 1.2;
					if (runner != null)
						runner.setDelay(delay);
				}
				if ("svg".equals(e.getActionCommand())) {
					// try {
					environmentPanel.saveToSVG();
					// } catch (IOException e1) {
					// e1.printStackTrace();
					// }
				}
				if ("Reset".equals(e.getActionCommand())) {
					// try {
					resetExperiment();
					// } catch (IOException e1) {
					// e1.printStackTrace();
					// }
				}
			}
		});

		add(controls, BorderLayout.EAST);
		
		lablePane=new LablePane(0);
		add(lablePane,BorderLayout.WEST);
		
		pack();
		setPreferredSize(new Dimension(1000, 800));
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		
//		messageTable=new MessageTableModel();
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		experiment.getSimulation().initialize();

		environmentPanel.resetViewer();

		
		
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				environmentPanel.resetViewer();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				environmentPanel.resetViewer();
			}
		});

		if (autostart) {
			startRunner();
			stopRunner();
		}
//		stopRunner();
	}

	public MainFrame(Experiment experiment) {
		this(experiment, false);
	}

	private void resetExperiment() {

				
		new Thread(new Runnable() {
			public void run() {
				try {
					MainFrame.this.experiment.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void runSingleRound() {
		new Thread(new Runnable() {
			public void run() {
				try {
					MainFrame.this.experiment.runSingleRound();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void addVertexClickListeners() {
		environmentPanel.addPickedStateListener(new SensorPickedListener(
				experiment.getSimulation(), environmentPanel));

		environmentPanel.addPickedStateListener(new GridPointPickedListener(
				experiment.getSimulation()));

	}

	public void handleEndOfRound(Simulation source, int round, double timestep) {
		update(source,timestep);
	}

	public void startRunner() {
		if (runner == null) {
			runner = new SimulationThread(experiment, delay);
			runner.start();
		}
	}

	public void stopRunner() {
		if (runner != null) {
			runner.stopRunning();
			runner = null;
		}
	}
	

	private void update(Simulation simulation, double time) {
		environmentPanel.setSensorLocations(simulation.getSensorLocationsMap());
//		environmentPanel.setRiskLocations(graph);

		ObservationInformativenessFunction function = simulation
				.getEnvironment().getInformativenessFunction();
		environmentPanel.setSpatialField(function.getValues());
		environmentPanel.setInformationStates(function.getInformationStates());
		environmentPanel.setRiskStates(function.getRiskStates());
		
		
		
		
		lablePane.updateLable(time,function.getTotalRewardGathered());
		
//		messageTable.setValueAt(obj, i, j);;
		
		

		if (function instanceof AbstractProbabilisticPursuitEvaderModel) {
			AbstractProbabilisticPursuitEvaderModel model = (AbstractProbabilisticPursuitEvaderModel) function;
			environmentPanel.setEvaderLocation(model.getEvaderLocation());
		}

		// if (function instanceof PatrollingInformativenessFunction) {
		// PatrollingInformativenessFunction model =
		// (PatrollingInformativenessFunction) function;
		// environmentPanel.setAttackLocations(model.getAttackLocations());
		// }
	}

	public void handleStartOfSimulation(Simulation source, double time) {
		update(source,time);
	}

	public void handleEndOfSimulation(Simulation source) throws Exception {
		// not handled
	}

	public void handleStartOfRound(Simulation source, int round, double timestep) {
		// not handled
	}
	

    public class LablePane extends JPanel {
    	JLabel l1 = new JLabel("Time:"+time);
    	JLabel l2 = new JLabel("Reward:"+0.0);

        public LablePane(double time) {
                        setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;

            add(l1, gbc);
            
            gbc.gridy++; 
            add(l2, gbc);
            gbc.gridy++; 

        }
        
        public void updateLable(double time, double reward)
        {
			DecimalFormat df = new DecimalFormat("0.00");
        	
        	l1.setText("Time:"+time);
        	l2.setText("Reward:"+ df.format(reward));
        }
    }
}


