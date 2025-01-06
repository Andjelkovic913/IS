package algorithms;

import org.graphstream.graph.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class HillClimb {

    public static void hillClimbing(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);

        Node currentNode;

        while (!stack.isEmpty()) {
            currentNode = stack.pop(); // Trenutni čvor se uklanja sa vrha steka
            if (currentNode.getAttribute("visited") == null) {
                markNodeVisited(currentNode, "blue", nodeVisitDelay); // Obeležavanje čvora kao posećenog

                if (currentNode.equals(targetNode)) {
                    markNodeVisited(currentNode, "orange", nodeVisitDelay); // Oboj čvor u narandžasto ako je cilj
                    System.out.println("Ciljni čvor pronađen!");
                    return;
                }

                List<Node> neighbors = getUnvisitedNeighbors(currentNode);
                neighbors.sort((neighbor1, neighbor2) -> {
                    double h1 = getHeuristicValue(neighbor1, targetNode);
                    double h2 = getHeuristicValue(neighbor2, targetNode);
                    return Double.compare(h1, h2);
                });

                for (Node neighbor : neighbors) {
                    neighbor.setAttribute("parent", currentNode);
                    stack.push(neighbor); // Dodavanje čvora na vrh steka
                    neighbor.setAttribute("ui.style", "fill-color: green;"); // Bojenje novih čvorova u zeleno

                    Edge edge = currentNode.getEdgeBetween(neighbor);
                    if (edge != null) {
                        markEdgeTraversed(edge, edgeVisitDelay); // Vizualizacija puta
                    }
                }
            }
        }

        System.out.println("Pretraga nije bila uspešna.");
    }

    private static List<Node> getUnvisitedNeighbors(Node node) {
        List<Node> unvisitedNeighbors = new ArrayList<>();
        for (Edge edge : node.getEdgeSet()) {
            Node neighbor = edge.getOpposite(node);
            if (neighbor.getAttribute("visited") == null) {
                unvisitedNeighbors.add(neighbor);
            }
        }
        return unvisitedNeighbors;
    }

    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay);
    }

    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay);
    }

    public static double getHeuristicValue(Node currentNode, Node targetNode) {
        double x1 = currentNode.getAttribute("x");
        double y1 = currentNode.getAttribute("y");
        double x2 = targetNode.getAttribute("x");
        double y2 = targetNode.getAttribute("y");

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
