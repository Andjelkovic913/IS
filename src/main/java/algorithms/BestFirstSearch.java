package algorithms;

import org.graphstream.graph.*;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BestFirstSearch {

    public static void bestFirstSearch(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> (double) n.getAttribute("priority")));

        // Koristimo istu heuristiku kao u HillClimbing
        startNode.setAttribute("priority", getHeuristicValue(startNode, targetNode));
        openSet.add(startNode);
        markNodeVisited(startNode,"blue", nodeVisitDelay);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            markNodeVisited(current, "blue", nodeVisitDelay);

            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange", nodeVisitDelay);
                break;
            }

            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);

                if (neighbor.getAttribute("visited") == null) {
                    // Koristimo istu heuristiku kao u HillClimbing
                    double priority = getHeuristicValue(neighbor, targetNode);
                    updateNeighborPriority(neighbor, priority, openSet);
                    neighbor.setAttribute("parent", current);
                    markEdgeTraversed(edge, edgeVisitDelay);
                }
            }

            current.setAttribute("visited", true);
        }
    }

    private static void updateNeighborPriority(Node neighbor, double priority, PriorityQueue<Node> openSet) {
        if (!openSet.contains(neighbor) ) {
            neighbor.setAttribute("priority", priority);
            neighbor.setAttribute("label", "priority: " + priority);


            openSet.add(neighbor);    // Dodati sa novim prioritetom
            neighbor.setAttribute("ui.style", "fill-color: green;");
            neighbor.setAttribute("visited", true);
        }
    }

    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        node.setAttribute("visited", true);
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
