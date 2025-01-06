package algorithms;

import org.graphstream.graph.*;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Astar {

    public static void astar(Graph graph, Node startNode, Node targetNode, int heuristicChoice, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> (double) n.getAttribute("totalCostEstimate")));
        openSet.add(startNode);

        initializeGraphAttributes(graph, startNode, targetNode, heuristicChoice);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            markNodeVisited(current, "blue");
            Thread.sleep(nodeVisitDelay);

            // Provera da li je ciljni cvor
            if (current.equals(targetNode)) {
                markNodeVisited(current, "orange");
                break;
            }

            // Prolazi kroz komsije
            for (Edge edge : current.getEdgeSet()) {
                Node neighbor = edge.getOpposite(current);
                double tentativePathCost = calculateTentativePathCost(current, edge);

                if (tentativePathCost < (double) neighbor.getAttribute("pathCost")) {
                    updateNodeCosts(neighbor, tentativePathCost, targetNode, heuristicChoice);
                    markNodeInProcess(neighbor, openSet);
                    markEdgeTraversed(edge, "blue");
                    Thread.sleep(edgeVisitDelay);
                }
            }
        }
        markNodeVisited(startNode, "pink");
    }

    // Svi nodovi su setovani na infinity jer mi trazimo najmanji put do cvora i svaki put je manji od beskonacno
    //Ako nadje neki drugi kraci put do tog cvora onda ce da mu stavi tu manju vrednost na cvor
    private static void initializeGraphAttributes(Graph graph, Node startNode, Node targetNode, int heuristicChoice) {
        graph.getNodeSet().forEach(n -> {
            n.setAttribute("pathCost", Double.POSITIVE_INFINITY);
            n.setAttribute("totalCostEstimate", Double.POSITIVE_INFINITY);
        });
        startNode.setAttribute("pathCost", 0.0);
        startNode.setAttribute("totalCostEstimate", heuristicManager(startNode, targetNode, heuristicChoice));
    }

    //Izracunava trosak puta od trenutnog cvora do njegovog komsije putem grane
    private static double calculateTentativePathCost(Node current, Edge edge) {
        return (double) current.getAttribute("pathCost") + (double) edge.getAttribute("length");
    }

    // Update path cost and heuristic estimate for a node
    private static void updateNodeCosts(Node node, double pathCost, Node targetNode, int heuristicChoice) {
        node.setAttribute("pathCost", pathCost);
        double totalCost = pathCost + heuristicManager(node, targetNode, heuristicChoice);
        node.setAttribute("totalCostEstimate", totalCost);
        node.setAttribute("label", "pathCost: " + pathCost + " | totalCostEstimate: " + totalCost);
    }

    //Posecen cvor
    private static void markNodeVisited(Node node, String color) {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    // Oznaci ga kao vidjenog ali ne i posecenog
    private static void markNodeInProcess(Node node, PriorityQueue<Node> openSet) {
        if (!openSet.contains(node)) {
            openSet.add(node);
            node.setAttribute("ui.style", "fill-color: green;");
        }
    }

    //Posecena grana
    private static void markEdgeTraversed(Edge edge, String color) {
        edge.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    private static double heuristicManager(Node current, Node target, int heuristicChoice) {
        switch (heuristicChoice) {
            case 1:
                return heuristicNull();
            case 2:
                return heuristicMinEdge(current);
            case 3:
                return heuristicAverageEdge(current);
            case 4:
                return heuristicMinAverageEdge(current);
            case 5:
                return heuristicDegree(current);
            default:
                return heuristicNull();
        }
    }

    private static double heuristicNull() {
        return 0.0;
    }

    private static double heuristicMinEdge(Node node) {
        return getSmallestEdgeLength(node.getGraph()) * (node.getGraph().getEdgeCount() / 2);
    }

    private static double heuristicAverageEdge(Node node) {
        return getAverageEdgeLength(node.getGraph()) * (node.getGraph().getEdgeCount() / 3);
    }

    private static double heuristicMinAverageEdge(Node node) {
        double cMin = getSmallestEdgeLength(node.getGraph());
        double cAvg = getAverageEdgeLength(node.getGraph());
        int estimatedEdgesToGoal = node.getGraph().getEdgeCount() / 2;
        return Math.max(cMin * estimatedEdgesToGoal, cAvg * estimatedEdgesToGoal);
    }

    private static double heuristicDegree(Node node) {
        double cMin = getSmallestEdgeLength(node.getGraph());
        return cMin * node.getDegree();
    }

    //Izracunava najmanju granu u grafu
    private static double getSmallestEdgeLength(Graph graph) {
        return graph.getEdgeSet().stream()
                .mapToDouble(e -> (double) e.getAttribute("length"))
                .min()
                .orElse(Double.POSITIVE_INFINITY);
    }

    //Izracunava prosecnu duzinu grane u grafu
    private static double getAverageEdgeLength(Graph graph) {
        return graph.getEdgeSet().stream()
                .mapToDouble(e -> (double) e.getAttribute("length"))
                .average()
                .orElse(Double.POSITIVE_INFINITY);
    }
}
