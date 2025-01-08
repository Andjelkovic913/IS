package algorithms;

import org.graphstream.graph.*;
import java.util.*;

public class HillClimb {

    public static void hillClimbing(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        // Иницијализација - постављање свих чворова као "непосећених"
        for (Node node : graph) {
            node.setAttribute("visited", false);  // Ovo nije potrebno, bilo je potrebno za neki drugi slucaj ali sam ispravio ovo se moze i obrisati
        }

        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        startNode.setAttribute("visited", true);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            currentNode.setAttribute("visited", true);
            // Обележавамо чвор као посећен
            markNodeVisited(currentNode, "blue", nodeVisitDelay);

            // Ако је тренутни чвор циљ, прекинути претрагу
            if (currentNode.equals(targetNode)) {
                markNodeVisited(currentNode, "orange", nodeVisitDelay);
                System.out.println("Циљни чвор пронађен!");
                reconstructPath(currentNode);
                return;
            }

            // Uzima neposecene susede
            List<Node> neighbors = getUnvisitedNeighbors(currentNode);

            // Rastuce sortiranje
            neighbors.sort(Comparator.comparingDouble(neighbor -> getHeuristicValue(neighbor, targetNode)));

            for (int i = neighbors.size() - 1; i >= 0; i--) {
                Node neighbor = neighbors.get(i);
                neighbor.setAttribute("parent", currentNode);
                stack.push(neighbor);
                neighbor.setAttribute("ui.style", "fill-color: green;");



                Edge edge = currentNode.getEdgeBetween(neighbor);
                if (edge != null) {
                    markEdgeTraversed(edge, edgeVisitDelay);
                }
            }
        }

        System.out.println("Pretraga nije bila uspesna.");
    }


    private static List<Node> getUnvisitedNeighbors(Node node) {
        List<Node> unvisitedNeighbors = new ArrayList<>();
        for (Edge edge : node.getEdgeSet()) {
            Node neighbor = edge.getOpposite(node);
            if (neighbor.getAttribute("visited") == null || !neighbor.getAttribute("visited", Boolean.class)) {
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

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)); // Еуклидска дистанца између два чвора
    }


    private static void reconstructPath(Node targetNode) {
        Node currentNode = targetNode;
        List<Node> path = new ArrayList<>();


        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getAttribute("parent");
        }


        System.out.println("Put do cilja:");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i).getId() + " ");
        }
        System.out.println();
    }
}
