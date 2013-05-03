package TSP;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ModifiedKohonen{

	private int numNodes = 0;
	private int numNeurons = numNodes * 2;
	private static final double DECAY = 0.995;
	private double node[][] = null;
	private double neuronXY[][] = null;
	private double weight[][] = null;
	private double h[][];
	private double gridInterval = 0.0;
	private double sigma = 0.5;
	private double alpha = 0.5;
	private double k = 0.5;
	private ArrayList<Node> nodes = new ArrayList<Node>();

	public void setup() throws FileNotFoundException, IOException{		
		FileReader fr = new FileReader();
		fr.readNodes();
		nodes = fr.getNodes();
		numNodes = fr.getNumberOfNodes();
		numNeurons = numNodes * 2;

		node = new double[numNodes][2];              
		neuronXY = new double[numNeurons][2];         
		weight = new double[numNeurons][2];           
		h = new double[numNeurons][numNeurons];


		for(int i=0; i<numNodes; i++){
			Node n = nodes.get(i);
			node[i][0] = n.getX();
			node[i][1] = n.getY();
		}

		for(int i = 0; i < numNeurons; i++)
		{
			neuronXY[i][0] = 0.5 + 0.5 * Math.cos(gridInterval);
			neuronXY[i][1] = 0.5 + 0.5 * Math.sin(gridInterval);
			gridInterval += Math.PI * 2.0 / (double)(numNeurons);
			weight[i][0] = 1000*Math.random();
			weight[i][1] = 1000*Math.random();
		}

//		calculateH(sigma);

		return;
	}

	// Calculates the distance neurons
//	private void calculateH(double theta){
//		for(int i = 0; i < numNeurons; i++)
//		{
//			h[i][i] = 1.0;
//			for(int j = i + 1; j < numNeurons; j++)
//			{
//				h[i][j] = Phi(i, j, theta);
//				h[j][i] = h[i][j];
//
//			}
//		}
//	}

	private double normalizedDistance(int i, int j, double theta){
		double numerator = Phi(i, j, theta);
		double denumerator = 0;

		for(int p = 0; p < numNeurons; p++){
			denumerator = Phi(i, p, theta);
		}

		return numerator / denumerator;
	}

	private double Phi(int x, int y, double theta){
		double result = Math.exp(-1.0 * (getDistance(x, y) * getDistance(x, y)) / (2 * Math.pow(theta, 2)));
		return result;
	}

	private double getDistance(int index, int index2){
		double dx = node[index][0] - neuronXY[index2][0];
		double dy = node[index][1] - neuronXY[index2][1];
		return Math.sqrt(dx * dx + dy * dy);
	}

	private int findBMU(double location1, double location2){
		double minimumDistance = 100000000.0;
		int minimumIndex = -1;
		for(int i = 0; i < numNeurons; i++)
		{
			double distance = (Math.pow((location1 - weight[i][0]), 2)) + (Math.pow((location2 - weight[i][1]), 2));
			if(distance < minimumDistance){
				minimumDistance = distance;
				minimumIndex = i;
			}
		}
		return minimumIndex;
	}

	public void algorithm(){

//		int index = 0;
//		int indexBMU = 0;
//		double patternX = 0.0;
//		double patternY = 0.0;

		// Pick a Random Node
//		index = (int)(Math.random() * numNodes);
//		patternX = node[index][0];
//		patternY = node[index][1];

		// Find index of the BMU
//		indexBMU = findBMU(patternX, patternY);

		//Update the all neurons with respect to their distances to BMU
		for(int j = 0; j < numNeurons; j++){
			double higherWeight;
			double lowerWeight;

			if(j <= numNeurons -2)
				higherWeight = weight[j+1][0];
			else
				higherWeight = weight[0][0];

			if(j >= 1)
				lowerWeight = weight[j-1][0];
			else
				lowerWeight = weight[numNeurons-1][0];

			for(int i = 0; i < numNodes; i++){
				
				weight[i][0] += (alpha * normalizedDistance(i, j, sigma) * (node[i][0] - weight[i][0])) + 
						k * (higherWeight - 2 * weight[i][0] + lowerWeight);
				
				weight[i][1] += (alpha * normalizedDistance(i, j, sigma) * (node[i][1] - weight[i][1])) +		
						k * (higherWeight - 2 * weight[i][1] + lowerWeight);
			}

		}
		//Updates alpha and sigma (Monoton decay)
		updateParameters();

//		calculateH(sigma);


	}


	private void updateParameters() {
		alpha *= DECAY;
		sigma *= DECAY;		
		k *= DECAY;
	}

	public double[][] getWeight() {
		return weight;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ModifiedKohonen en = new ModifiedKohonen();
		en.findKohonenSolution();
	}


	public void findKohonenSolution() throws FileNotFoundException, IOException{
		setup();
		algorithm();
	}

}