package basicgraph;

import geography.GeographicPoint;
import util.GraphLoader;

public class GraphTester {

	public static void main(String[] args)
	{
		GraphAdjList listGraph = new GraphAdjList();
		GraphLoader.loadOneWayMap("data/ucsd.map", listGraph);
		System.out.println(listGraph.getNumVertices() + " " + listGraph.getNumEdges());
		System.out.println(listGraph.degreeSequence());
		
		GeographicPoint pt1 = new GeographicPoint(32.822, -117.23);
		GeographicPoint pt2 = new GeographicPoint(32.821, -117.18);
		
		GraphLoader.testLineInfo(pt1, pt2, "Main");
		
		
		/*
		GraphAdjMatrix matGraph = new GraphAdjMatrix();
		MapLoader.loadMap("data/ucsdtest.map", matGraph);
		System.out.println(matGraph.getNumVertices() + " " + matGraph.getNumEdges());
		System.out.println(matGraph.degreeSequence());
		*/
		
	}
	
}
