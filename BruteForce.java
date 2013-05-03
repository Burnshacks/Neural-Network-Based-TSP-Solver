package TSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class BruteForce {
	int[] shortestPath;
	int numNodes;

	public static void main(String args[]) throws FileNotFoundException, IOException{
		BruteForce bf = new BruteForce();
		System.out.println("Brute force solution is "+ bf.findSolution());
	}

	public int findSolution() throws FileNotFoundException, IOException{
		FileReader fileReader = new FileReader();
		fileReader.readNodes();
		int numNodes = fileReader.getNumberOfNodes();
		if(numNodes<=14){
			shortestPath = new int[numNodes];
			System.out.println(numNodes);
			ArrayList<Node> nodes = fileReader.getNodes();
			for(int i=0; i<numNodes-1; i++){
				System.out.println(calculateDistance(nodes.get(i),nodes.get(i+1)));
			}
			System.out.println(calculateDistance(nodes.get(numNodes-1),nodes.get(0)));
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
		//		System.out.println("Number of nodes is: "+ fileReader.getNumberOfNodes());
		numNodes = fileReader.getNumberOfNodes();
		PermutationGenerator pg = new PermutationGenerator(fileReader.getNumberOfNodes(),1);
		ArrayList<Node> nodes = fileReader.getNodes();
		while (pg.hasMore()) {
			currentPermutation =  pg.getNext();

			//						Printing the array
//			for(int i=0; i<currentPermutation.length; i++)
//				System.out.print(currentPermutation[i] +" ");
//			System.out.println();

			//			System.out.println("Permutation");

			for (int i = 0; i < currentPermutation.length-1; i++) {
				double add = calculateDistance(nodes.get(currentPermutation[i]-1), nodes.get(currentPermutation[i+1]-1));
				sum += add;
				//				System.out.println("Distance between: "+ currentPermutation[i]+ " and "+ currentPermutation[i+1]+" "+ add); 
			}

			sum += calculateDistance(nodes.get(currentPermutation[0]-1), nodes.get(currentPermutation[currentPermutation.length-1]-1));
//						System.out.println("Sum is "+sum);
			if(sum<min){
				min = sum;
				System.out.println("Min is "+min);
				//				System.out.println("Number of nodes is "+ numNodes);
				for(int i=0; i<numNodes; i++){
					shortestPath[i] = currentPermutation[i];
					System.out.print(currentPermutation[i]+ " ");
					System.out.println();
				}
			} 
			sum = 0;
		}//end while
		return min;
	}

	public int[] getShortestPath() {
		return shortestPath;
	}

	public double calculateDistance (Node n1, Node n2){
//				System.out.println( n1.getId() +" " + n1.getX() + " " +n1.getY());
//				System.out.println(n2.getId() + " "+ n2.getX() + " " +n2.getY());
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		double sqDistance = xDiff*xDiff + yDiff*yDiff;
		//		System.out.println("Square of distance is "+sqDistance);
		double distance = Math.sqrt(sqDistance);
//				System.out.println(distance);
		return distance;
	}



}
