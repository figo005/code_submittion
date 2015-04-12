package uk.ac.soton.ecs.experiments.gui;


import java.io.PrintStream;

import uk.ac.soton.ecs.mobilesensors.configuration.Experiment;




public class Boot {

	public static final String LOCAL_GREEDY = "src/main/resources/sensor/new/sensor_greedy.xml";
	public static final String GLOBAL_GREEDY = "src/main/resources/sensor/new/sensor_global_greedy.xml";
	public static final String JUMPING_GREEDY = "src/main/resources/sensor/new/sensor_jumping_greedy.xml";
	public static final String TSP_LARGE_ROOM = "src/main/resources/sensor/new/sensor_tsp_large_room.xml";
	public static final String TSP_B32 = "src/main/resources/sensor/new/sensor_tsp_b32.xml";
	public static final String GLOBAL_RANDOM = "src/main/resources/sensor/new/sensor_global_random.xml";
	public static final String MAX_SUM = "src/main/resources/sensor/new/sensor_max_sum.xml";
	public static final String MAX_SUM_LOCAL = "src/main/resources/sensor/new/sensor_max_sum_local_cluster15.xml";
	public static final String MDP = "src/main/resources/sensor/new/sensor_mdp.xml";
	public static final String HIERARCHICAL_MDP = "src/main/resources/sensor/new/sensor_hierarchical_mdp.xml";
	public static final String POMDP = "src/main/resources/sensor/new/sensor_pomdp.xml";
	public static final String LOCAL_GREEDY_new = "src/main/resources/sensor/new/sensor_greedy.xml";	
	public static final String GLOBAL_RANDOM_new = "src/main/resources/sensor/new/sensor_global_random_new.xml";	
	public static final String mdpCoordination = "src/main/resources/sensor/new/sensor_mdp_coordination.xml";	
	public static final String pomdpCoordination = "src/main/resources/sensor/new/sensor_pomdp_coordination.xml";	
	public static final String GLOBAL_RANDOM_new2 = "src/main/resources/sensor/new/sensor_global_random_new2.xml";	
	public static final String pomdpCentralised = "src/main/resources/sensor/new/sensor_pomdp.xml";	

	public static void main(String[] args) throws Exception {
		int agentsNumber = 1;
		
		int experimentTimes=100;
		int steps=200;
		double[] result=new double[experimentTimes];
		double resulttotal=0, average=0;
		
		
		for(int run=0;run<experimentTimes;run++)
		{
		
		Experiment experiment = new Experiment();
		experiment.setSensorCount(agentsNumber);
		experiment.setSensorDefinitionFile(pomdpCentralised);

		experiment.setSimulationDefinitionFile
//		("src/main/resources/simulation/new/smallgraph.xml");
		   ("src/main/resources/simulation/new/smallgraph_differentMarkov.xml");
//		("src/main/resources/simulation/new/b32POMDP.xml");
//		   ("src/main/resources/simulation/new/b32POMDP_differentMarkov.xml");
		experiment.setRootOutputDirectory("/tmp/sensor_" + agentsNumber);

		experiment.initialize();
		
		experiment.getSimulation().initialize();
		
		if(run!=0) experiment.getSimulation().reset();
		
		
//		experiment.run();
//		double delay=0.1;
//		SimulationThread runner = new SimulationThread(experiment, delay);
//		runner.start();
		
		for(int j=0; j<steps; j++)
		{
			experiment.runSingleRound();
		}

//		new MainFrame(experiment, true);
		double resulttemp=experiment.getSimulation().getEnvironment().getInformativenessFunction().getTotalRewardGathered();
		result[run]=resulttemp;
		resulttotal+=resulttemp;
		
		experiment.getSimulation().deleteSensors();
		
		}
		average=resulttotal/experimentTimes;
		average=average;
		
		PrintStream out = System.out;
    	out.println( "average reward : "+average );

	}
}
