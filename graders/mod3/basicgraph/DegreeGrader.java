package basicgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import util.GraphLoader;

public class DegreeGrader {
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
        DegreeGrader grader = new DegreeGrader();
        grader.run();
    }

    public void runTest(int i, String desc, int start) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();

        String file = "data/graph" + i + ".txt";
        String corr = readCorrect(file + ".degrees");
        
        feedback += "\\n\\nGRAPH: " + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list"); 

        GraphLoader.loadGraph(file, lst);
/*
        try {
            PrintWriter tempout = new PrintWriter(file + ".degrees");
            String result = lst.degreeSequence();
            tempout.println(result);
            tempout.close();
        } catch (Exception e) {
        }
*/            
        String result = lst.degreeSequence();
        judge(result, corr);
 
        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        GraphLoader.loadGraph(file, mat);
        result = mat.degreeSequence();
        judge(result, corr);
    }

    public void runSpecialTest(int i, String file, String desc, int start) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();

        file = "data/" + file;
        String corr = readCorrect(file + ".degrees");

        feedback += "\\n\\n" + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list");

        GraphLoader.loadOneWayMap(file, lst);

        String result = lst.degreeSequence();
        judge(result, corr);

        System.out.println(lst.adjacencyString());
        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        GraphLoader.loadOneWayMap(file, mat);
        result = mat.degreeSequence();
        judge(result, corr);
        System.out.println(mat.adjacencyString());

    }

    public void judge(String result, String corr) {
        if (result.length() != corr.length() || !result.equals(corr)) {
            feedback += "FAILED. Expected " + corr + ", got " + result + ". ";
        } else {
            feedback += "PASSED.";
            correct++;
        }
    }

    public String readCorrect(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.readLine();
        } catch (Exception e) {
            feedback += "\\nCould not open answer file! Please submit a bug report.";
            return "";
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
