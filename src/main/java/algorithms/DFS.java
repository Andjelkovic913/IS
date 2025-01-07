package algorithms;

import org.graphstream.graph.*;
import java.util.Stack;

public class DFS {

    public static void dfs(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        Stack<Node> stack = new Stack<>();
        stack.add(startNode);
        startNode.setAttribute("visited", true);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            markNodeVisited(current, "blue", nodeVisitDelay);

            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange", nodeVisitDelay);
                break;
            }

            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);
                if (neighbor.getAttribute("visited") == null) {
                    neighbor.setAttribute("visited", true);
                    stack.add(neighbor);
                    markNodeInProcess(neighbor, edge, edgeVisitDelay);
                }
            }
        }
    }

    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay);
    }

    private static void markNodeInProcess(Node node, Edge edge, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: green;");
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay);
    }
}
