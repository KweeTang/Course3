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
import java.util.function.Consumer;
import util.GraphLoader;
import geography.*;

public class DijkstraGrader implements Runnable {
    public String feedback;

    public int correct;

    private static final int TESTS = 4;

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
        DijkstraGrader grader = new DijkstraGrader();
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

        GraphLoader.loadRoadMap("data/" + file, graph);
        CorrectAnswer corr = new CorrectAnswer("data/" + file + ".answer");
        //printCorrect("data/" + file + ".answer", graph, start, end);

        judge(i, graph, corr, start, end);
    }

    public void judge(int i, MapGraph result, CorrectAnswer corr, GeographicPoint start, GeographicPoint end) {
        Consumer<GeographicPoint> temp = (x) -> {};

        feedback += appendFeedback(i, "Running Dijkstra's algorithm from (" + start.getX() + ", " + start.getY() + ") to (" + end.getX() + ", " + end.getY() + ")");
        List<GeographicPoint> path = result.dijkstra(start, end, temp);
        if (path == null) {
            if (corr.path == null) {
                feedback += "PASSED.";
                correct++;
            } else {
                feedback += "FAILED. Your implementation returned null; expected \\n" + printPath(corr.path) + ".";
            }
        } else if (path.size() != corr.path.size() || !corr.path.containsAll(path)) {
            feedback += "FAILED. Expected: \\n" + printPath(corr.path) + "Got: \\n" + printPath(path);
            if (path.size() != corr.path.size()) {
                feedback += "Your result has size " + path.size() + "; expected " + corr.path.size() + ".";
            } else {
                feedback += "Correct size, but incorrect path.";
            }
        } else {
            feedback += "PASSED.";
            correct++;
        }
    }

    public String printPath(List<GeographicPoint> path) {
        String ret = "";
        for (GeographicPoint point : path) {
            ret += point + "\\n";
        }
        return ret;
    }

    public void printCorrect(String file, MapGraph graph, GeographicPoint start, GeographicPoint end) {
        try {
            PrintWriter outfile = new PrintWriter(file);
            Consumer<GeographicPoint> temp = (x) -> {};
            List<GeographicPoint> path = graph.dijkstra(start, end, temp);
            if (path != null) {
                for (GeographicPoint point : path) {
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
            runTest(1, "map1.txt", "MAP: Straight line (-3 <- -2 <- -1 <- 0 -> 1 -> 2-> 3 ->...)", new GeographicPoint(0, 0), new GeographicPoint(6, 6));

            runTest(2, "map2.txt", "MAP: Example map from the writeup", new GeographicPoint(7, 3), new GeographicPoint(4, -1));

            runTest(3, "map3.txt", "MAP: Right triangle (with a little detour)", new GeographicPoint(0, 0), new GeographicPoint(0, 4));

            runTest(4, "ucsd.map", "UCSD MAP: Intersections around UCSD", new GeographicPoint(32.8709815, -117.2434254), new GeographicPoint(32.8742087, -117.2381344));

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
