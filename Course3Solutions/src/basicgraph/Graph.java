package basicgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.MapLoader;

/** An abstract class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * Vertices are labeled by integers 0 .. n-1. 
 * The edges of the graph are not labeled.
 * Representation of edges is left abstract.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 * 
 */

public abstract class Graph {

	private int numVertices;
	private int numEdges;

	/**
	 * Create a new empty Graph
	 */
	public Graph() {
		numVertices = 0;
		numEdges = 0;
	}
	
	/**
	 * Report size of vertex set
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices() {
		return numVertices;
	}
	
	/**
	 * Test whether some vertex in the graph is labeled 
	 * with a given index.
	 * @param The index being checked
	 * @return True if there's a vertex in the graph with this index; false otherwise.
	 */
	public boolean hasVertex(int v)
	{
		return v < getNumVertices();
	}
	
	/**
	 * Report size of edge set
	 * @return The number of edges in the graph.
	 */	
	public int getNumEdges() {
		return numEdges;
	}
	
	/**
	 * Add new vertex to the graph.  This vertex will
	 * have as its index the next available integer.
	 * Precondition: contiguous integers are used to 
	 * index vertices.
	 */
	public void addVertex() {
		implementAddVertex();
		numVertices ++;
	}
	
	/**
	 * Abstract method implementing adding a new
	 * vertex to the representation of the graph.
	 */
	public abstract void implementAddVertex();
	
	/**
	 * Add new edge to the graph between given vertices,
	 * @param u Index of the start point of the edge to be added. 
	 * @param v Index of the end point of the edge to be added. 
	 */
	public void addEdge(int v , int w) {
		//System.out.println("Adding edge between "+v+" and "+w);
		numEdges ++;
		if (v < numVertices && w < numVertices) {
			implementAddEdge(v , w);			
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * Abstract method implementing adding a new
	 * edge to the representation of the graph.
	 */
	public abstract void implementAddEdge(int v , int w);
	
	/**
	 * Get all (out-)neighbors of a given vertex.
	 * @param v Index of vertex in question.
	 * @return List of indices of all vertices that are adjacent to v
	 * 	via outgoing edges from v. 
	 */
	public abstract List<Integer> getNeighbors(int v); 
	
	/**
	 * Get all in-neighbors of a given vertex.
	 * @param v Index of vertex in question.
	 * @return List of indices of all vertices that are adjacent to v
	 * 	via incoming edges to v. 
	 */
	public abstract List<Integer> getInNeighbors(int v);

	// Helper method learners may implement en route to degree sequence
	public int outdegree(int v) {
		return getNeighbors(v).size();
	}
	
	//Helper method learners may implement en route to degree sequence
	public int indegree(int v) {
		return getInNeighbors(v).size();
	}
	
	//Helper method learners may implement en route to degree sequence
	public int degree(int v) {
		return outdegree(v) + indegree(v);
	}
	
	//Finding nodes at distance-2 away 
	public abstract List<Integer> getDistance2(int v); 

	public String toString() {
		String s = "\nGraph with " + numVertices + " vertices and " + numEdges + " edges.\n";
		s += "Degree sequence: " + degreeSequence() + ".\n";
		s += adjacencyString();
		return s;
	}

	public abstract String adjacencyString();
	
	// For learners to implement
	public String degreeSequence() {
		List<Integer> degrees = new ArrayList<Integer>();
		for (int i = 0; i < numVertices; i ++) {
			degrees.add(degree(i));
		}
		Collections.sort(degrees);
		return degrees.toString();
	}
	
	//Small graph example for testing.
	private static Graph buildTestGraph1(String type) {
		Graph test1 = null;
		if (type.equals("list")) {
			test1 = new GraphAdjList();
		}
		else if (type.equals("matrix")) {
			test1 = new GraphAdjMatrix();
		}
		else {
			System.out.println("ERROR: unidentified type; returning null");
			return null;
		}
		test1.addVertex();
		test1.addVertex();
		test1.addVertex();
		test1.addVertex();
		test1.addEdge(0, 0);
		test1.addEdge(0, 2);
		test1.addEdge(0, 3);
		test1.addEdge(2, 3);
		test1.addEdge(3, 3);
		return test1;
	}
	
	//Small graph example with multiple edges between some vertices.
	private static Graph buildTestGraph2(String type) {
		Graph test2 = null;
		if (type.equals("list")) {
			test2 = new GraphAdjList();
		}
		else if (type.equals("matrix")) {
			test2 = new GraphAdjMatrix();
		}
		else {
			System.out.println("ERROR: unidentified type; returning null");
			return null;
		}
		test2.addVertex();
		test2.addVertex();
		test2.addVertex();
		test2.addEdge(0, 1);
		test2.addEdge(0, 1);
		test2.addEdge(1, 0);
		test2.addEdge(1, 2);
		test2.addEdge(2, 2);
		return test2;
	}
	
	public static void main (String[] args) {

		Graph test1List = buildTestGraph1("list");
		System.out.println(test1List);
		test1List.getDistance2(0);
		test1List.getDistance2(3);

		Graph test1Mat = buildTestGraph1("matrix");
		System.out.println(test1Mat);
		test1Mat.getDistance2(0);		
		test1Mat.getDistance2(3);

		Graph test2List = buildTestGraph2("list");
		System.out.println(test2List);
		test2List.getDistance2(0);
		test2List.getDistance2(2);

		Graph test2Mat = buildTestGraph2("matrix");
		System.out.println(test2Mat);
		test2Mat.getDistance2(0);		
		test2Mat.getDistance2(2);
		
		GraphAdjList graphFromFile = new GraphAdjList();
		MapLoader.loadMap("data/test.map", graphFromFile);
		System.out.println(graphFromFile);


	}
}
