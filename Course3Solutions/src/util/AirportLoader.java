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


/**
 * @author Mia
 *
 * A utility class that reads a file of direct flights into a Graph
 */
public class AirportLoader 
{
	/** Read in a file specifying a map.
	 * The file contains data as follows:
	 * Airline, AirlineID, Source airport, Source airport ID,
	 * Destination airport, Destination airport ID, Codeshare, Stops, Equipment
	 * This method will only read in nonstop routes (with Stops == 0)
	 * @param filename
	 * @param graph
	 */
	public static void loadRoutes(String filename, Graph graph)
	{
		
		Collection<String> airports = new HashSet<String>();
        HashMap<GeographicPoint,List<LineInfo>> pointMap = 
        		buildPointMap(filename);
		
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		for (GeographicPoint pt : intersections) {
			map.addNode(pt);
			nodes.add(pt);
		}
		
		// Now we need to add the edges
		// This is the tricky part
		for (GeographicPoint pt : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			List<LineInfo> infoList = pointMap.get(pt);
			HashSet<LineInfo> used = new HashSet<LineInfo>();
			for (LineInfo info : infoList) {
				if (!used.contains(info)) {
					List<GeographicPoint> pointsOnEdge = 
							findPointsOnEdge(pointMap, info, pt, nodes, used);
					GeographicPoint end = pointsOnEdge.remove(pointsOnEdge.size()-1);
					double length = getRoadLength(pt, end, pointsOnEdge);
					map.addEdge(pt, end, info.roadName, info.roadType, length);
					
					// Now create road Segments for each edge
					HashSet<RoadSegment> segs = segments.get(pt);
					if (segs == null) {
						segs = new HashSet<RoadSegment>();
						segments.put(pt,segs);
					}
					RoadSegment seg = new RoadSegment(pt, end, pointsOnEdge, 
							info.roadName, info.roadType);
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
	

	/** Load a file into a basicgraph Graph object.
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
		HashMap<GeographicPoint,List<LineInfo>> pointMap = 
        		buildPointMap(filename);
		
		HashMap<Integer,GeographicPoint> vertexMap = 
				new HashMap<Integer,GeographicPoint>();
		HashMap<GeographicPoint,Integer> reverseMap = 
				new HashMap<GeographicPoint,Integer>();
		
        // Add the nodes to the graph
		List<GeographicPoint> intersections = findIntersections(pointMap);
		int index = 0;
		for (GeographicPoint pt : intersections) {
			theGraph.addVertex();
			vertexMap.put(index, pt);
			reverseMap.put(pt, index);
			index++;
		}

		// Now add the edges
		Collection<Integer> nodes = vertexMap.keySet();
		for (Integer nodeNum : nodes) {
			// Trace the node to its next node, building up the points 
			// on the edge as you go.
			GeographicPoint pt = vertexMap.get(nodeNum);
			List<LineInfo> infoList = pointMap.get(pt);
			HashSet<LineInfo> used = new HashSet<LineInfo>();
			for (LineInfo info : infoList) {
				if (!used.contains(info)) {
					GeographicPoint end = findEndOfEdge(pointMap, info, pt, 
							theGraph, used, reverseMap);
					Integer endNum = reverseMap.get(end);
					theGraph.addEdge(nodeNum, endNum);
				}
			}
		}		
	}
	
	/**
	 * Loads a graph from a file.  The file is specified with each 
	 * line representing an edge.  Vertices are numbered from 
	 * 0..1-numVertices.
	 * The first line of the file contains a single int which is the 
	 * number of vertices in the graph.
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
	
	
	private static HashMap<String,List<LineInfo>> 
	buildPointMap(String filename)
	{
		BufferedReader reader = null;
        HashMap<String,List<LineInfo>> pointMap = 
        		new HashMap<GeographicPoint,List<LineInfo>>();
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
            	//System.out.println("Parsing line " + nextLine);
            	LineInfo line = splitInputString(nextLine);
            	//System.out.println("Found: " + line.point1 + " " + line.point2 +
            	//		" " + line.roadName + " " + line.roadType);
            	addToPointMap(pointMap, line, line.point1);
                addToPointMap(pointMap, line, line.point2);
                //System.out.println(pointMap.size());
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
		
		return pointMap;
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

		// route data parsed as
		// Airline, Airline ID, Source Airport, Source Airport ID,
		// Destination Airport, Destination Airport ID, Codeshare,
		// Stops, Equipment
    	String source = tokens.get(2);
        String destination = tokens.get(4);
        int stops = Integer.parseInt(tokens.get(7));
        return new LineInfo(source, destination, stops);
		
	}
	
}


class LineInfo
{
	String sourceAirport;
	String destinationAirport;
	boolean nonstop;
	
	LineInfo(String sourceAirport, String destinationAirport, int stops) 
	{
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;
		this.nonstop = (stops == 0);
	}
	
	
}

