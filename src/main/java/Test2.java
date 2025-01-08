import algorithms.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Test2 {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Graph");


        int numNodes = 15;
        Random random = new Random();

        for (int i = 0; i < numNodes; i++) {
            Node node = graph.addNode("A " + i);
            node.setAttribute("ui.label", "A" + i);

            double x = random.nextDouble() * 100;
            double y = random.nextDouble() * 100;
            node.setAttribute("x", x);
            node.setAttribute("y", y);
        }

        // Povezivanje svakog cvora sa prethodnim
        for (int i = 1; i < numNodes; i++) {
            int parent = random.nextInt(i);
            double randomLength = random.nextInt(15) + 1;
            graph.addEdge("Edge" + parent + "_" + i, "A " + parent, "A " + i)
                    .setAttribute("length", randomLength);
        }

        graph.setAttribute("ui.stylesheet", "node { fill-color: red; size: 20px; text-size: 20px; } edge { fill-color: black; text-size: 20px; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        graph.getEdgeSet().forEach(e -> e.setAttribute("label", "" + (int) e.getNumber("length")));

        graph.display();

        Thread.sleep(2000);

        // Definisanje ciljnog cvora
        Node targetNode = graph.getNode("A 14");

        // Pravim mapu kako bih u a zvezda mogao da koristim vrednosti heuristike koje su zadate na pocetku
        Map<Node, Double> heuristicMap = new HashMap<>();
        for (Node node : graph) {
            double heuristic = HillClimb.getHeuristicValue(node, targetNode);
            heuristicMap.put(node, heuristic);
            node.setAttribute("ui.label", node.getAttribute("ui.label") + " (h=" + String.format("%.2f", heuristic) + ")");
            System.out.println("Node " + node.getId() + " heuristic: " + heuristic);
        }


        JFrame prozor = new JFrame();
        prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prozor.setSize(400, 200);
        prozor.setLayout(new BorderLayout());

        String[] algoritmi = {"1.BFS", "2.DFS", "3.Best-First Search", "4.Hill Climbing", "5.A*"};
        JComboBox<String> opcije = new JComboBox<>(algoritmi);
        JButton runButton = new JButton("Run");

        runButton.addActionListener(e -> {
            int izbor = opcije.getSelectedIndex();
            new Thread(() -> {
                try {
                    switch (izbor) {
                        case 0:
                            System.out.println("Running Breadth-First Search (BFS):");
                            BFS.bfs(graph, graph.getNode("A 0"), graph.getNode("A 14"), 500, 500);
                            break;
                        case 1:
                            System.out.println("Running Depth-First Search (DFS):");
                            DFS.dfs(graph, graph.getNode("A 0"), graph.getNode("A 14"), 500, 500);
                            break;
                        case 2:
                            System.out.println("Running Best-First Search:");
                            BestFirstSearch.bestFirstSearch(graph, graph.getNode("A 0"), graph.getNode("A 14"), 500, 500);
                            break;
                        case 3:
                            System.out.println("Running Hill Climbing:");
                            HillClimb.hillClimbing(graph, graph.getNode("A 0"), graph.getNode("A 14"), 500, 500);
                            break;
                        case 4:
                            System.out.println("Running A* Search:");
                            Astar.astar(graph, graph.getNode("A 0"), graph.getNode("A 14"), heuristicMap, 500, 500);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        prozor.add(opcije, BorderLayout.NORTH);
        prozor.add(runButton, BorderLayout.SOUTH);
        prozor.setVisible(true);
    }
}
