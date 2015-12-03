/**
 * 
 */
package roadgraph;

import java.util.HashMap;

/**
 * This class stores all the info about a road map.  It stores the 
 * geometric information in a MapGraph class, with road intersections
 * as vertices and roads as edges.  It stores detailed geometry and 
 * meta-information about roads in objects of type RoadSegment.
 * 
 * XXX: Not currently used.
 * 
 * Road segments are indexed by each of their end points
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 */
public class MapData {
	private HashMap theRoads;
	private MapGraph theMap;
	
	/** Create a new MapData object by loading the data from a file 
	 * 
	 * @param filename The file where the map data is located 
	 */
	public MapData(String filename)
	{
		
	}
}
