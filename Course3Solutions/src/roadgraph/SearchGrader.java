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


    public static String printList(List<Integer> lst) {
        String res = "";
        for (int i : lst) {
            res += i + "-";
        }
        return res.substring(0, res.length() - 1);
    }

    public static String printOutput(double score, String feedback) {
        return "Score: " + score + "\nFeedback: " + feedback;
    }

    public static String appendFeedback(int num, String test) {
        return "\n** Test #" + num + ": " + test + "...";
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
            System.out.println(printOutput((double)grader.correct / TESTS, grader.feedback + "\nYour program entered an infinite loop."));
        }
    }

    public void runTest(int i, String file, String desc, GeographicPoint start, GeographicPoint end) {
        MapGraph graph = new MapGraph();

        feedback += "\n\n" + desc;

        GraphLoader.loadRoadMap("data/graders/mod2" + file, graph);
        CorrectAnswer corr = new CorrectAnswer("data/graders/mod2" + file + ".answer", true);

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
                feedback += "FAILED. Your implementation returned null; expected \n" + printBFSList(corr.path) + ".";
            }
        } else if (bfs.size() != corr.path.size() || !corr.path.containsAll(bfs)) {
            feedback += "FAILED. Expected: \n" + printBFSList(corr.path) + "Got: \n" + printBFSList(bfs);
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
            ret += point + "\n";
        }
        return ret;
    }

    @Override
    public void run() {
        feedback = "";

        correct = 0;

        try {
            runTest(1, "map1.txt", "Straight line (0->1->2->3->...)", new GeographicPoint(0, 0), new GeographicPoint(6, 6));

            runTest(2, "map2.txt", "Undirected straight line (0<->1<->2<->3<->...)", new GeographicPoint(6, 6), new GeographicPoint(0, 0));

            runTest(3, "map3.txt", "Square graph - Each edge has 2 nodes", new GeographicPoint(0, 0), new GeographicPoint(1, 2));

            runTest(4, "ucsd.map", "UCSD MAP: Intersections around UCSD", new GeographicPoint(32.8756538, -117.2435715), new GeographicPoint(32.8742087, -117.2381344));

            if (correct == TESTS)
                feedback = "All tests passed. Great job!" + feedback;
            else
                feedback = "Some tests failed. Check your code for errors, then try again:" + feedback;

        } catch (Exception e) {
            feedback += "\nError during runtime: " + e;
        }
            
        System.out.println(printOutput((double)correct / TESTS, feedback));
    }
}
