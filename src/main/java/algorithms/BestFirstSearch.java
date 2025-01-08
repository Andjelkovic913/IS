package algorithms;

import org.graphstream.graph.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BestFirstSearch {

    public static void bestFirstSearch(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> (double) n.getAttribute("priority")));
        Set<Node> visitedNodes = new HashSet<>();

        startNode.setAttribute("priority", getHeuristicValue(startNode, targetNode));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // Označi čvor kao posećen
            if (visitedNodes.contains(current)) continue;
            visitedNodes.add(current);
            markNodeVisited(current, "blue", nodeVisitDelay);

            // Ako je cilj pronađen
            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange", nodeVisitDelay);
                reconstructPath(targetNode);
                return;
            }

            // Prođi kroz sve susede
            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);

                if (!visitedNodes.contains(neighbor)) {
                    double priority = getHeuristicValue(neighbor, targetNode);

                    if (openSet.contains(neighbor)) {
                        openSet.remove(neighbor); // Osveži ako se prioritet promeni
                    }

                    neighbor.setAttribute("priority", priority);
                    neighbor.setAttribute("parent", current);
                    neighbor.setAttribute("label", "priority: " + priority);
                    openSet.add(neighbor);
                    markNodeVisited(neighbor, "green", nodeVisitDelay);

                    markEdgeTraversed(edge, edgeVisitDelay);
                }
            }
        }

        System.out.println("Pretraga nije pronašla cilj.");
    }

    private static void reconstructPath(Node targetNode) {
        Node current = targetNode;
        StringBuilder path = new StringBuilder();

        while (current != null) {
            path.insert(0, current.getId() + " -> ");
            current = current.getAttribute("parent");
        }
        System.out.println("Path: " + path.substring(0, path.length() - 4));
    }

    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay);
    }

    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay);
    }

    private static double getHeuristicValue(Node currentNode, Node targetNode) {
        double x1 = currentNode.getAttribute("x");
        double y1 = currentNode.getAttribute("y");
        double x2 = targetNode.getAttribute("x");
        double y2 = targetNode.getAttribute("y");
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
