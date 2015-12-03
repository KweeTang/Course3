package roadgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import geography.GeographicPoint;
import geography.RoadSegment;
import util.GraphLoader;

public class MapTester {

	public static void main(String[] args) {
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		
		HashMap<GeographicPoint,HashSet<RoadSegment>> theRoads = 
				new HashMap<GeographicPoint,HashSet<RoadSegment>>();
		
		GraphLoader.loadMap("data/simpletest.map", theMap, theRoads);
			
		System.out.println("DONE.");
		
		System.out.println("Num nodes: " + theMap.getNumVertices());
		System.out.println("Num edges: " + theMap.getNumEdges());
		
		theMap.printEdges();
		theMap.printNodes();
        List<GeographicPoint> route = theMap.bfs(new GeographicPoint(1.0,1.0), new GeographicPoint(8.0,-1.0));
        System.out.println(route);
	}

}
