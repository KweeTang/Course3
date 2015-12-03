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
import util.GraphLoader;
import geography.*;

public class SearchGrader implements Runnable {
    public String feedback;

    public int correct;

    private static final int TESTS = 12;

    public PrintWriter out;

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
        Thread thread = new Thread(grader);
        thread.start();
        long endTime = System.currentTimeMillis() + 10000;
        boolean infinite = false;
        while(thread.isAlive()) {
            if (System.currentTimeMillis() > endTime) {
                thread.stop();
                infinite = true;
                break;
            }
        }
        if (infinite) {
            grader.out.println(makeJson((double)grader.correct / TESTS, grader.feedback + "\\nYour program entered an infinite loop."));
            grader.out.close();
        }
    }

    public void runTest(int i, String file, String desc, GeographicPoint start, GeographicPoint end) {
        MapGraph graph = new MapGraph();

        feedback += "\\n\\n" + desc;

        GraphLoader.loadOneWayMap("data/" + file, graph);
        CorrectAnswer corr = new CorrectAnswer("data/" + file + ".answer");
        //printCorrect("data/" + file + ".answer", graph, start, end);

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
            if (corr.path == null) {
                feedback += "PASSED.";
                correct++;
            } else {
                feedback += "FAILED. Your implementation returned null; expected \\n" + printBFSList(corr.path) + ".";
            }
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
            PrintWriter outfile = new PrintWriter(file);
            outfile.println(graph.getNumVertices());
            outfile.println(graph.getNumEdges());
            List<GeographicPoint> bfs = graph.bfs(start, end);
            if (bfs != null) {
                for (GeographicPoint point : bfs) {
                    outfile.println(point.getX() + " " + point.getY());
                }
            }
            outfile.close();
        } catch (Exception e) {
            e.printStackTrace();
            feedback += "\\nCould not open answer file! Please submit a bug report.";
        }
    }

    @Override
    public void run() {
        feedback = "";

        try {
            out = new PrintWriter("output.out");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        correct = 0;

        try {
            runTest(1, "map1.txt", "Straight line (0->1->2->3->...)", new GeographicPoint(0, 0), new GeographicPoint(6, 6));

            runTest(2, "map2.txt", "Undirected straight line (0<->1<->2<->3<->...)", new GeographicPoint(6, 6), new GeographicPoint(0, 0));

            runTest(3, "map3.txt", "Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)", new GeographicPoint(0, 0), new GeographicPoint(1, 2));

            runTest(4, "ucsd.map", "UCSD MAP: Intersections around UCSD", new GeographicPoint(32.8756538, -117.2435715), new GeographicPoint(32.8742087, -117.2381344));

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
            path = null;
            if (s.hasNextDouble()) {
                path = new ArrayList<GeographicPoint>();
            }
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
