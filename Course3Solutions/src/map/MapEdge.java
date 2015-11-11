/**
 * 
 */
package map;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Christine
 *
 */
public class MapEdge 
{
	/** The name of the road */
	private String roadName;
	
	/** The two endpoints of the edge */
	private MapNode point1;
	private MapNode point2;
	
	/** The points that allow the road segment to have non-straight geometry 
	 * These points DO NOT include the start and end points.  They are 
	 * optional.  If they are missing, the road is simply a straight line 
	 * between start and end.*/
	List<GeographicPoint> roadPoints;
	
	/** Create a new MapEdge object
	 * 
	 * @param roadName
	 * @param n1  The point at one end of the segment
	 * @param n2  The point at the other end of the segment
	 * 
	 */
	public MapEdge(String roadName, MapNode n1, MapNode n2) 
	{
		this.roadName = roadName;
		point1 = n1;
		point2 = n2;
		roadPoints = new LinkedList<GeographicPoint>();
	}
}
