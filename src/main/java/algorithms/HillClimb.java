package algorithms;

import org.graphstream.graph.*;
import java.util.*;

public class HillClimb {

    public static void hillClimbing(Graph graph, Node startNode, Node targetNode, int nodeVisitDelay, int edgeVisitDelay) throws InterruptedException {
        // Иницијализација - постављање свих чворова као "непосећених"
        for (Node node : graph) {
            node.setAttribute("visited", false);  // Постављамо све чворове као непосећене
        }

        Stack<Node> stack = new Stack<>();
        stack.push(startNode);  // Додајемо почетни чвор на стек
        startNode.setAttribute("visited", true);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();  // Узимамо чвор са врха стека

            // Обележавамо чвор као посећен
            markNodeVisited(currentNode, "blue", nodeVisitDelay);

            // Ако је тренутни чвор циљ, прекинути претрагу
            if (currentNode.equals(targetNode)) {
                markNodeVisited(currentNode, "orange", nodeVisitDelay); // Обојити циљ у наранџасто
                System.out.println("Циљни чвор пронађен!");
                reconstructPath(currentNode); // Реконструкција пута
                return;
            }

            // Дохватити непосећене суседе
            List<Node> neighbors = getUnvisitedNeighbors(currentNode);

            // Сортирати суседе по вредностима хеуристике у растућем редоследу
            neighbors.sort(Comparator.comparingDouble(neighbor -> getHeuristicValue(neighbor, targetNode)));

            // Додати сортиране суседе на стек, редом од последњег до првог
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                Node neighbor = neighbors.get(i);
                neighbor.setAttribute("parent", currentNode);  // Обележавамо родитеља суседа
                stack.push(neighbor);  // Додајемо суседа на врх стека
                neighbor.setAttribute("ui.style", "fill-color: green;"); // Обојити суседа у зелено
                neighbor.setAttribute("visited", true); // Означити суседа као посећеног

                // Обележавамо ивицу као пређену
                Edge edge = currentNode.getEdgeBetween(neighbor);
                if (edge != null) {
                    markEdgeTraversed(edge, edgeVisitDelay);
                }
            }
        }

        System.out.println("Претрага није била успешна.");
    }

    // Метод за добијање свих непосећених суседа тренутног чвора
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

    // Маркирамо чвор као посећен и мењамо његову боју
    private static void markNodeVisited(Node node, String color, int delay) throws InterruptedException {
        node.setAttribute("visited", true);
        node.setAttribute("ui.style", "fill-color: " + color + ";");
        Thread.sleep(delay); // Delay за визуализацију
    }

    // Маркирамо ивицу као пређену
    private static void markEdgeTraversed(Edge edge, int delay) throws InterruptedException {
        edge.setAttribute("ui.style", "fill-color: blue;");
        Thread.sleep(delay); // Delay за визуализацију
    }

    // Хеуристичка функција - Еуклидска дистанца (за графове у 2D простору)
    public static double getHeuristicValue(Node currentNode, Node targetNode) {
        double x1 = currentNode.getAttribute("x");
        double y1 = currentNode.getAttribute("y");
        double x2 = targetNode.getAttribute("x");
        double y2 = targetNode.getAttribute("y");

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)); // Еуклидска дистанца између два чвора
    }

    // Реконструкција пута - праћење родитељских веза од циља до почетног чвора
    private static void reconstructPath(Node targetNode) {
        Node currentNode = targetNode;
        List<Node> path = new ArrayList<>();

        // Реконструкција пута кроз родитељске везе
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getAttribute("parent");
        }

        // Исписивање пута од почетног до циљног чвора
        System.out.println("Пут до циља:");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i).getId() + " ");
        }
        System.out.println(); // Нови ред за крај пута
    }
}
