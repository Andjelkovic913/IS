package algorithms;

import org.graphstream.graph.*;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class Astar {

    public static void astar(Graph graph, Node startNode, Node targetNode, Map<Node, Double> heuristicMap, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> (double) n.getAttribute("totalCostEstimate")));
        openSet.add(startNode);

        initializeGraphAttributes(graph, startNode, targetNode, heuristicMap);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            markNodeVisited(current, "blue");
            Thread.sleep(nodeVisitDelay);

            // Da li je cilj
            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange");
                break;
            }

            // Prolaz kroz sve komsije
            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);
                double tentativePathCost = calculateTentativePathCost(current, edge);

                if (tentativePathCost < (double) neighbor.getAttribute("pathCost")) {
                    updateNodeCosts(neighbor, tentativePathCost, targetNode, heuristicMap);
                    markNodeInProcess(neighbor, openSet);
                    markEdgeTraversed(edge, "blue");
                    Thread.sleep(edgeVisitDelay);
                }
            }
        }
        markNodeVisited(startNode, "pink");
    }

    private static void initializeGraphAttributes(Graph graph, Node startNode, Node targetNode, Map<Node, Double> heuristicMap) {
        graph.getNodeSet().forEach(n -> {
            n.setAttribute("pathCost", Double.POSITIVE_INFINITY);
            n.setAttribute("totalCostEstimate", Double.POSITIVE_INFINITY);
        });
        startNode.setAttribute("pathCost", 0.0);
        startNode.setAttribute("totalCostEstimate", heuristicMap.get(startNode));
    }

    private static double calculateTentativePathCost(Node current, Edge edge) {
        return (double) current.getAttribute("pathCost") + (double) edge.getAttribute("length");
    }

    private static void updateNodeCosts(Node node, double pathCost, Node targetNode, Map<Node, Double> heuristicMap) {
        node.setAttribute("pathCost", pathCost);
        double totalCost = pathCost + heuristicMap.get(node);
        node.setAttribute("totalCostEstimate", totalCost);
        node.setAttribute("label", "pathCost: " + pathCost + " | totalCostEstimate: " + totalCost);
    }

    private static void markNodeVisited(Node node, String color) {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    private static void markNodeInProcess(Node node, PriorityQueue<Node> openSet) {
        if (!openSet.contains(node)) {
            openSet.add(node);
            node.setAttribute("ui.style", "fill-color: green;");
        }
    }

    private static void markEdgeTraversed(Edge edge, String color) {
        edge.setAttribute("ui.style", "fill-color: " + color + ";");
    }
}
