package TSP;


import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import TSP.*;
import acm.graphics.*;
import acm.program.*;

public class drawPaths extends GraphicsProgram {
	private static final int DOTSIZE = 4;
	private static final int MARGIN = 20;
	private int[] shortestPath;
	private static final int EPOCHS = 1000;
	private Map<Integer, GLine> lineGraphic = new HashMap<Integer, GLine>(); 
	private int bruteForceDistance = 0;


	public void run(){
		drawBruteForce();
		try {
			addAllNodes();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			drawKohonen();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void drawBruteForce() {
		BruteForce bf = new BruteForce();

		try {
			bruteForceDistance = bf.findSolution();
			if(bruteForceDistance>0){
				
				int[] temp = bf.getShortestPath();
				shortestPath = new int[temp.length];
				for(int i=0; i<temp.length; i++){
					shortestPath[i] = temp[i];
				}

				addAllNodes();
				drawLines();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void drawKohonen() throws FileNotFoundException, IOException{
		int tempx1;
		int tempy1;
		int tempx2;
		int tempy2;
		int totalDistance = 0;
		ElasticNet en = new ElasticNet();
		en.initialize();
		for(int j=0; j<EPOCHS; j++){
			totalDistance=0;
			en.algorithm();
			pause(50-j/20);
			//		en.findKohonenSolution();
			double[][] weights = en.getWeight();
//			System.out.println(weights.length);

			for(int i=0; i<weights.length-1;i++){
//				System.out.println(weights[i][0]);
				tempx1 = (int) weights[i][0];
				tempy1 = (int) weights[i][1];
				tempx2 = (int) weights[i+1][0];
				tempy2 = (int) weights[i+1][1];
				totalDistance += (int) Math.sqrt( (tempx1- tempx2)*(tempx1- tempx2) +(tempy1- tempy2)*(tempy1- tempy2));
				drawMapLine(i,tempx1, tempy1, tempx2, tempy2);
			}
			tempx1 = (int) weights[0][0];
			tempy1 = (int) weights[0][1];
			tempx2 = (int) weights[weights.length-1][0];
			tempy2 = (int) weights[weights.length-1][1];
			totalDistance += (int) Math.sqrt( (tempx1- tempx2)*(tempx1- tempx2) +(tempy1- tempy2)*(tempy1- tempy2));
			drawMapLine(weights.length-1,tempx1, tempy1, tempx2, tempy2);
			System.out.println("Iteration " + (j+1) +" out of " +EPOCHS);
			System.out.println("Total distance is "+totalDistance);
		}
		System.out.println("Brute Force Solution is "+bruteForceDistance);
		System.out.println("SOM Solution is " +totalDistance);
		System.out.println("Error is " + (double) 100*(totalDistance-bruteForceDistance)/bruteForceDistance );

	}

	private void drawMapLine(int id, int tempx1, int tempy1, int tempx2, int tempy2) {
		GLine prevLine = lineGraphic.get(id);
		if(prevLine != null)
			remove(prevLine);
		GLine line = new GLine(tempx1/2+MARGIN,tempy1/2+MARGIN,tempx2/2+MARGIN,tempy2/2+MARGIN);
		line.setColor(Color.BLUE);
		add(line);		
		lineGraphic.put(id, line);

	}

	private void addAllNodes() throws FileNotFoundException, IOException{
		FileReader fr = new FileReader();
		fr.readNodes();
		ArrayList<Node> nodes = fr.getNodes();
		for(Node n:nodes)
			addNode(n);
	}

	private void addNode(Node n){
		GOval point = new GOval(n.getX()/2+MARGIN,n.getY()/2+MARGIN,DOTSIZE,DOTSIZE);
		point.setFilled(true);
		point.setFillColor(Color.RED);
		add(point);
		GLabel label = new GLabel(String.valueOf(n.getId()),n.getX()/2+MARGIN, n.getY()/2-5+MARGIN);
		add(label);
	}

	private void drawLines() throws FileNotFoundException, IOException{
		FileReader fr = new FileReader();
		fr.readNodes();
		ArrayList<Node> nodes = fr.getNodes();
		for(int i=0; i<shortestPath.length-1;i++){
			drawLine(nodes.get(shortestPath[i]-1),nodes.get(shortestPath[i+1]-1));
		}
		drawLine(nodes.get(shortestPath[shortestPath.length-1]-1),nodes.get(shortestPath[0]-1));
	}

	private void drawLine(Node n1, Node n2){
		GLine line = new GLine(n1.getX()/2+MARGIN,n1.getY()/2+MARGIN,n2.getX()/2+MARGIN,n2.getY()/2+MARGIN);
		line.setColor(Color.BLACK);
		add(line);
	}

	public void init() {
		setSize(800, 600);
	}

}
