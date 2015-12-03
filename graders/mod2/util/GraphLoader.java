/**
 * 
 */
package util;

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

import basicgraph.Graph;
import geography.GeographicPoint;
import geography.RoadSegment;
import roadgraph.MapGraph;

/**
 * @author UCSD Intermediate Programming MOOC team
 *
 * A utility class that reads various kinds of files into different 
 * graph structures.
 */
public class GraphLoader 
{
	/**XXX WILL NOT BE IN STARTER CODE.
	 * 
	 *  Read in a file specifying a map.
	 *
	 * The file contains data as follows:
	 * lat1 lon1 lat2 lon2 roadName roadType
	 * This method will collapse the points so that only intersections 
	 * are represented as nodes in the graph.
	 * @param filename The file containing the road data, in the format 
	 *   described.
	 * @param map The graph to load the map into
	 * @param segments The collection of RoadSegments that define the 
	 *   shape of a road.  These segments are maintained separately from 
	 *   the graph as they are only used to display paths.
	 */
	public static void loadMap(String filename, MapGraph map, 
				HashMap<GeographicPoint,HashSet<RoadSegment>> segments)
	{
		
		Collection<GeographicPoint> nodes = new HashSet<GeographicPoint>();
        HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		buildPointMap(filename);
		
        
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		//System.out.println("Done finding intersections");
		for (GeographicPoint pt : intersections) {
			map.addNode(pt);
			nodes.add(pt);
		}
		
		addEdgesAndSegments(nodes, pointMap, map, segments);
		
	}
	
	

	/**
	 * XXX: Will be in starter code with the name loadRoadMap
	 * 
	 *  Read in a file specifying a map.
	 *
	 * The file contains data lines as follows:
	 * lat1 lon1 lat2 lon2 roadName roadType
	 * 
	 * where each line is a segment of a road
	 * These road segments are assumed to be ONE WAY.
	 * 
	 * This method will collapse the points so that only intersections 
	 * are represented as nodes in the graph.
	 * 
	 * @param filename The file containing the road data, in the format 
	 *   described.
	 * @param map The graph to load the map into.  The graph is
	 *   assumed to be directed.
	 * @param segments The collection of RoadSegments that define the 
	 *   shape of a road.  These segments are maintained separately from 
	 *   the graph as they are only used to display paths.
	 */
	public static void loadOneWayMap(String filename, roadgraph.MapGraph map)
	{
		loadOneWayMap(filename, map, null);
	}

	
	
	/**
	 * XXX: Will be in starter code with the name loadRoadMap
	 * 
	 *  Read in a file specifying a map.
	 *
	 * The file contains data lines as follows:
	 * lat1 lon1 lat2 lon2 roadName roadType
	 * 
	 * where each line is a segment of a road
	 * These road segments are assumed to be ONE WAY.
	 * 
	 * This method will collapse the points so that only intersections 
	 * are represented as nodes in the graph.
	 * 
	 * @param filename The file containing the road data, in the format 
	 *   described.
	 * @param map The graph to load the map into.  The graph is
	 *   assumed to be directed.
	 */
	public static void loadOneWayMap(String filename, roadgraph.MapGraph map,  
			HashMap<GeographicPoint,HashSet<RoadSegment>> segments)
	{
		Collection<GeographicPoint> nodes = new HashSet<GeographicPoint>();
        HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		buildPointMapOneWay(filename);
		
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		for (GeographicPoint pt : intersections) {
			map.addNode(pt);
			nodes.add(pt);
		}
		
		
		addEdgesAndSegments(nodes, pointMap, map, segments);
	}

	
	/**
	 * XXX: Will be in starter code with the name loadRoadMap
	 * 
	 *  Read in a file specifying a map.
	 *
	 * The file contains data lines as follows:
	 * lat1 lon1 lat2 lon2 roadName roadType
	 * 
	 * where each line is a segment of a road
	 * These road segments are assumed to be ONE WAY.
	 * 
	 * This method will collapse the points so that only intersections 
	 * are represented as nodes in the graph.
	 * 
	 * @param filename The file containing the road data, in the format 
	 *   described.
	 * @param theGraph The graph to load the map into.  The graph is
	 *   assumed to be directed.
	 */
	public static void loadOneWayMap(String filename, basicgraph.Graph theGraph)
	{
		HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		buildPointMapOneWay(filename);
		
		HashMap<Integer,GeographicPoint> vertexMap = 
				new HashMap<Integer,GeographicPoint>();
		HashMap<GeographicPoint,Integer> reverseMap = 
				new HashMap<GeographicPoint,Integer>();
		
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		//System.out.println("Done finding intersections");
		
		int index = 0;
		for (GeographicPoint pt : intersections) {
			theGraph.addVertex();
			vertexMap.put(index, pt);
			reverseMap.put(pt, index);
			//System.out.println(pt);
			index++;
		}
		
		// Now add the edges
		Collection<Integer> nodes = vertexMap.keySet();
		for (Integer nodeNum : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			GeographicPoint pt = vertexMap.get(nodeNum);
			List<LinkedList<LineInfo>> inAndOut = pointMap.get(pt);
			List<LineInfo> infoList = inAndOut.get(0);
			for (LineInfo info : infoList) {
				GeographicPoint end = findEndOfEdge(pointMap, info, theGraph, 
						reverseMap);
				//System.out.println("\tAdding edge from " + pt + " to " + end);
				Integer endNum = reverseMap.get(end);
				//System.out.println("\t" + nodeNum + "->" + endNum);
				theGraph.addEdge(nodeNum, endNum);
			}
		}
	}
	
