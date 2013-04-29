package TSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class BruteForce {
	int[] shortestPath;
	int numNodes;

	public int[] getShortestPath() {
		return shortestPath;
	}

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
			//			System.out.println(numNodes);
			//			ArrayList<Node> nodes = fileReader.getNodes();
			//			for(int i=0; i<numNodes-1; i++){
			//				System.out.println(calculateDistance(nodes.get(i),nodes.get(i+1)));
			//			}
			//			System.out.println(calculateDistance(nodes.get(numNodes-1),nodes.get(0)));

			//		System.out.println(calculateMinDistance());
			return calculateMinDistance();
		}
		else{
			System.out.println("There are more than 14 nodes.");
			System.out.println("The problem is far too complex for a salesman.");
			return -1;
		}
	}

	public double calculateDistance (Node n1, Node n2){
		int xDiff = n1.getX() - n2.getX();
		int yDiff = n1.getY() - n1.getY();
		double distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
		return distance;
	}

	public int calculateMinDistance() throws FileNotFoundException, IOException{
		int sum = 0;
		int min = 10000000;
		int max = 0;
		int[] temp = null;
		FileReader fileReader = new FileReader();
		fileReader.readNodes();
//		System.out.println("Number of nodes is: "+ fileReader.getNumberOfNodes());
		numNodes = fileReader.getNumberOfNodes();
		PermutationGenerator pg = new PermutationGenerator(fileReader.getNumberOfNodes(),1);
		ArrayList<Node> nodes = fileReader.getNodes();
		while (pg.hasMore()) {
			temp =  pg.getNext();

			//			Printing the array
//			for(int i=0; i<temp.length; i++)
//				System.out.print(temp[i] +" ");

//			System.out.println("Permutation");

			for (int i = 0; i < temp.length-1; i++) {
				sum += calculateDistance(nodes.get(temp[i]-1), nodes.get(temp[i+1]-1));
				//				System.out.println("Calculating the distance between: "+ i+ " and "
				//						+ (i+1)); 
			}

			sum += calculateDistance(nodes.get(0), nodes.get(temp.length-1));
//			System.out.println("Sum is "+sum);
			if(sum<min){
				min = sum;
				System.out.println("Min is "+min);
				//				System.out.println("Number of nodes is "+ numNodes);
				for(int i=0; i<numNodes; i++){
					shortestPath[i] = temp[i];
					System.out.print(temp[i]+ " ");
					System.out.println();
				}

			} else if(sum>max){
				max = sum;
				//				System.out.println("Max is "+max);
			}
			sum = 0;
		}//end while
		return min;
	}



}
