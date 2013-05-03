package TSP;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ElasticNet{

	private int numNodes = 0;
	private int numNeurons = numNodes * 2;
	private static final double DECAY = 0.995;
	private double neuronXY[][] = null;
	private double weight[][] = null;
	private double h[][];
	private double gridInterval = 0.0;
	private double theta = 0.8;
	private double alpha = 0.8;
	private ArrayList<Node> nodes = new ArrayList<Node>();

	public void setup() throws FileNotFoundException, IOException{		
		FileReader fr = new FileReader();
		fr.readNodes();
		nodes = fr.getNodes();
		numNodes = fr.getNumberOfNodes();
		numNeurons = numNodes * 2;

		neuronXY = new double[numNeurons][2];         
		weight = new double[numNeurons][2];           
		h = new double[numNeurons][numNeurons];


		for(int i = 0; i < numNeurons; i++){
//			neuronXY[i][0] = 0.5 + 0.5 * Math.cos(gridInterval);
//			neuronXY[i][1] = 0.5 + 0.5 * Math.sin(gridInterval);
//			gridInterval += Math.PI * 2.0 / (double)(numNeurons);
			
			neuronXY[i][0] = 1.0 * (1.0 + 1.0 * Math.cos(gridInterval));
			neuronXY[i][1] = 1.0 * (1.0 + 1.0 * Math.sin(gridInterval));
			gridInterval += Math.PI * 2.0 / (double)(numNeurons);

			//			int neuronPerLine = (int) Math.sqrt(numNeurons);
			//			double grid =  (double) 1.0/(neuronPerLine+1);
			//			neuronXY[i][0] = grid * ( (i % neuronPerLine) + 1);
			//			neuronXY[i][1] = grid * ( (i / neuronPerLine) + 1);
			//			System.out.println(neuronXY[i][0] + " " + neuronXY[i][1]);



			weight[i][0] = 1000*Math.random();
			weight[i][1] = 1000*Math.random();
		}

		calculateH();

		return;
	}

	// Calculates the distance between BMU and the neighboring neuron
	private void calculateH()
	{
		for(int i = 0; i < numNeurons; i++)
		{
			h[i][i] = 1.0;
			for(int j = i + 1; j < numNeurons; j++)
			{
				h[i][j] = Phi(i, j, theta);
				h[j][i] = h[i][j];
			}
		}
		return;
	}

	// Calculates the distance between neurons
	private double Phi(int x, int y, double theta){
		double result = Math.exp(-1.0 * (getDistance(x, y) * getDistance(x, y)) / (2.0 * Math.pow(theta, 2)));
		return result;
	}

	private double getDistance(int index, int index2){
		double xDiff = neuronXY[index][0] - neuronXY[index2][0];
		double yDiff = neuronXY[index][1] - neuronXY[index2][1];
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	private int findBMU(double location1, double location2){
		double minimumDistance = 10000000.0;
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

	public void algorithm(){

		int index = 0;
		int indexBMU = 0;
		double patternX = 0.0;
		double patternY = 0.0;

		// Pick a Random Node
		index = (int)(Math.random() * numNodes);
		Node n = nodes.get(index);
		patternX = n.getX();
		patternY = n.getY();

		// Find index of the BMU
		indexBMU = findBMU(patternX, patternY);

		//Update the BMU
		for(int i = 0; i < numNeurons; i++){
			weight[i][0] += (alpha * h[i][indexBMU] * (patternX - weight[i][0]));
			weight[i][1] += (alpha * h[i][indexBMU] * (patternY - weight[i][1]));
		}

		//Updates alpha and sigma (Monoton decay)
		updateParameters();

		calculateH();
	}

	private void updateParameters() {
		alpha *= DECAY;
		theta *= DECAY;		
	}

	public double[][] getWeight() {
		return weight;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ElasticNet en = new ElasticNet();
		en.findKohonenSolution();
	}

	public void findKohonenSolution() throws FileNotFoundException, IOException{
		setup();
		algorithm();
	}

}