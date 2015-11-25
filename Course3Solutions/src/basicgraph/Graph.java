package basicgraph;

import java.util.ArrayList;
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
	
	public Graph() {
		numVertices = 0;
		numEdges = 0;
	}
	
	public int getNumVertices() {
		return numVertices;
	}
	
	public boolean hasVertex(int v)
	{
		return v < getNumVertices();
	}
	
	public int getNumEdges() {
		return numEdges;
	}
	
	public void addVertex( ) {
		implementAddVertex();
		numVertices ++;
	}
	
	public abstract void implementAddVertex();

	public void addEdge( int v , int w ) {
		//System.out.println("Adding edge between "+v+" and "+w);
		numEdges ++;
		if (v < numVertices && w < numVertices) {
			implementAddEdge( v , w );			
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public abstract void implementAddEdge(int v , int w);

	public abstract List<Integer> getNeighbors(int v); 

	public String toString() {
		String s = "Graph with "+numVertices+" vertices and "+numEdges+" edges.\n";
		s += adjacencyString();
		return s;
	}

	public abstract String adjacencyString();
	
	public static void main (String[] args) {

		GraphAdjList graphList = new GraphAdjList();
		graphList.addVertex();
		graphList.addVertex();
		graphList.addVertex();
		graphList.addVertex();
		graphList.addEdge(0, 0);
		graphList.addEdge(0, 2);
		graphList.addEdge(0, 3);
		graphList.addEdge(2, 3);
		graphList.addEdge(3, 3);
		System.out.println(graphList);
		graphList.getDistance2(0);
		graphList.getDistance2(3);

		GraphAdjMatrix graphMat = new GraphAdjMatrix();
		graphMat.addVertex();
		graphMat.addVertex();
		graphMat.addVertex();
		graphMat.addVertex();
		graphMat.addEdge(0, 0);
		graphMat.addEdge(0, 2);
		graphMat.addEdge(0, 3);
		graphMat.addEdge(2, 3);
		graphMat.addEdge(3, 3);
		System.out.println(graphMat);
		graphMat.getDistance2(0);		
		graphMat.getDistance2(3);
		
		GraphAdjList graphFromFile = new GraphAdjList();
		MapLoader.loadMap("data/test.map", graphFromFile);
		System.out.println(graphFromFile);

	}
}
