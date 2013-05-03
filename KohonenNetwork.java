package TSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class KohonenNetwork{

	private int numNodes = 0;
	private int numNeurons = 0;
	private double neuron[][] = null;
	private double weight[][] = null;
	private double theta = 0.8;
	private double alpha = 0.8;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	private static final double DECAY = 0.995;

	
	public void updateWeights(){

		// Pick a Random Node
		int index = (int)(Math.random() * numNodes);
		Node n = nodes.get(index);
		double patternX = n.getX();
		double patternY = n.getY();

		// Find index of the BMU
		int indexBMU = findBMU(patternX, patternY);

		//Update all the weights
		for(int i = 0; i < numNeurons; i++){
			weight[i][0] += (alpha * Phi(i,indexBMU, theta) * (patternX - weight[i][0]));
			weight[i][1] += (alpha * Phi(i,indexBMU, theta) * (patternY - weight[i][1]));
		}

		//Updates alpha and theta (Monoton decay)
		updateParameters();
	}

	public void setup() throws FileNotFoundException, IOException{		
		FileReader fr = new FileReader();
		fr.readNodes();
		nodes = fr.getNodes();
		numNodes = fr.getNumberOfNodes();
		numNeurons = numNodes * 2;
		neuron = new double[numNeurons][2];         
		weight = new double[numNeurons][2];           
		double omega = 0.0;

		
		for(int i = 0; i < numNeurons; i++){
			
			// Initialized nuerons form a closed circle
			// S = C + R * cos(2 * pi * i / M)
			double center = 1.0;
			double radius = 1.0;
			neuron[i][0] = center + radius * Math.cos(omega);
			neuron[i][1] = center + radius * Math.sin(omega);
			omega += Math.PI * 2.0 / (double)(numNeurons);
			weight[i][0] = 1000*Math.random();
			weight[i][1] = 1000*Math.random();
		}
	}

	
	private int findBMU(double location1, double location2){
		double minimumDistance = 100000000.0; // A high initial value which will be overwritten
		int minimumIndex = -1;
		for(int i = 0; i < numNeurons; i++){
			double distance = (Math.pow((location1 - weight[i][0]), 2)) + (Math.pow((location2 - weight[i][1]), 2));
			if(distance < minimumDistance){
				minimumDistance = distance;
				minimumIndex = i;
			}
		}
		return minimumIndex;
	}
	
	// Calculates the distance between neurons
	private double Phi(int x, int y, double theta){
		double result = Math.exp(-1.0 * (getDistance(x, y) * getDistance(x, y)) / (2.0 * Math.pow(theta, 2)));
		return result;
	}

	private double getDistance(int index, int index2){
		double xDiff = neuron[index][0] - neuron[index2][0];
		double yDiff = neuron[index][1] - neuron[index2][1];
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	private void updateParameters() {
		alpha *= DECAY;
		theta *= DECAY;		
	}

	public double[][] getWeight() {
		return weight;
	}
}