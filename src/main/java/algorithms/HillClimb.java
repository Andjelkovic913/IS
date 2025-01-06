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

            // Čvor postavljamo kao posećen tek kad je uklonjen iz steka
            markNodeVisited(currentNode, "blue", nodeVisitDelay); // Obeležavanje čvora kao posećenog

            // Ako je trenutni čvor cilj, prekinuti pretragu
            if (currentNode.equals(targetNode)) {
                markNodeVisited(currentNode, "orange", nodeVisitDelay); // Obojiti cilj u narandžasto
                System.out.println("Ciljni čvor pronađen!");
                reconstructPath(currentNode); // Rekonstrukcija puta
                return;
            }

            // Dohvatiti neposećene susede i sortirati ih prema heurističkoj vrednosti
            List<Node> neighbors = getUnvisitedNeighbors(currentNode);
            neighbors.sort((neighbor1, neighbor2) -> {
                double h1 = getHeuristicValue(neighbor1, targetNode);
                double h2 = getHeuristicValue(neighbor2, targetNode);
                return Double.compare(h1, h2); // Sortiranje suseda prema heurističkoj vrednosti
            });

            // Dodajemo susede na stek, izuzetno birajući onog sa najmanjom heuristikom
            for (Node neighbor : neighbors) {
                neighbor.setAttribute("parent", currentNode); // Obeležavamo roditelja suseda
                stack.push(neighbor); // Dodavanje na stek
                neighbor.setAttribute("ui.style", "fill-color: green;"); // Oboji novi čvor u zeleno

                // Vizualizacija pređenog puta (ako ivica postoji)
                Edge edge = currentNode.getEdgeBetween(neighbor);
                if (edge != null) {
                    markEdgeTraversed(edge, edgeVisitDelay); // Obeležavanje ivice kao pređene
                }
            }
        }

        System.out.println("Pretraga nije bila uspešna.");
    }

    // Metoda za dobijanje svih neposećenih suseda trenutnog čvora
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

    // Markiramo čvor kao posećen i menjamo njegovu boju
    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay); // Delay za vizualizaciju
    }

    // Markiramo ivicu kao pređenu
    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay); // Delay za vizualizaciju
    }

    // Heuristička funkcija - Euklidska distanca (za grafove u 2D prostoru)
    public static double getHeuristicValue(Node currentNode, Node targetNode) {
        double x1 = currentNode.getAttribute("x");
        double y1 = currentNode.getAttribute("y");
        double x2 = targetNode.getAttribute("x");
        double y2 = targetNode.getAttribute("y");

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)); // Euklidska distanca između dva čvora
    }

    // Rekonstrukcija puta - praćenje roditeljskih veza od cilja do početnog čvora
    private static void reconstructPath(Node targetNode) {
        Node currentNode = targetNode;
        List<Node> path = new ArrayList<>();

        // Rekonstrukcija puta kroz roditeljske veze
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getAttribute("parent");
        }

        // Ispisivanje puta od početnog do ciljnog čvora
        System.out.println("Put do cilja:");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i).getId() + " ");
        }
        System.out.println(); // Novi red za kraj puta
    }
}
