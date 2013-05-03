package TSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class BruteForce {
	int[] shortestPath;
	int numNodes;

	public int findSolution() throws FileNotFoundException, IOException{
		FileReader fileReader = new FileReader();
		fileReader.readNodes();
		int numNodes = fileReader.getNumberOfNodes();
		if(numNodes<=14){
			shortestPath = new int[numNodes];
			System.out.println("Number of nodes: " + numNodes);
			System.out.println(calculateMinDistance());
			return calculateMinDistance();
		}
		else{
			System.out.println("There are more than 14 nodes.");
			System.out.println("The problem is far too complex for a salesman.");
			return -1;
		}
	}


	public int calculateMinDistance() throws FileNotFoundException, IOException{
		int sum = 0;
		int min = 10000000;
		int[] currentPermutation= null;
		FileReader fileReader = new FileReader();
		fileReader.readNodes();
		numNodes = fileReader.getNumberOfNodes();
		PermutationGenerator pg = new PermutationGenerator(fileReader.getNumberOfNodes(),1);
		ArrayList<Node> nodes = fileReader.getNodes();
		while (pg.hasMore()) {
			currentPermutation =  pg.getNext();
			for (int i = 0; i < currentPermutation.length-1; i++) {
				double add = calculateDistance(nodes.get(currentPermutation[i]-1), nodes.get(currentPermutation[i+1]-1));
				sum += add;
			}

			sum += calculateDistance(nodes.get(currentPermutation[0]-1), nodes.get(currentPermutation[currentPermutation.length-1]-1));
			if(sum<min){
				min = sum;
				System.out.println("Current minimum is "+min);
				System.out.println("Node sequence of this min is: ");
				for(int i=0; i<numNodes; i++){
					shortestPath[i] = currentPermutation[i];
					System.out.print(currentPermutation[i]+ " ");
				}
				System.out.println();
			} 
			sum = 0;
		}//end while
		return min;
	}

	public int[] getShortestPath() {
		return shortestPath;
	}

	public double calculateDistance (Node n1, Node n2){
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		double sqDistance = xDiff*xDiff + yDiff*yDiff;
		double distance = Math.sqrt(sqDistance);
		return distance;
	}
	
	// For testing purposes
	public static void main(String args[]) throws FileNotFoundException, IOException{
		BruteForce bf = new BruteForce();
		System.out.println("Brute force solution is "+ bf.findSolution());
	}
}
