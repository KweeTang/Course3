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

/**
 * @author Christine
 *
 * A utility class that reads a file into a MapGraph
 */
public class MapLoader 
{
    /**
     * Loads a graph from a file.  The file is specified with each 
     * line representing an edge.  Vertices are numbered from 
     * 0..1-numVertices.
     * The first line of the file containts a single int which is the 
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
}
