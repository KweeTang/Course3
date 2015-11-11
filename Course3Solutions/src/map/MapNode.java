/**
 * A class to represent a node in the map
 */
package map;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Christine
 *
 */
public class MapNode 
{
	/** The list of edges out of this node */
	private HashSet<MapEdge> edges;
	
	/** The list of streets that this node is part of */
	private HashSet<String> streets;
	
	/** the latitude and longitude of this node */
	private GeographicPoint location;
	
	public MapNode(GeographicPoint loc)
	{
		location = loc;
		edges = new HashSet<MapEdge>();
		streets = new HashSet<String>();
	}
	
	public void addStreet(String street)
	{
		//XXX might change to boolean
		streets.add(street);
	}
	
	public void addEdge(MapEdge edge)
	{
		//XXX Again, might want to change to boolean
		edges.add(edge);
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 * XXX: Maybe change this?
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof MapNode) || (o == null)) {
			return false;
		}
		MapNode node = (MapNode)o;
		return node.location.equals(this.location);
	}
	
	/** Because we compare nodes using their location, we also 
	 * use their location for HashCode
	 * @return The HashCode for this node, which is the HashCode for the 
	 * underlying point
	 */
	public int HashCode()
	{
		return location.hashCode();
	}
	
	
}
