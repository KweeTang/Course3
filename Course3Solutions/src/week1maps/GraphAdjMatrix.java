package week1maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** A class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * The edges of the graph are not labeled.
 * Edges represented in adjacency matrix.
 * Assume vertices are labelled by integers 0..numVertices-1
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 */
public class GraphAdjMatrix extends Graph<Integer> {

	private final int defaultNumVertices = 5;
	private int[][] adjMatrix;
	
	/** Create a new empty Graph */
	public GraphAdjMatrix () {
		adjMatrix = new int[defaultNumVertices][defaultNumVertices];
	}
	
	public void addVertex(Integer v) {
		int index = getNumVertices();
		setNumVertices(index+1);
		if (index >= adjMatrix.length) {
			int[][] newAdjMatrix = new int[index*2][index*2];
			for (int i=0; i<adjMatrix.length; i++) {
				for (int j=0; j<adjMatrix.length; j++) {
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
		for (int i=0; i<adjMatrix[index].length; i++) {
			adjMatrix[index][i]=0;
		}
	}
	
	public void addEdge(Integer v, Integer w) {
		adjMatrix[v][w] = 1;
		setNumEdges(getNumEdges()+1);
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

	public static void main (String[] args) {
		GraphAdjList<String> graphList1 = new GraphAdjList<String>();
		graphList1.addVertex("A");
		graphList1.addVertex("B");
		graphList1.addVertex("C");
		graphList1.addVertex("D");
		graphList1.addEdge("A", "B");
		graphList1.addEdge("A", "D");
		graphList1.addEdge("B", "B");
		System.out.println(graphList1);	
		GraphAdjList<Integer> graphList2 = new GraphAdjList<Integer>();
		graphList2.addVertex(1);
		graphList2.addVertex(2);
		graphList2.addVertex(3);
		graphList2.addVertex(4);
		graphList2.addEdge(1, 1);
		graphList2.addEdge(1, 3);
		graphList2.addEdge(4, 4);
		System.out.println(graphList2);
		GraphAdjMatrix graphMat = new GraphAdjMatrix();
		graphMat.addVertex(1);
		graphMat.addVertex(2);
		graphMat.addVertex(3);
		graphMat.addVertex(4);
		graphMat.addEdge(1, 1);
		graphMat.addEdge(1, 3);
		graphMat.addEdge(4, 4);
		System.out.println(graphMat);
	}

}
