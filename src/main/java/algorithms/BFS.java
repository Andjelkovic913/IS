package algorithms;

import org.graphstream.graph.*;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static void bfs(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);
        markNodeVisited(startNode, "blue");

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange");
                return;
            }

            markNodeVisited(current, "darkblue");
            Thread.sleep(nodeVisitDelay);

            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);

                if (!isVisited(neighbor)) {
                    queue.add(neighbor);
                    markNodeVisited(neighbor, "green");
                    neighbor.setAttribute("parent", current);
                    // Ako je ciljni cvor pronadjen medju susedima mozemo da zavrsimo ranije
                    if (neighbor.equals(targetNode)) {
                        markNodeVisited(neighbor, "orange");
                        return;
                    }
                    markEdgeTraversed(edge, "blue");
                    Thread.sleep(edgeVisitDelay);
                }
            }
        }
    }

    private static void markNodeVisited(Node node, String color) {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    private static void markEdgeTraversed(Edge edge, String color) {
        edge.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    private static boolean isVisited(Node node) {
        return node.getAttribute("visited") != null;
    }
}
