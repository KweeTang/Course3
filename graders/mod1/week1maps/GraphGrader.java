package week1maps;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GraphGrader {
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

    public final static int TESTS = 10;

    public void run() {
        PrintWriter out;
        String feedback = "";

        try {
            out = new PrintWriter("output.out");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int correct = 0;

        try {
            GraphAdjList adj = new GraphAdjList();
            GraphAdjMatrix mat = new GraphAdjMatrix();
            List<Integer> result;

            // straight line
            for (int i = 0; i < 10; i++) {
                adj.addVertex();
                mat.addVertex();
            }

            for (int i = 0; i < 9; i++) {
                adj.addEdge(i, i + 1);
                mat.addEdge(i, i + 1);
            }

            feedback += "\\n\\nStraight line graph (1->2->3->4->...)";
            feedback += appendFeedback(1, "Testing adjacency list...");
            result = adj.getDistance2(5);
            if (result.size() != 1 && result.get(0) != 7) {
                feedback += "FAILED. Expected 7, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += appendFeedback(2, "Testing adjacency matrix...");
            result = mat.getDistance2(5);
            if (result.size() != 1 || result.get(0) != 7) {
                feedback += "FAILED. Expected 7, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            for (int i = 0; i < 9; i++) {
                adj.addEdge(i + 1, i);
                mat.addEdge(i + 1, i);
            }

            feedback += "\\n\\nUndirected straight line (1<->2<->3<->4<->...)";
            List<Integer> correctAns = new ArrayList<Integer>();
            correctAns.add(4);
            correctAns.add(6);
            correctAns.add(8);

            feedback += appendFeedback(3, "Testing adjacency list...");
            result = adj.getDistance2(6);
            if (result.size() != 3 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected 4-6-8, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += appendFeedback(4, "Testing adjacency matrix...");
            result = mat.getDistance2(6);
            if (result.size() != 3 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected 4-6-8, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            adj = new GraphAdjList();
            mat = new GraphAdjMatrix();

            for (int i = 0; i < 10; i++) {
                adj.addVertex();
                mat.addVertex();
            }

            for (int i = 1; i < 10; i++) {
                adj.addEdge(0, i);
                mat.addEdge(0, i);
                adj.addEdge(i, 0);
                mat.addEdge(i, 0);
            }

            feedback += "\\n\\n0->others; others->0; starting at 0";
            correctAns = new ArrayList<Integer>();
            correctAns.add(0);

            feedback += appendFeedback(5, "Testing adjacency list...");
            result = adj.getDistance2(0);
            if (result.size() != 1 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected 0, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += appendFeedback(6, "Testing adjacency matrix...");
            result = mat.getDistance2(0);
            if (result.size() != 1 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected 0, got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += "\\n\\n0->others; others->0; starting at 5";
            correctAns = new ArrayList<Integer>();
            for (int i = 1; i < 10; i++)
                correctAns.add(i);

            feedback += appendFeedback(7, "Testing adjacency list...");
            result = adj.getDistance2(5);
            if (result.size() != 9 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected " + printList(correctAns) + ", got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += appendFeedback(8, "Testing adjacency matrix...");
            result = mat.getDistance2(5);
            if (result.size() != 9 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected " + printList(correctAns) + ", got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            adj = new GraphAdjList();
            mat = new GraphAdjMatrix();

            for (int i = 0; i < 11; i++) {
                adj.addVertex();
                mat.addVertex();
            }

            for (int i = 1; i < 6; i++) {
                adj.addEdge(0, i);
                mat.addEdge(0, i);
                adj.addEdge(i, i + 5);
                mat.addEdge(i, i + 5);
            }

            feedback += "\\n\\nFive 'arms' coming out of a central node, each with two edges";
            correctAns = new ArrayList<Integer>();
            for (int i = 6; i < 11; i++)
                correctAns.add(i);

            feedback += appendFeedback(9, "Testing adjacency list...");
            result = adj.getDistance2(0);
            if (result.size() != 5 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected " + printList(correctAns) + ", got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

            feedback += appendFeedback(10, "Testing adjacency matrix...");
            result = mat.getDistance2(0);
            if (result.size() != 5 || !result.containsAll(correctAns)) {
                feedback += "FAILED. Expected " + printList(correctAns) + ", got " + printList(result) + ".";
            } else {
                feedback += "PASSED.";
                correct++;
            }

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
