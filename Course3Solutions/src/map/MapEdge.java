/**
 * 
 */
package map;

import java.util.LinkedList;
import java.util.List;

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
	
	/** The points that allow the road segment to have non-straight geometry 
	 * These points DO NOT include the start and end points.  They are 
	 * optional.  If they are missing, the road is simply a straight line 
	 * between start and end.*/
	private List<GeographicPoint> roadPoints;
	
	/** Create a new MapEdge object
	 * 
	 * @param roadName
	 * @param n1  The point at one end of the segment
	 * @param n2  The point at the other end of the segment
	 * 
	 */
	MapEdge(String roadName, MapNode n1, MapNode n2) 
	{
		this(roadName, null, new LinkedList<GeographicPoint>(), n1, n2);
	}
	
	MapEdge(String roadName, String roadType, MapNode n1, MapNode n2) 
	{
		this(roadName, roadType, new LinkedList<GeographicPoint>(), n1, n2);
	}
	
	MapEdge(String roadName, String roadType, List<GeographicPoint> ptsOnEdge,
			MapNode n1, MapNode n2) 
	{
		this.roadName = roadName;
		point1 = n1;
		point2 = n2;
		roadPoints = new LinkedList<GeographicPoint>(ptsOnEdge);
		this.roadType = roadType;
	}
	
	GeographicPoint getPoint1()
	{
		return point1.getLocation();
	}
	
	GeographicPoint getPoint2()
	{
		return point2.getLocation();
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
	
	MapNode getOtherNode(MapNode node)
	{
		if (node.equals(point1)) 
			return point2;
		else if (node.equals(point2))
			return point1;
		throw new IllegalArgumentException("Looking for " +
			"a point that is not in the edge");
	}
	
	/** Return the list of road points, without the intersections */
	List<GeographicPoint> getMinorPoints()
	{
		return roadPoints;
	}
	public String toString()
	{
		String toReturn = "[EDGE between ";
		toReturn += "\n\t" + point1.getLocation();
		toReturn += "\n\t" + point2.getLocation();
		toReturn += "\nRoad name: " + roadName + " Road type: " + roadType;
		toReturn += "\nGeometry: [";
		for (GeographicPoint p : roadPoints)
		{
			toReturn += "(" + p + ") ";
		}
		toReturn += "]\n";
		
		return toReturn;
	}

}
