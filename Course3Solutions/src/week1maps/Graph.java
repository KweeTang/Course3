package week1maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** An abstract class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * The edges of the graph are not labeled.
 * Representation of edges is left abstract.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of elements stored in the vertices of the graph
 */

public abstract class Graph<E> {

	private int numVertices;
	private int numEdges;
	
	public Graph() {
		numVertices = 0;
		numEdges = 0;
	}
	
	public int getNumVertices() {
		return numVertices;
	}
	
	public int getNumEdges() {
		return numEdges;
	}
	
	public void setNumVertices(int n) {
		numVertices = n;
	}

	public void setNumEdges(int n) {
		numEdges = n;
	}
	
	public abstract void addVertex(E v);
	
	public abstract void addEdge(E v, E w);

	public abstract String adjacencyString();
	
	public String toString() {
		String s = "Graph with "+numVertices+" vertices and "+numEdges+" edges.\n";
		s += adjacencyString();
		return s;
	}

}
