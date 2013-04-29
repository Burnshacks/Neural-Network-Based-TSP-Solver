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

		calculateH(sigma);

		return;
	}

	// Calculates the distance neurons
	private void calculateH(double theta){
		for(int i = 0; i < numNeurons; i++)
		{
			h[i][i] = 1.0;
			for(int j = i + 1; j < numNeurons; j++)
			{
				h[i][j] = Math.exp(-1.0 * (getDistance(i, j) * getDistance(i, j)) / (Math.pow(theta, 2)));
				h[j][i] = h[i][j];
			}
		}
	}

	private double getDistance(int index, int index2)
	{
		double dx = neuronXY[index][0] - neuronXY[index2][0];
		double dy = neuronXY[index][1] - neuronXY[index2][1];
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
		
		int index = 0;
		int indexBMU = 0;
		double patternX = 0.0;
		double patternY = 0.0;

		// Pick a Random Node
		index = (int)(Math.random() * numNodes);
		patternX = node[index][0];
		patternY = node[index][1];

		// Find index of the BMU
		indexBMU = findBMU(patternX, patternY);

		//Update the all neurons with respect to their distances to BMU
		for(int i = 0; i < numNeurons; i++){
			for(int)
			
			
			weight[i][0] += (alpha * h[i][indexBMU] * (patternX - weight[i][0]));
			weight[i][1] += (alpha * h[i][indexBMU] * (patternY - weight[i][1]));
		}

		//Updates alpha and sigma (Monoton decay)
		updateParameters();
		
		calculateH(sigma);


	}


	private void updateParameters() {
		alpha *= DECAY;
		sigma *= DECAY;		
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