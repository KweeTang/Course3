package week1maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			for (int i = 0; i < adjMatrix.length; i ++) {
				for (int j = 0; j < adjMatrix.length; j ++) {
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
		for (int i=0; i < adjMatrix[v].length; i++) {
			adjMatrix[v][i] = 0;
		}
	}
	
	public void implementAddEdge(int v, int w) {
		adjMatrix[v][w] = 1;
	}
	
	public List<Integer> getNeighbors(int v) {
		List<Integer> neighbors = new ArrayList<Integer>();
		for (int i = 0; i < getNumVertices(); i ++) {
			if (adjMatrix[v][i] != 0) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	
	public String adjacencyString() {
		String s = "Adjacency matrix: ";
		for (int i=0; i<adjMatrix.length; i++) {
			s += "\n\t"+i+": ";
			for (int j=0; j<adjMatrix[i].length; j++) {
			s += adjMatrix[i][j]+", ";
			}
		}
		return s;
	}

	public List<Integer> getDistance2(int v) {
		List<Integer> distance2 = new ArrayList<Integer>();
		int numVertices = getNumVertices();
		int[][] squareMatrix = new int[numVertices][numVertices];
		for (int i = 0; i < numVertices; i ++) {
			for (int k = 0; k < numVertices; k ++) {
				squareMatrix[i][k] = 0;
				for (int j = 0; j < numVertices; j ++) {
					squareMatrix[i][k] += adjMatrix[i][j] * adjMatrix[j][k];
				}
			}
		}
		
		//Print squared adjacency matrix
		String s = "Square of adjacency matrix: ";
		for (int i=0; i<squareMatrix.length; i++) {
			s += "\n\t"+i+": ";
			for (int j=0; j<squareMatrix[i].length; j++) {
			s += squareMatrix[i][j]+", ";
			}
		}
		System.out.println(s);
		
		for (int i = 0; i < numVertices; i ++) {
			if (squareMatrix[v][i] != 0) {
				distance2.add(i);
			}
		}
		
		//Print distance 2 vertices
		System.out.println(distance2);
		
		return distance2;
	}


}
