package basicgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import util.GraphLoader;

public class GraphGrader {
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
        GraphGrader grader = new GraphGrader();
        grader.run();
    }

    public void runTest(int i, String desc, int start, List<Integer> corr) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();
        
        feedback += "\\n\\nGRAPH: " + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list"); 

        GraphLoader.loadGraph("data/graph" + i + ".txt", lst);
        List<Integer> result = lst.getDistance2(start);
        judge(result, corr);
 
        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        GraphLoader.loadGraph("data/graph" + i + ".txt", mat);
        result = mat.getDistance2(start);
        judge(result, corr);
    }

    public void runSpecialTest(int i, String file, String desc, int start, List<Integer> corr) {
        GraphAdjList lst = new GraphAdjList();
        GraphAdjMatrix mat = new GraphAdjMatrix();

        feedback += "\\n\\n" + desc;
        feedback += appendFeedback(i * 2 - 1, "Testing adjacency list");

        GraphLoader.loadOneWayMap("data/" + file, lst);
        List<Integer> result = lst.getDistance2(start);
        judge(result, corr);

        feedback += appendFeedback(i * 2, "Testing adjacency matrix");
        GraphLoader.loadOneWayMap("data/" + file, mat);
        result = mat.getDistance2(start);
        judge(result, corr);
    }

    public void judge(List<Integer> result, List<Integer> corr) {
        if (result.size() != corr.size() || !result.containsAll(corr)) {
            feedback += "FAILED. Expected " + printList(corr) + ", got " + printList(result) + ". ";
            if (result.size() > corr.size()) {
                feedback += "Make sure you aren't including vertices of distance 1. ";
            }
            if (result.size() < corr.size()) { 
                feedback += "Make sure you're exploring all possible paths. ";
            }
        } else {
            feedback += "PASSED.";
            correct++;
        }
    }

    public ArrayList<Integer> readCorrect(String file) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/" + file));
            String next;
            while ((next = br.readLine()) != null) {
                ret.add(Integer.parseInt(next));
            }
        } catch (Exception e) {
            feedback += "\\nCould not open answer file! Please submit a bug report.";
        }
        return ret;
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
        ArrayList<Integer> correctAns;

        try {
            correctAns = new ArrayList<Integer>();
            correctAns.add(7);
            runTest(1, "Straight line (0->1->2->3->...)", 5, correctAns);

            correctAns = new ArrayList<Integer>();
            correctAns.add(4);
            correctAns.add(6);
            correctAns.add(8);
            runTest(2, "Undirected straight line (0<->1<->2<->3<->...)", 6, correctAns);

            correctAns = new ArrayList<Integer>();
            correctAns.add(0);
            runTest(3, "Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)", 0, correctAns);

            correctAns = new ArrayList<Integer>();
            for (int i = 1; i < 10; i++)
                correctAns.add(i);
            runTest(4, "Star graph (starting at 5)", 5, correctAns);
            
            correctAns = new ArrayList<Integer>();
            for (int i = 6; i < 11; i++)
                correctAns.add(i);
            runTest(5, "Star graph - Each 'arm' consists of two undirected edges leading away from 0 (starting at 0)", 0, correctAns);

            correctAns = new ArrayList<Integer>();
            runTest(6, "Same graph as before (starting at 5)", 5, correctAns);

            correctAns = readCorrect("ucsd.map.twoaway");
            runSpecialTest(7, "ucsd.map", "UCSD MAP: Intersections around UCSD", 3, correctAns);

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