	/** 
	 * XXX Will not be in the starter code.
	 * Load a file into a basicgraph Graph object.
	 * 
	 * The file contains data as follows:
	 * lat1 lon1 lat2 lon2 roadName roadType
	 * This method will collapse the points so that only intersections 
	 * are represented as nodes in the graph.
	 * 
	 * @param filename  The file containing the graph, in the format described.
	 * @param theGraph The graph to load.
	 */
	public static void loadMap(String filename, basicgraph.Graph theGraph)
	{
		HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		buildPointMap(filename);
		
		HashMap<Integer,GeographicPoint> vertexMap = 
				new HashMap<Integer,GeographicPoint>();
		HashMap<GeographicPoint,Integer> reverseMap = 
				new HashMap<GeographicPoint,Integer>();
		
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		//System.out.println("Done finding intersections");
		
		int index = 0;
		for (GeographicPoint pt : intersections) {
			theGraph.addVertex();
			vertexMap.put(index, pt);
			reverseMap.put(pt, index);
			//System.out.println(pt);
			index++;
		}
		
		// Now add the edges
		Collection<Integer> nodes = vertexMap.keySet();
		for (Integer nodeNum : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			GeographicPoint pt = vertexMap.get(nodeNum);
			//System.out.println("Finding edges out of " + nodeNum);
			List<LinkedList<LineInfo>> inAndOut = pointMap.get(pt);
			List<LineInfo> infoList = inAndOut.get(0);
			for (LineInfo info : infoList) {
				GeographicPoint end = findEndOfEdge(pointMap, info, theGraph, 
						reverseMap);
				Integer endNum = reverseMap.get(end);
				theGraph.addEdge(nodeNum, endNum);
			}
		}
	}		

	/**
	 * XXX WILL NOT BE IN THE STARTER CODE.
	 * @param pt1
	 * @param pt2
	 * @param roadName
	 */
	public static void testLineInfo(GeographicPoint pt1, 
			GeographicPoint pt2, String roadName)
	{
		LineInfo l1 = new LineInfo(pt1, pt2, roadName, "");
		LineInfo l2 = new LineInfo(pt1, pt2, roadName, "");
		LineInfo l3 = new LineInfo(new GeographicPoint(pt1.getX(), pt1.getY()),
				new GeographicPoint(pt2.getX(), pt2.getY()), roadName, "");
		System.out.println("l1 equals l2? " + l1.equals(l2));
		System.out.println("l1 equals l3? " + l1.equals(l3));
		System.out.println("l1 hashCode l2? " + l1.hashCode() + " " + l2.hashCode());
		System.out.println("l1 hashCode l3? " + l1.hashCode() + " " + l3.hashCode());
		
	}
	
	
	/**
	 * Loads a graph from a file.  The file is specified with each 
	 * line representing an edge.  Vertices are numbered from 
	 * 0..1-numVertices.
	 * 
	 * The first line of the file contains a single int which is the 
	 * number of vertices in the graph.
	 * e.g. 
	 * 5
	 * 1 3
	 * 3 2
	 * 3 5
	 * 5 4
	 * 
	 * @param filename The file containing the graph
	 * @param theGraph The graph to be loaded
	 */
	public static void loadGraph(String filename, basicgraph.Graph theGraph)
	{
		BufferedReader reader = null;
        try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            nextLine = reader.readLine();
            if (nextLine == null) {
            	reader.close();
            	throw new IOException("Graph file is empty!");
            }
            int numVertices = Integer.parseInt(nextLine);
            for (int i = 0; i < numVertices; i++) {
            	theGraph.addVertex();
            }
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
            	String[] verts = nextLine.split(" ");
            	int start = Integer.parseInt(verts[0]);
            	int end = Integer.parseInt(verts[1]);
            	theGraph.addEdge(start, end);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
	}
	
