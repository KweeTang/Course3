/**
 * 
 */
package map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author UCSD MOOC development team
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint,MapNode> pointNodeMap;
	private HashMap<String,MapNode> streetNodeMap;
	private HashSet<MapEdge> edges;
	
	// Need to be able to look up nodes by lat/lon or by roads 
	// that they are part of.
	
	public MapGraph()
	{
		streetNodeMap = new HashMap<String,MapNode>();
		pointNodeMap = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/** Add a node corresponding to an intersection */
	public void addNode(List<String> roadNames, double latitude, double longitude)
	{
		GeographicPoint pt = new GeographicPoint(latitude, longitude);
		
		MapNode n = pointNodeMap.get(pt);
		if (n == null) {
			n = new MapNode(pt);
		}
		
		for (String name : roadNames) {
			n.addStreet(name);
			streetNodeMap.put(name, n);
		}
	}
	
	/** Add an edge representing a segment of a road. 
	 * @param roadName
	 */
	public void addEdge(String roadName, double lat1, double lon1, 
						double lat2, double lon2) 
	{
		// Find the two Nodes associated with this edge.
		// XXX An alternative is to have this method take the nodes themselves?
		// I think setting up the graph will be all internal
		// so maybe we don't need to expose this method?
		
		
	}
	
	// Add an edge when you already know the nodes involved in the edge
	private void addEdge(String roadName, MapNode n1, MapNode n2)
	{
		// We need to be careful to only add edges once because 
		// we have no check for duplicate edges
		MapEdge edge = new MapEdge(roadName, n1, n2);
		edges.add(edge);
	}
	
}
