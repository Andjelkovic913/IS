package algorithms;

import org.graphstream.graph.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class HillClimb {

    public static void hillClimbing(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        startNode.setAttribute("visited", true);
        startNode.setAttribute("ui.style", "fill-color: red;");  // Početni čvor u crvenoj boji
        Node currentNode = startNode;

        while (!stack.isEmpty()) {
            currentNode = stack.peek();
            markNodeVisited(currentNode, "blue", nodeVisitDelay);  // Postavi boju za posećeni čvor

            if (currentNode.equals(targetNode)) {
                markNodeVisited(currentNode, "orange", nodeVisitDelay);  // Oboj čvor u narandžasto ako je cilj
                break;
            }

            stack.pop();

            List<Node> neighbors = getUnvisitedNeighbors(currentNode);
            neighbors.sort(Comparator.comparingDouble(neighbor -> getHeuristicValue(neighbor, targetNode)));

            for (Node neighbor : neighbors) {
                neighbor.setAttribute("parent", currentNode);
                stack.push(neighbor);
                neighbor.setAttribute("ui.style", "fill-color: green;");  // Dodajte bojenje u zeleno za nove čvorove

                Edge edge = currentNode.getEdgeBetween(neighbor);
                if (edge != null) {
                    markEdgeTraversed(edge, edgeVisitDelay);  // Obemi ivicu
                }
            }
        }

        if (currentNode.equals(targetNode)) {
            System.out.println("Ciljni čvor pronađen!");
        } else {
            System.out.println("Pretraga nije bila uspešna.");
        }
    }

    private static List<Node> getUnvisitedNeighbors(Node node) {
        List<Node> unvisitedNeighbors = new ArrayList<>();
        for (Edge edge : node.getEdgeSet()) {
            Node neighbor = edge.getOpposite(node);
            if (neighbor.getAttribute("visited") == null) {
                unvisitedNeighbors.add(neighbor);
                neighbor.setAttribute("visited", true);
            }
        }
        return unvisitedNeighbors;
    }

    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay);
    }

    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay);
    }

    // Heuristička funkcija koja koristi udaljenost između trenutnog čvora i ciljnog čvora
    private static double getHeuristicValue(Node currentNode, Node targetNode) {
        // Pretpostavljamo da čvorovi imaju koordinate "x" i "y"
        double x1 = currentNode.getAttribute("x");
        double y1 = currentNode.getAttribute("y");
        double x2 = targetNode.getAttribute("x");
        double y2 = targetNode.getAttribute("y");

        // Euklidska udaljenost između trenutnog i ciljnog čvora
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