	// Once you have built the pointMap and added the Nodes, 
	// add the edges and build the road segments if the segments
	// map is not null.
	private static void addEdgesAndSegments(Collection<GeographicPoint> nodes, 
			HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap,
			MapGraph map, 
			HashMap<GeographicPoint,HashSet<RoadSegment>> segments)
	{
	
		// Now we need to add the edges
		// This is the tricky part
		for (GeographicPoint pt : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			List<LinkedList<LineInfo>> inAndOut = pointMap.get(pt);
			LinkedList<LineInfo> outgoing = inAndOut.get(0);
			for (LineInfo info : outgoing) {
				HashSet<GeographicPoint> used = new HashSet<GeographicPoint>();
				used.add(pt);
				
				List<GeographicPoint> pointsOnEdge = 
						findPointsOnEdge(pointMap, info, nodes);
				GeographicPoint end = pointsOnEdge.remove(pointsOnEdge.size()-1);
				double length = getRoadLength(pt, end, pointsOnEdge);
				map.addEdge(pt, end, info.roadName, info.roadType, length);

				// If the segments variable is not null, then we 
				// save the road geometry
				if (segments != null) {
					// Now create road Segments for each edge
					HashSet<RoadSegment> segs = segments.get(pt);
					if (segs == null) {
						segs = new HashSet<RoadSegment>();
						segments.put(pt,segs);
					}
					RoadSegment seg = new RoadSegment(pt, end, pointsOnEdge, 
							info.roadName, info.roadType, length);
					segs.add(seg);
					segs = segments.get(end);
					if (segs == null) {
						segs = new HashSet<RoadSegment>();
						segments.put(end,segs);
					}
					segs.add(seg);
				}
			}
		}
	}
			
	
	private static double getRoadLength(GeographicPoint start, GeographicPoint end,
			List<GeographicPoint> path)
	{
		double dist = 0.0;
		GeographicPoint curr = start;
		for (GeographicPoint next : path) {
			dist += curr.distance(next);
			curr = next;
		}
		dist += curr.distance(end);
		return dist;
	}
	
	private static List<GeographicPoint>
	findPointsOnEdge(HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap,
		LineInfo info, Collection<GeographicPoint> nodes) 
	{
		List<GeographicPoint> toReturn = new LinkedList<GeographicPoint>();
		GeographicPoint pt = info.point1;
		GeographicPoint end = info.point2;
		List<LinkedList<LineInfo>> nextInAndOut = pointMap.get(end);
		LinkedList<LineInfo> nextLines = nextInAndOut.get(0);
		while (!nodes.contains(end)) {
			toReturn.add(end);
			LineInfo nextInfo = nextLines.get(0);
			if (nextLines.size() == 2) {
				if (nextInfo.point2.equals(pt)) {
					nextInfo = nextLines.get(1);
				}
			}
			else if (nextLines.size() != 1) {
				System.out.println("Something went wrong building edges");
			}
			pt = end;
			end = nextInfo.point2;
			nextInAndOut = pointMap.get(end);
			nextLines = nextInAndOut.get(0);
		}
		toReturn.add(end);
		
		return toReturn;
	}

