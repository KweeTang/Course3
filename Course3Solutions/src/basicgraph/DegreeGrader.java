package basicgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.GraphLoader;

public class DegreeGrader {
    private String feedback;

    private int correct;

    private static final int TESTS = 12;

    public static String printList(List<Integer> lst) {
        String res = "";
        for (int i : lst) {
            res += i + " ";
        }
        if (res.length() > 0)
            return res.substring(0, res.length() - 1);
        else
            return res;
    }

    public static String printOutput(double score, String feedback) {
        return "Score: " + score + "\n Feedback: " + feedback;
    }

    public static String appendFeedback(int num, String test) {
        return "\n** Test #" + num + ": " + test + "...";
    }

    public static void main(String[] args) {
        DegreeGrader grader = new DegreeGrader();
        grader.run();
    }

    public void runTest(int i, String desc) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();

        String file = "data/graders/mod1/graph" + i + ".txt";
        List<Integer> corr = readCorrect(file + ".degrees");
        
        feedback += "\n\nGRAPH: " + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list"); 

        GraphLoader.loadGraph(file, lst);
        List<Integer> result = lst.degreeSequence();
        judge(result, corr);
 
        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        GraphLoader.loadGraph(file, mat);
        result = mat.degreeSequence();
        judge(result, corr);

    }

    public void runSpecialTest(int i, String file, String desc, String type) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();

        file = "data/graders/mod1/" + file;
        List<Integer> corr = readCorrect(file + ".degrees");
        
        feedback += "\n\n" + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list");

        if (type.equals("road")) {
            GraphLoader.loadRoadMap(file, lst);
            GraphLoader.loadRoadMap(file, mat);
        } else if (type.equals("air")) {
            GraphLoader.loadRoutes(file, lst);
            GraphLoader.loadRoutes(file, mat);
        }
        List<Integer> result = lst.degreeSequence();
        judge(result, corr);

        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        result = mat.degreeSequence();
        judge(result, corr);

    }

    public void judge(List<Integer> result, List<Integer> corr) {
        if (result.size() != corr.size() || !result.containsAll(corr)) {
            feedback += "FAILED. Expected " + printList(corr) + ", got " + printList(result) + ". ";
        } else {
            feedback += "PASSED.";
            correct++;
        }
    }

    public List<Integer> readCorrect(String file) {
        List<Integer> ret = new ArrayList<Integer>();
        try {
            Scanner s = new Scanner(new File(file));
            while(s.hasNextInt()) { 
                ret.add(s.nextInt());
            }
        } catch (Exception e) {
            feedback += "\nCould not open answer file! Please submit a bug report.";
        }
        return ret;
    }

    public void run() {
        feedback = "";

        correct = 0;

        try {
            runTest(1, "Straight line (0->1->2->3->...)");

            runTest(2, "Undirected straight line (0<->1<->2<->3<->...)");

            runTest(3, "Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)");

            runTest(4, "Star graph - Each 'arm' consists of two undirected edges leading away from 0 (starting at 0)");

            runSpecialTest(5, "ucsd.map", "UCSD MAP: Intersections around UCSD", "road");
            
            runSpecialTest(6, "routesUA.dat", "AIRLINE MAP: Routes of airplanes around the world", "air");

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
