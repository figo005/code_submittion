package uk.ac.soton.ecs.mobilesensors.sensor.coordination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.ac.soton.ecs.mobilesensors.layout.Cluster;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.coordination.centralised.State;

public class HealthyBelief implements Comparable<HealthyBelief>{

	private final int[] Healthylevels={10,9,8,7,6,5,4,3,2,1,0};

	private double[] ProbabilityDistribution=new double [Healthylevels.length];
	private final double expectHealthy;
	private final Map<Integer, Double> beliefMap;


	public HealthyBelief(double[] ProbabilityDistribution) {

		Validate.isTrue(ProbabilityDistribution.length==Healthylevels.length);
		
		this.ProbabilityDistribution=ProbabilityDistribution;
		this.beliefMap=setBeliefMap();
		this.expectHealthy=setExpectHealthy();
	}
	
	public HealthyBelief(int realhealth) {

		for(int i=0;i<Healthylevels.length;i++)
		{
			if(Healthylevels[i]==realhealth)			
				ProbabilityDistribution[i]=1.0;
			else
				ProbabilityDistribution[i]=0.0;
			
		}
		this.beliefMap=setBeliefMap();
		this.expectHealthy=setExpectHealthy();
	}
	
	
	
	
	public HealthyBelief(Map<Integer, Double> beliefMap) {
		Validate.isTrue(beliefMap.size()==Healthylevels.length);
		this.beliefMap=beliefMap;
		
		this.ProbabilityDistribution=setProbabilityDistribution();

		this.expectHealthy=setExpectHealthy();
	}

	public Map<Integer, Double> getBeliefMap() {

		return beliefMap;
	}
	
	public Map<Integer, Double> setBeliefMap() {
		//.descendingMap() to make reading beliefMap from 10 to 0;
		Map<Integer, Double> beliefMaptemp = new TreeMap<Integer, Double>().descendingMap(); 
//		for (int i = Healthylevels.length-1; i >=0; i--)	
		for (int i = 0; i <Healthylevels.length; i++)
			beliefMaptemp.put(Healthylevels[i],ProbabilityDistribution[i]);

		return beliefMaptemp;
	}

	public double[] getProbabilityDistribution() {

		return ProbabilityDistribution;
	}
	public double[] setProbabilityDistribution() {
		double[] ProbabilityDistribution=new double [Healthylevels.length];
		for (int i = 0; i < Healthylevels.length; i++)
			ProbabilityDistribution[i]=beliefMap.get(Healthylevels[i]);
		
		return ProbabilityDistribution;
	}
	
	public double setExpectHealthy()
	{
		double expectHealthy=0;
		for (int i = 0; i < Healthylevels.length; i++)
			expectHealthy+=Healthylevels[i]*ProbabilityDistribution[i];
		return expectHealthy;
		
	}	
	
	public double getExpectHealthy()
	{
		return expectHealthy;
		
	}
	public double getProbability(int level)
	{
		return beliefMap.get(level);
		
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < ProbabilityDistribution.length; i++) {
			builder.append(ProbabilityDistribution[i] + " ");
		}
		builder.append(ProbabilityDistribution.toString());

		return builder.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(ProbabilityDistribution)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HealthyBelief) {
			HealthyBelief healthyBelief = (HealthyBelief) obj;

			return Arrays.equals(healthyBelief.ProbabilityDistribution,
					ProbabilityDistribution);
		}

		return false;
	}




	public int compareTo(HealthyBelief h) {
		HealthyBelief healthyBelief = (HealthyBelief) h;

		for (int i = 0; i < ProbabilityDistribution.length; i++) {
			if (ProbabilityDistribution[i] != healthyBelief.ProbabilityDistribution[i]) {
				{ 
					int temp=(int)(10*(ProbabilityDistribution[i]- healthyBelief.ProbabilityDistribution[i]));
					return Math.max(1,temp);
				}
			}
		}
		

		if (!equals(h)) {
			System.out.println("JKLDFJ");
			System.out.println(this);
			System.out.println(h);
//			System.out.println(s.getHealthyLevel());
			System.out.println(hashCode());
			System.out.println(h.hashCode());
		}

		Validate.isTrue(equals(h));

		return 0;

	}

	// remove the state of sensor with given index
//	public void remove(int index) {
//		sensorStates = (SensorPositionState[]) ArrayUtils.remove(sensorStates,
//				index);
//	}
}
