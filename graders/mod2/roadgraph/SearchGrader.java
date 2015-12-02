package roadgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import util.MapLoader;
import geography.*;

public class SearchGrader {
    private String feedback;

    private int correct;

    private static final int TESTS = 14;

    public static String printList(List<Integer> lst) {
        String res = "";
        for (int i : lst) {
            res += i + "-";
        }
        return res.substring(0, res.length() - 1);
    }

    public static String makeJson(double score, String feedback) {
        return "{\"fractionalScore\": " + score + ", \"feedback\": \"" + feedback + "\"}";
    }

    public static String appendFeedback(int num, String test) {
        return "\\n** Test #" + num + ": " + test + "...";
    }

    public static void main(String[] args) {
        SearchGrader grader = new SearchGrader();
        grader.run();
    }

    public void runTest(int i, String file, String desc, GeographicPoint start, GeographicPoint end) {
        MapGraph graph = new MapGraph();
        HashMap<GeographicPoint, HashSet<RoadSegment>> segments = new HashMap<GeographicPoint, HashSet<RoadSegment>>();

        feedback += "\\n\\n" + desc;

        MapLoader.loadOneWayMap("data/" + file, graph, segments);
        CorrectAnswer corr = new CorrectAnswer("data/" + file + ".answer");

        judge(i, graph, corr, start, end);
    }

    public void judge(int i, MapGraph result, CorrectAnswer corr, GeographicPoint start, GeographicPoint end) {
        feedback += appendFeedback(i * 3 - 2, "Testing vertex count");
        if (result.getNumVertices() != corr.vertices) {
            feedback += "FAILED. Expected " + corr.vertices + "; got " + result.getNumVertices() + ".";
        } else {
            feedback += "PASSED.";
            correct++;
        }

        feedback += appendFeedback(i * 3 - 1, "Testing edge count");
        if (result.getNumEdges() != corr.edges) {
            feedback += "FAILED. Expected " + corr.edges + "; got " + result.getNumEdges() + ".";
        } else {
            feedback += "PASSED.";
            correct++;
        }

        feedback += appendFeedback(i * 3, "Testing BFS");
        List<GeographicPoint> bfs = result.bfs(start, end);
        if (bfs == null) {
            feedback += "FAILED. Your implementation returned null.";
        } else if (bfs.size() != corr.path.size() || !corr.path.containsAll(bfs)) {
            feedback += "FAILED. Expected: \\n" + printBFSList(corr.path) + "Got: \\n" + printBFSList(bfs);
            if (bfs.size() != corr.path.size()) {
                feedback += "Your result has size " + bfs.size() + "; expected " + corr.path.size() + ".";
            } else {
                feedback += "Correct size, but incorrect path.";
            }
        } else {
            feedback += "PASSED.";
            correct++;
        }
    }

    public String printBFSList(List<GeographicPoint> bfs) {
        String ret = "";
        for (GeographicPoint point : bfs) {
            ret += point + "\\n";
        }
        return ret;
    }

    public void printCorrect(String file, MapGraph graph, GeographicPoint start, GeographicPoint end) {
        try {
            PrintWriter out = new PrintWriter(file);
            out.println(graph.getNumVertices());
            out.println(graph.getNumEdges());
            List<GeographicPoint> bfs = graph.bfs(start, end);
            for (GeographicPoint point : bfs) {
                out.println(point.getX() + " " + point.getY());
            }
        } catch (Exception e) {
            feedback += "\\nCould not open answer file! Please submit a bug report.";
        }
    }

    public void run() {
        PrintWriter out;
        feedback = "";

        try {
            out = new PrintWriter("output.out");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        correct = 0;

        try {
            runTest(1, "Straight line (0->1->2->3->...)", 5);

            runTest(2, "Undirected straight line (0<->1<->2<->3<->...)", 6);

            runTest(3, "Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)", 0);

            runTest(4, "Star graph (starting at 5)", 5);
            
            runTest(5, "Star graph - Each 'arm' consists of two undirected edges leading away from 0 (starting at 0)", 0);

            runTest(6, "Same graph as before (starting at 5)", 5);

            runSpecialTest(7, "ucsd.map", "UCSD MAP: Intersections around UCSD", 4);

            if (correct == TESTS)
                feedback = "All tests passed. Great job!" + feedback;
            else
                feedback = "Some tests failed. Check your code for errors, then try again:" + feedback;

        } catch (Exception e) {
            feedback += "\\nError during runtime: " + e;
        }
            
        out.println(makeJson((double)correct / TESTS, feedback));
        out.close();
    }
}

class CorrectAnswer {
    public int vertices;
    public int edges;
    public List<GeographicPoint> path;
    public CorrectAnswer(String file) {
        try {
            Scanner s = new Scanner(new File(file));
            vertices = s.nextInt();
            edges = s.nextInt();
            int i = 0;
            while (s.hasNextDouble()) {
                double x = s.nextDouble();
                double y = s.nextDouble();
                path.add(new GeographicPoint(x, y));
            }
        } catch (Exception e) {
            System.err.println("Error reading correct answer!");
            e.printStackTrace();
        }
    }
            
}
