package TSP;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileReader {

	int numNodes = 0;
	ArrayList<Node> nodes = new ArrayList<Node>();


	public void readNodes() throws FileNotFoundException, IOException{
		
		DrawSolutions ds = new DrawSolutions();
		String path = ds.getPath();
		
		 // Number of nodes
		String line;
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		while((line = br.readLine()) != null){	
			numNodes++;
//			System.out.println(line);
			int x = getXCoordinate(line);
			int y = getYCoordinate(line);
			nodes.add(new Node(numNodes,x,y));
		}
		
	}
	
	
	public int getYCoordinate(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line);
		String xStr = tokenizer.nextToken();
		int x = Integer.parseInt(xStr);
//		System.out.println("Parsed X is:"+ x);
		return x;
	}


	public int getXCoordinate(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line);
		tokenizer.nextToken();
		String yStr = tokenizer.nextToken();
		int y = Integer.parseInt(yStr);
//		System.out.println("Parsed Y is:"+ y);
		return y;
	}
	
	public int getNumberOfNodes(){
		return this.numNodes;
	}

	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	

	// Tests this class
	public static void main(String args[]) throws FileNotFoundException, IOException{
		FileReader myFileReader = new FileReader();
		myFileReader.readNodes();
	}
	
}