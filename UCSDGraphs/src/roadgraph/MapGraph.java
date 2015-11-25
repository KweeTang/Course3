/**
 * 
 */
package roadgraph;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import geography.GeographicPoint;
import geography.RoadSegment;
import util.MapLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here
	
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method
		return 0;
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method
		return 0;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the node is already in the graph, this method does not change 
	 * the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (e.g. the node
	 * was already in the graph).
	 */
	public boolean addNode(GeographicPoint location)
	{
		// TODO: Implement this method
		return false;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending poing of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method.
		
	}
	

//	/** Returns the nodes in terms of their geographic locations */
//	public Collection<GeographicPoint> getNodes() {
//		return pointNodeMap.keySet();
//	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal)
	{
		// TODO: Implement this method
		return null;
	}
	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		HashMap<GeographicPoint,HashSet<RoadSegment>> theRoads = 
				new HashMap<GeographicPoint,HashSet<RoadSegment>>();
		MapLoader.loadMap("data/simpletest.map", theMap, theRoads);
		System.out.println("DONE.");
		
		// You can use this for testing.  Feel free to change the name
		// of the file to the test.map when you first start.
		
	}
	
}
