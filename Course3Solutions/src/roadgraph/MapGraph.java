/**
 * 
 */
package roadgraph;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import geography.GeographicPoint;
import geography.RoadSegment;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint,MapNode> pointNodeMap;
	private HashSet<MapEdge> edges;
	
	// Need to be able to look up nodes by lat/lon or by roads 
	// that they are part of.
	
	/** Create a new empty MapGraph 
	 * 
	 */
	public MapGraph()
	{
		pointNodeMap = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return pointNodeMap.values().size();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		return edges.size();
	}
	
	// DEBUGGING.  NOT REQUIRED OF LEARNERS
	public void printNodes()
	{
		System.out.println("****PRINTING NODES ********");
		System.out.println("There are " + getNumVertices() + " Nodes: \n");
		for (GeographicPoint pt : pointNodeMap.keySet()) 
		{
			MapNode n = pointNodeMap.get(pt);
			System.out.println(n);
		}
	}

	// DEBUGGING.  NOT REQUIRED OF LEARNERS
	public void printEdges()
	{
		System.out.println("******PRINTING EDGES******");
		System.out.println("There are " + getNumEdges() + " Edges:\n");
		for (MapEdge e : edges) 
		{
			System.out.println(e);
		}
		
	}
	
	/** Add a node corresponding to an intersection 
	 * 
	 * @param latitude The latitude of the location
	 * @param longitude The longitude of the location
	 * */
	public void addNode(double latitude, double longitude)
	{
		GeographicPoint pt = new GeographicPoint(latitude, longitude);
		this.addNode(pt);
	}
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * 
	 * @param location  The location of the intersection
	 */
	public void addNode(GeographicPoint location)
	{
		MapNode n = pointNodeMap.get(location);
		if (n == null) {
			n = new MapNode(location);
			pointNodeMap.put(location, n);
		}
		else {
			System.out.println("Warning: Node at location " + location +
					" already exists in the graph.");
		}
		
	}
	
	/** Add an edge representing a segment of a road.
	 * Precondition: The corresponding Nodes must have already been 
	 *     added to the graph.
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 */
	public void addEdge(double lat1, double lon1, 
						double lat2, double lon2, String roadName, String roadType) 
	{
		// Find the two Nodes associated with this edge.
		// XXX An alternative is to have this method take the nodes themselves?
		// I think setting up the graph will be all internal
		// so maybe we don't need to expose this method?
		GeographicPoint pt1 = new GeographicPoint(lat1, lon1);
		GeographicPoint pt2 = new GeographicPoint(lat2, lon2);
		
		MapNode n1 = pointNodeMap.get(pt1);
		MapNode n2 = pointNodeMap.get(pt2);

		// XXX Should error check and throw exception here if the points 
		// aren't already in the graph.
		
		addEdge(n1, n2, roadName, roadType, MapEdge.DEFAULT_LENGTH);
		
	}
	
	public void addEdge(GeographicPoint pt1, GeographicPoint pt2, String roadName,
			String roadType) {
		
		MapNode n1 = pointNodeMap.get(pt1);
		MapNode n2 = pointNodeMap.get(pt2);

		// XXX Should error check and throw exception here if the points 
		// aren't already in the graph.
		
		addEdge(n1, n2, roadName, roadType, MapEdge.DEFAULT_LENGTH);
	}
	
	public void addEdge(GeographicPoint pt1, GeographicPoint pt2, String roadName,
			String roadType, double length) {
		MapNode n1 = pointNodeMap.get(pt1);
		MapNode n2 = pointNodeMap.get(pt2);

		// XXX Should error check and throw exception here if the points 
		// aren't already in the graph.
		
		addEdge(n1, n2, roadName, roadType, length);
	}
	
	public boolean isNode(GeographicPoint point)
	{
		return pointNodeMap.containsKey(point);
	}
	
	//XXX will probably need a similar isNode method for the intersection

	// Add an edge when you already know the nodes involved in the edge
	private void addEdge(MapNode n1, MapNode n2, String roadName, String roadType,
			double length)
	{

		MapEdge edge = new MapEdge(roadName, roadType, n1, n2, length);
		edges.add(edge);
		n1.addEdge(edge);
	}


	/** Returns the nodes in terms of their geographic locations */
	public Collection<GeographicPoint> getNodes() {
		return pointNodeMap.keySet();
	}
	
	private Set<MapNode> getNeighbors(MapNode node)
	{
		return node.getNeighbors();
	}
	
	//XXX: Needs refactor and some testing.
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal)
	{
		// Set up
		if (start == null || goal == null) 
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		toExplore.add(startNode);
		MapNode next = null;
		while (!toExplore.isEmpty()) {
			next = toExplore.remove();
			if (next.equals(endNode)) break;
			Set<MapNode> neighbors = getNeighbors(next);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, next);
					toExplore.add(neighbor);
				}
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path
		List<GeographicPoint> path = 
				reconstructPath(parentMap, startNode, endNode);
		
		return path;
	}

	
	
	private List<GeographicPoint> 
	reconstructPath(HashMap<MapNode,MapNode> parentMap, 
					MapNode start, MapNode goal)
	{
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		// Leo - bug had us starting with start and ending at goal
		MapNode current = goal;
		
		while (!current.equals(start)) {
			path.addFirst(current.getLocation());
			current = parentMap.get(current);
		}
		// Leo - need to add the start node
		path.addFirst(start.getLocation());
		return path;
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal)
	{
		// Set up
		if (start == null || goal == null) 
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		// initialize distance for all nodes
		for (MapNode n : pointNodeMap.values()) {
			n.setDistance(Double.POSITIVE_INFINITY);
		}
		startNode.setDistance(0);
		
		toExplore.add(startNode);
		MapNode next = null;
		while (!toExplore.isEmpty()) {
			next = toExplore.remove();
			System.out.println("DIJKSTRA visiting" + next);
			if (next.equals(endNode)) break;
			if(!visited.contains(next)) {
				visited.add(next);
				Set<MapEdge> edges = next.getEdges();
				for (MapEdge edge : edges) {
					MapNode neighbor = edge.getPoint2Node();
					if (!visited.contains(neighbor)) {
						
						double currDist = edge.getLength()+next.getDistance();
						if(currDist < neighbor.getDistance()){
							// debug 
							// System.out.println("Distance: " + currDist + "Node\n"+neighbor);
							parentMap.put(neighbor, next);
							neighbor.setDistance(currDist);
							toExplore.add(neighbor);
						}
					}
				}
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path
		List<GeographicPoint> path = 
				reconstructPath(parentMap, startNode, endNode);
		
		return path;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal)
	{
		// Set up
		if (start == null || goal == null) 
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		// initialize distance for all nodes
		for (MapNode n : pointNodeMap.values()) {
			n.setDistance(Double.POSITIVE_INFINITY);
			n.setActualDistance(Double.POSITIVE_INFINITY);
		}
		startNode.setDistance(0);
		startNode.setActualDistance(0);
		
		toExplore.add(startNode);
		MapNode next = null;
		while (!toExplore.isEmpty()) {
			next = toExplore.remove();
			System.out.println("A* visiting" + next);
			if (next.equals(endNode)) break;
			if(!visited.contains(next)) {
				visited.add(next);
				Set<MapEdge> edges = next.getEdges();
				for (MapEdge edge : edges) {
					MapNode neighbor = edge.getPoint2Node();
					if (!visited.contains(neighbor)) {
						
						double currDist = edge.getLength()+next.getActualDistance();
						// core of A* is just to add to currDist the cost of getting to
						// the destination
						double predDist = currDist+ (neighbor.getLocation()).distance(endNode.getLocation()); 
						if(predDist < neighbor.getDistance()){
							// debug
							//System.out.println("Pred Distance: " + predDist + "Node\n"+neighbor);
							parentMap.put(neighbor, next);
							neighbor.setActualDistance(currDist);
							neighbor.setDistance(predDist);
							toExplore.add(neighbor);
						}
					}
				}
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path
		List<GeographicPoint> path = 
				reconstructPath(parentMap, startNode, endNode);
		
		return path;
	}
	
	
	public void printEdgePointsToFile(String filename)
	{
	
		try {
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
			for (MapEdge e : edges) {
				writer.println(e.getPoint1() + " " + e.getPoint2());
			}	
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			System.out.println("Exception opening file " + e);
		}
	
	}
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		HashMap<GeographicPoint,HashSet<RoadSegment>> theRoads = 
				new HashMap<GeographicPoint,HashSet<RoadSegment>>();
		//MapLoader.loadMap("data/ucsdtest2.map", theMap, theRoads);
		
		GraphLoader.loadOneWayMap("data/testFromAdam.map", theMap, theRoads);
		System.out.println("DONE.");
		
		System.out.println("Num nodes: " + theMap.getNumVertices());
		System.out.println("Num edges: " + theMap.getNumEdges());
		//theMap.printEdgePointsToFile("data/ucsd.intersections.map");
		//theMap.printNodes();
		//theMap.printEdges();
		
		// Print the road segments
		//System.out.println("Road segments: ");
		//for (GeographicPoint p : theRoads.keySet()) {
			//System.out.println("Road segment with end a " + p);
		//	HashSet<RoadSegment> segments = theRoads.get(p);
		//	for (RoadSegment seg : segments) {
		//		System.out.println("\t"+ seg);
		//	}
		
		//}
		
	}
	
}

