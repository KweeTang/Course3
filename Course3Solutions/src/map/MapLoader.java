/**
 * 
 */
package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Christine
 *
 * A utility class that reads a file into a MapGraph
 */
public class MapLoader 
{
	/** Read in a file specifying a map.
	 * The file contains data as follows:
	 * lat1 lon1 lat2 lon2 roadName
	 * This method will collapse the points so that only intersections 
	 * are represnted as nodes in the graph.
	 * @param filename
	 * @param map
	 */
	public static void loadMap(String filename, MapGraph map)
	{
		BufferedReader reader = null;
        HashMap<GeographicPoint,List<LineInfo>> pointMap = 
        		new HashMap<GeographicPoint,List<LineInfo>>();
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
            	System.out.println("Parsing line " + nextLine);
            	LineInfo line = splitInputString(nextLine);
            	//System.out.println("Found: " + line.point1 + " " + line.point2 +
            	//		" " + line.roadName + " " + line.roadType);
            	addToPointMap(pointMap, line, line.point1);
                addToPointMap(pointMap, line, line.point2);
                System.out.println(pointMap.size());
            }
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
		
		// Now find the intersections.  These will be entries in the point map
		// with more than one road associated with them
		for (GeographicPoint pt : pointMap.keySet()) {
			System.out.println("Considering point " + pt);
			List<LineInfo> roadsOut = pointMap.get(pt);
			boolean isNode = true;
			if (roadsOut.size() == 2) {
				LineInfo road1 = roadsOut.get(0);
				LineInfo road2 = roadsOut.get(1);
				if (road1.sameRoad(road2)) {
					isNode = false;
				}
			}
			if (isNode) {
				// Get the road names that feed into this intersection
				HashSet<String> names = new HashSet<String>();
				for (LineInfo info : roadsOut) {
					names.add(info.roadName);
				}
				map.addNode(names,  pt);
			}
		}
		
		
		// Now we need to add the edges
		// This is the tricky part
		Collection<GeographicPoint> nodes = map.getNodes();
		for (GeographicPoint pt : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			List<LineInfo> infoList = pointMap.get(pt);
			HashSet<LineInfo> used = new HashSet<LineInfo>();
			for (LineInfo info : infoList) {
				if (!used.contains(info)) {
					List<GeographicPoint> pointsOnEdge = 
							findPointsOnEdge(pointMap, info, pt, map, used);
					GeographicPoint end = pointsOnEdge.remove(pointsOnEdge.size()-1);
					map.addEdge(pt, end, pointsOnEdge, info.roadName, info.roadType);
				}	
			}
		}
		
	}
	
	private static List<GeographicPoint>
	findPointsOnEdge(HashMap<GeographicPoint,List<LineInfo>> pointMap,
		LineInfo info, GeographicPoint pt, MapGraph map, HashSet<LineInfo> used) 
	{
		used.add(info);
		List<GeographicPoint> toReturn = new LinkedList<GeographicPoint>();
		GeographicPoint end = info.getOtherPoint(pt);
		List<LineInfo> nextLines = pointMap.get(end);
		while (!map.isNode(end)) {
			toReturn.add(end);
			if (nextLines.size() != 2) {
				System.out.println("Something went wrong building edges");
			}
			LineInfo nextInfo = nextLines.get(0);
			if (used.contains(nextInfo)) {
				nextInfo = nextLines.get(1);
			}
			used.add(nextInfo);
			end = nextInfo.getOtherPoint(end);
			nextLines = pointMap.get(end);
		}
		toReturn.add(end);
		
		return toReturn;
	}
	
	private static void addToPointMap(HashMap<GeographicPoint,List<LineInfo>> pointMap,
			LineInfo line, GeographicPoint pt)
	{
		List<LineInfo> pointEntries = pointMap.get(pt);
        if (pointEntries == null) {
        	pointEntries = new LinkedList<LineInfo>();
        	pointMap.put(pt, pointEntries);
        }
        pointEntries.add(line);
	}
	
	private static LineInfo splitInputString(String input)
	{	
		
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");
		Matcher m = tokSplitter.matcher(input);
		
		while (m.find()) {
			if (m.group(1) != null) {
				tokens.add(m.group(1));	
			}
			else {
				tokens.add(m.group());
			}
		}

    	double lat1 = Double.parseDouble(tokens.get(0));
        double lon1 = Double.parseDouble(tokens.get(1));
        double lat2 = Double.parseDouble(tokens.get(2));
        double lon2 = Double.parseDouble(tokens.get(3));
        GeographicPoint p1 = new GeographicPoint(lat1, lon1);
        GeographicPoint p2 = new GeographicPoint(lat2, lon2);

        return new LineInfo(p1, p2, tokens.get(4), tokens.get(5));
		
	}
	
}

class LineInfo
{
	GeographicPoint point1;
	GeographicPoint point2;
	
	String roadName;
	String roadType;
	
	LineInfo(GeographicPoint p1, GeographicPoint p2, String roadName, String roadType) 
	{
		point1 = p1;
		point2 = p2;
		this.roadName = roadName;
		this.roadType = roadType;
	}
	
	public GeographicPoint getOtherPoint(GeographicPoint pt)
	{
		if (pt == null) throw new IllegalArgumentException();
		if (pt.equals(point1)) {
			return point2;
		}
		else if (pt.equals(point2)) {
			return point1;
		}
		else throw new IllegalArgumentException();
	}
	
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof LineInfo))
		{
			return false;
		}
		LineInfo info = (LineInfo)o;
		return (info.point1.equals(this.point1) && info.point2.equals(this.point2) ||
				info.point1.equals(this.point2) && info.point2.equals(this.point1)) &&
				info.roadType.equals(this.roadType) && info.roadName.equals(this.roadName);
				
	}
	public boolean sameRoad(LineInfo info)
	{
		return info.roadName.equals(this.roadName) && info.roadType.equals(this.roadType);
	}
	
}
