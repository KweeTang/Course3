package basicgraph;

import java.util.List;


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
	
}
