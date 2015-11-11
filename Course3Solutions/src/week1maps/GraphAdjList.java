package week1maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** A class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * The edges of the graph are not labeled.
 * Edges represented in adjacency list.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of elements stored in the vertices of the graph
 */
public class GraphAdjList<E> extends Graph<E> {


	private Map<E,ArrayList<E>> adjLists;
	
	/** Create a new empty Graph */
	public GraphAdjList () {
		adjLists = new HashMap<E,ArrayList<E>>();
	}
	
	public void addVertex(E v) {
		ArrayList<E> neighbors = new ArrayList<E>();
		adjLists.put(v,  neighbors);
		setNumVertices(getNumVertices()+1);
	}
	
	public void addEdge(E v, E w) {
		(adjLists.get(v)).add(w);
		setNumEdges(getNumEdges()+1);
	}
	
	public String adjacencyString() {
		String s = "Adjacency list: ";
		for (E v : adjLists.keySet()) {
			s += "\n\t"+v+": ";
			for (E w : adjLists.get(v)) {
				s += w+", ";
			}
		}
		return s;
	}

	public static void main (String[] args) {
		GraphAdjList<String> graph1 = new GraphAdjList<String>();
		graph1.addVertex("A");
		graph1.addVertex("B");
		graph1.addVertex("C");
		graph1.addVertex("D");
		graph1.addEdge("A", "B");
		graph1.addEdge("A", "D");
		graph1.addEdge("B", "B");
		System.out.println(graph1);
	}

}
