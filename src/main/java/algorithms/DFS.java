package algorithms;

import org.graphstream.graph.*;
import java.util.Stack;

public class DFS {

    public static void dfs(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        markNodeInProcess(startNode, "green", nodeVisitDelay);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            markNodeVisited(current, "blue", nodeVisitDelay);

            // Ne bi trebalo da se desi iz prve iteracije ali je obradjen slucaj svakako
            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange", nodeVisitDelay); // Означи циљ као циљно
                break;
            }

            // Prolaz kroz susede
            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);

                if (neighbor.getAttribute("visited") == null) { // Непосећен чвор
                    stack.push(neighbor);
                    markNodeInProcess(neighbor, "green", nodeVisitDelay); // Означимо у обради
                    markEdgeTraversed(edge, edgeVisitDelay); // Обележимо ивицу
                }
            }
        }
    }

    // Метод за означавање да је чвор у обради (додат у стек)
    private static void markNodeInProcess(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay);
    }

    // Метод за означавање да је чвор обрађен (скинут са стека)
    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        node.setAttribute("visited", true); // Обележавамо као посећен тек сада
        Thread.sleep(delay);
    }

    // Метод за означавање преласка кроз ивицу
    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay);
    }
}