	private static GeographicPoint
	findEndOfEdge(HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap,
		LineInfo info, basicgraph.Graph graph, 
		HashMap<GeographicPoint, Integer> reverseMap) 
	{
		//System.out.println("Finding the end of edge " + info);
		
		GeographicPoint pt = info.point1;
		GeographicPoint end = info.point2;
		Integer endNum = reverseMap.get(end);
		while (endNum==null) {
			//System.out.println("Current point is " + pt);
			List<LinkedList<LineInfo>> inAndOut = pointMap.get(end);
			List<LineInfo> nextLines = inAndOut.get(0);
			LineInfo nextInfo = nextLines.get(0);
			if (nextLines.size() == 2) {
				if (nextInfo.point2.equals(pt)) {
					nextInfo = nextLines.get(1);
				}
			}
			else if (nextLines.size() != 1) {
				System.out.println("Something went wrong building edges");
			}
			pt = end;
			end = nextInfo.point2;
			endNum = reverseMap.get(end);
		}
		
		return end;
	}
	
	
	private static List<GeographicPoint> 
	findIntersections(HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap) {
		//System.out.println("Finding intersections");
		// Now find the intersections.  These are roads that do not have
		// Exactly 1 or 2 roads coming in and out, where the roads in
		// match the roads out.
		List<GeographicPoint> intersections = new LinkedList<GeographicPoint>();
		for (GeographicPoint pt : pointMap.keySet()) {
			//System.out.println("Considering point " + pt);
			List<LinkedList<LineInfo>> roadsInAndOut = pointMap.get(pt);
			LinkedList<LineInfo> roadsOut = roadsInAndOut.get(0);
			LinkedList<LineInfo> roadsIn = roadsInAndOut.get(1);
			
			boolean isNode = true;
			
			if (roadsIn.size() == 1 && roadsOut.size() == 1) {
				// If these are the reverse of each other, then this is
				// and intersection (dead end)
				if (!(roadsIn.get(0).point1.equals(roadsOut.get(0).point2) &&
						roadsIn.get(0).point2.equals(roadsOut.get(0).point1))
						&& roadsIn.get(0).roadName.equals(roadsOut.get(0).roadName)) {
					isNode = false;
				}
			}
			if (roadsIn.size() == 2 && roadsOut.size() == 2) {
				// If all the road segments have the same name, then 
				// this is not an intersection
				String name = roadsIn.get(0).roadName;
				boolean sameName = true;
				for (LineInfo info : roadsIn) {
					if (!info.roadName.equals(name)) {
						sameName = false;
					}
				}
				for (LineInfo info : roadsOut) {
					if (!info.roadName.equals(name)) {
						sameName = false;
					}
				}
				if (sameName) {
					isNode = false;
				}
			}
			if (isNode) {
				intersections.add(pt);
			}
		}
		//System.out.println("Returning intersections");
		return intersections;
	}
	
	private static HashMap<GeographicPoint, List<LinkedList<LineInfo>>>
	buildPointMapOneWay(String filename)
	{
		BufferedReader reader = null;
        HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		new HashMap<GeographicPoint,List<LinkedList<LineInfo>>>();
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
            	//System.out.println("Parsing line " + nextLine);
            	LineInfo line = splitInputString(nextLine);
            	//System.out.println("Found: " + line.point1 + " " + line.point2 +
            	//		" " + line.roadName + " " + line.roadType);
            	addToPointsMapOneWay(line, pointMap);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
		
		return pointMap;
	}
	
	private static void 
	addToPointsMapOneWay(LineInfo line,
						HashMap<GeographicPoint,List<LinkedList<LineInfo>>> map)
	{
		List<LinkedList<LineInfo>> pt1Infos = map.get(line.point1);
		if (pt1Infos == null) {
			pt1Infos = new ArrayList<LinkedList<LineInfo>>();
			pt1Infos.add(new LinkedList<LineInfo>());
			pt1Infos.add(new LinkedList<LineInfo>());
			map.put(line.point1, pt1Infos);
		}
		List<LineInfo> outgoing = pt1Infos.get(0);
		outgoing.add(line);
		/*
		System.out.println("Entry in pointMap for " + line.point1 + " is ");
		System.out.println("OUTGOING");
		for (LineInfo info : pt1Infos.get(0))
			System.out.println(info);
	
		
		System.out.println("INCOMING");
		for (LineInfo info : pt1Infos.get(1))
			System.out.println(info);
*/
		
		List<LinkedList<LineInfo>> pt2Infos = map.get(line.point2);
		if (pt2Infos == null) {
			pt2Infos = new ArrayList<LinkedList<LineInfo>>();
			pt2Infos.add(new LinkedList<LineInfo>());
			pt2Infos.add(new LinkedList<LineInfo>());
			map.put(line.point2, pt2Infos);
		}
		List<LineInfo> incoming = pt2Infos.get(1);
		incoming.add(line);
/*
		System.out.println("Entry in pointMap for " + line.point2 + " is ");
		System.out.println("OUTGOING");
		for (LineInfo info : pt2Infos.get(0))
			System.out.println(info);
	
		
		System.out.println("INCOMING");
		for (LineInfo info : pt2Infos.get(1))
			System.out.println(info);

	*/	
		
	}
	
