/**
 * 
 */
package roadgraph;

import java.util.LinkedList;
import java.util.List;

import geography.GeographicPoint;

/**
 * @author UCSD Intermediate Programming MOOC team
 *
 * An undirected edge in a map graph.
 */
class MapEdge 
{
	/** The name of the road */
	private String roadName;
	
	/** The type of the road */
	private String roadType;
	
	/** The two endpoints of the edge */
	private MapNode point1;
	private MapNode point2;
	
	
	/** The length of the road segment, in km */
	private double length;
	
	static final double DEFAULT_LENGTH = 0.01;
	
	
	/** Create a new MapEdge object
	 * 
	 * @param roadName
	 * @param n1  The point at one end of the segment
	 * @param n2  The point at the other end of the segment
	 * 
	 */
	MapEdge(String roadName, MapNode n1, MapNode n2) 
	{
		this(roadName, "", n1, n2, DEFAULT_LENGTH);
	}
	
	MapEdge(String roadName, String roadType, MapNode n1, MapNode n2) 
	{
		this(roadName, roadType, n1, n2, DEFAULT_LENGTH);
	}
	
	MapEdge(String roadName, String roadType,
			MapNode n1, MapNode n2, double length) 
	{
		this.roadName = roadName;
		point1 = n1;
		point2 = n2;
		this.roadType = roadType;
		this.length = length;
	}
	
	MapNode getPoint2Node() {
	   return point2;
	}
	
	GeographicPoint getPoint1()
	{
		return point1.getLocation();
	}
	
	GeographicPoint getPoint2()
	{
		return point2.getLocation();
	}
	
	double getLength()
	{
		return length;
	}
	
	MapNode getOtherPoint(MapNode point)
	{
		if (point.equals(point1)) {
			return point2;
		}
		else if (point.equals(point2)) {
			return point1;
		}
		throw new IllegalArgumentException(point + " is not an endpoint for this edge");
			
	}
	
	public String getRoadName()
	{
		return roadName;
	}
	
	MapNode getOtherNode(MapNode node)
	{
		if (node.equals(point1)) 
			return point2;
		else if (node.equals(point2))
			return point1;
		throw new IllegalArgumentException("Looking for " +
			"a point that is not in the edge");
	}
	

	public String toString()
	{
		String toReturn = "[EDGE between ";
		toReturn += "\n\t" + point1.getLocation();
		toReturn += "\n\t" + point2.getLocation();
		toReturn += "\nRoad name: " + roadName + " Road type: " + roadType +
				" Segment length: " + String.format("%.3g", length) + "km";
		
		return toReturn;
	}

}
