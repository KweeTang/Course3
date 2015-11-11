package week1maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** A class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * The edges of the graph are not labeled.
 * Edges represented in adjacency matrix.
 * Assume vertices are labeled by integers 0..numVertices-1
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 */
public class GraphAdjMatrix extends Graph {

	private final int defaultNumVertices = 5;
	private int[][] adjMatrix;
	
	/** Create a new empty Graph */
	public GraphAdjMatrix () {
		adjMatrix = new int[defaultNumVertices][defaultNumVertices];
	}
	
	public void implementAddVertex() {
		int v = getNumVertices();
		if (v >= adjMatrix.length) {
			int[][] newAdjMatrix = new int[v*2][v*2];
			for (int i=0; i<adjMatrix.length; i++) {
				for (int j=0; j<adjMatrix.length; j++) {
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
		for (int i=0; i<adjMatrix[v].length; i++) {
			adjMatrix[v][i]=0;
		}
	}
	
	public void implementAddEdge(int v, int w) {
		adjMatrix[v][w] = 1;
	}
	
	public String adjacencyString() {
		String s = "Adjacency matrix: ";
		for (int i=0; i<adjMatrix.length;i++) {
			s += "\n\t"+i+": ";
			for (int j=0; j<adjMatrix[i].length; j++) {
			s += adjMatrix[i][j]+", ";
			}
		}
		return s;
	}



}