	private static HashMap<GeographicPoint,List<LinkedList<LineInfo>>> 
	buildPointMap(String filename)
	{
		BufferedReader reader = null;
        HashMap<GeographicPoint,List<LinkedList<LineInfo>>> pointMap = 
        		new HashMap<GeographicPoint,List<LinkedList<LineInfo>>>();
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
            	//System.out.println("Parsing line " + nextLine);
            	LineInfo line = splitInputString(nextLine);
            	//System.out.println("Found: " + line.point1 + " " + line.point2 +
            	//		" " + line.roadName + " " + line.roadType);
            	addToPointsMapOneWay(line, pointMap);
            	// Reverse the line info
            	
            	LineInfo lineRev = line.getReverseCopy();
            	addToPointsMapOneWay(lineRev, pointMap);
                //System.out.println(pointMap.size());
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
		
		return pointMap;
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
	
	
	/** Read in a file specifying route maps between airports.
	 * The file contains data as follows:
	 * Airline, AirlineID, Source airport, Source airport ID,
	 * Destination airport, Destination airport ID, Codeshare, Stops, Equipment
	 * This method will only read in nonstop routes (with Stops == 0)
	 * Vertices are airports (labeled with Strings)
	 * Edges represent nonstop routes
	 * @param filename
	 * @param graph
	 */
	public static void loadRoutes(String filename, Graph graph)
	{
		String source;
		String destination;
		int sourceIndex;
		int destinationIndex;
		
		int lineCount = 0; //for debugging
		
		//Initialize vertex label HashMap in graph
		graph.initializeLabels();
		
		//Read in flights from file
		BufferedReader reader = null;
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            while ((nextLine = reader.readLine()) != null) {
            	String[] flightInfo = nextLine.split(",");
//           	//Only count nonstop flights
//            	if (Integer.parseInt(flightInfo[7])==0) {
            		source = flightInfo[2];
            		destination = flightInfo[4];
                	//System.out.print("Line:" + lineCount);
                	//System.out.println(". Source: "+ source + ", destination: "+destination);
            		//Add edge for this flight, if both source & destination are already vertices.
            		//If one of these airports is missing, add vertex for it and then place edge.
            		if (!graph.hasVertex(source)) {
            			sourceIndex = graph.addVertex();
            			graph.addLabel(sourceIndex, source);
            		}
            		else {
            			sourceIndex = graph.getIndex(source);
            		}
            		if (!graph.hasVertex(destination)) {
            			destinationIndex = graph.addVertex();
            			graph.addLabel(destinationIndex, destination);
            		}
            		else {
            			destinationIndex = graph.getIndex(destination);
            		}
            		graph.addEdge(sourceIndex, destinationIndex);
            	}
            	lineCount ++;
//           }
    		reader.close();
		} catch (IOException e) {
            System.err.println("Problem loading route file: " + filename);
            e.printStackTrace();
        }

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
	
	public int hashCode()
	{
		return point1.hashCode() + point2.hashCode();
		
	}
	public boolean sameRoad(LineInfo info)
	{
		return info.roadName.equals(this.roadName) && info.roadType.equals(this.roadType);
	}
	
	/** Return a copy of this LineInfo in the other direction */
	public LineInfo getReverseCopy()
	{
		return new LineInfo(this.point2, this.point1, this.roadName, this.roadType);
	}
	public String toString()
	{
		return this.point1 + " " + this.point2 + " " + this.roadName + " " + this.roadType;
		
	}
	
	
}
