package uk.ac.soton.ecs.mobilesensors.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.Validate;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.Location;

public class AccessibilityGraphReader {

	public AccessibilityGraphImpl load(FileReader fileReader, double scale)
			throws IOException {
		BufferedReader reader = new BufferedReader(fileReader);

		String line = reader.readLine();

		AccessibilityGraphImpl graph = new AccessibilityGraphImpl();

		Map<Integer, Location> locationIndices = new HashMap<Integer, Location>();

		int vertices = Integer.parseInt(line.substring(10));

		for (int i = 0; i < vertices; i++) {
			line = reader.readLine();
			Scanner scanner = new Scanner(line);
			int index = scanner.nextInt();

			double locationX = scanner.nextDouble() * scale;
			double locationY = scanner.nextDouble() * scale;
//			double riskProbability = scanner.nextDouble() * scale;
			double riskProbability=0;
			locationIndices.put(index, graph.addAccessibleLocation(locationX,
					locationY,index,riskProbability));
		}

		line = reader.readLine();

		Validate.isTrue(line.startsWith("*Edges"));
		
		int lines = Integer.parseInt(line.substring(7));
		
	
		for (int i = 0; i < lines; i++) {
			line = reader.readLine();
			Scanner scanner = new Scanner(line);
			int vertexIndex1 = scanner.nextInt();
			int vertexIndex2 = scanner.nextInt();

			Location location1 = locationIndices.get(vertexIndex1);
			Location location2 = locationIndices.get(vertexIndex2);

			graph.addAccessibilityRelation(location1, location2);

			
		}

		
		line = reader.readLine();
		Validate.isTrue(line.startsWith("*Risk Vertices"));
		
		int number_RiskVertices = Integer.parseInt(line.substring(15));		

		for (int i = 0; i < number_RiskVertices; i++) {
			line = reader.readLine();
			Scanner scanner = new Scanner(line);
			int vertexIndex = scanner.nextInt();			
			Location location = locationIndices.get(vertexIndex);
			graph.addAccessibleRiskLocation(vertexIndex, location);
			
		}
		
		
		line = reader.readLine();
		Validate.isTrue(line.startsWith("*Markov risk model"));
		int number_RiskStates = Integer.parseInt(line.substring(19));
		
		double markovRiskModelTemp1[][]=new double[number_RiskStates][number_RiskStates];
		double markovRiskModelTemp2[][]=new double[number_RiskStates][number_RiskStates];

		for (int i = 0; i < number_RiskStates; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);			
			for(int j = 0; j < number_RiskStates; j++)
			{
				double value = scanner.nextDouble();			
				markovRiskModelTemp1[i][j]=value;
			}
		}
		
		for (int i = 0; i < number_RiskStates; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);			
			for(int j = 0; j < number_RiskStates; j++)
			{
				double value = scanner.nextDouble();			
				markovRiskModelTemp2[i][j]=value;
			}
		}
		
		graph.addAccessibleMarkovRiskModel(locationIndices.size(),markovRiskModelTemp1,markovRiskModelTemp2);
		
		
		line = reader.readLine();
		Validate.isTrue(line.startsWith("*Markov information model"));
		int number_InformationStates = Integer.parseInt(line.substring(26));
		
		double markovInformationModelTemp1[][]=new double[number_InformationStates][number_InformationStates];
		double markovInformationModelTemp2[][]=new double[number_InformationStates][number_InformationStates];

		for (int i = 0; i < number_InformationStates; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);			
			for(int j = 0; j < number_InformationStates; j++)
			{
				double value = scanner.nextDouble();			
				markovInformationModelTemp1[i][j]=value;
			}
		}
		for (int i = 0; i < number_InformationStates; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);			
			for(int j = 0; j < number_InformationStates; j++)
			{
				double value = scanner.nextDouble();			
				markovInformationModelTemp2[i][j]=value;
			}
		}
		
		graph.addAccessibleMarkovInformationModel(locationIndices.size(),markovInformationModelTemp1,markovInformationModelTemp2);
		
		
		line = reader.readLine();
		Validate.isTrue(line.startsWith("*Risk state and belief"));
		


		for (int i = 0; i < vertices; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);
			
			int index = scanner.nextInt();
			int riskState = scanner.nextInt();
			double riskBelief[] = new double[number_RiskStates];
			
			for(int j = 0; j < number_RiskStates; j++)
			{
				double value = scanner.nextDouble();			
				riskBelief[j]=value;
			}
			
			graph.addAccessibleRiskStateAndBelief(index, riskState, riskBelief);
		}
		
		
//		double markovInformationModelTemp[][]=new double[number_InformationStates][number_InformationStates];
		
		line = reader.readLine();
		Validate.isTrue(line.startsWith("*Information state and belief"));
		


		for (int i = 0; i < vertices; i++) {
			line = reader.readLine();	
			Scanner scanner = new Scanner(line);
			
			int index = scanner.nextInt();
			int InformationState = scanner.nextInt();
			double InformationBelief[] = new double[number_InformationStates];
			
			for(int j = 0; j < number_InformationStates; j++)
			{
				double value = scanner.nextDouble();			
				InformationBelief[j]=value;
			}
			
			graph.addAccessibleInformationStateAndBelief(index, InformationState, InformationBelief);
		}

		return graph;

	}

	public AccessibilityGraphImpl load(FileReader fileReader)
			throws IOException {
		return load(fileReader, 1.0);
	}

}
