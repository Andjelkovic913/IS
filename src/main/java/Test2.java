import algorithms.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Test2 {

    public static void main(String[] args) throws InterruptedException {
        // Initialize and configure the graph
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Graph");

        // Number of nodes
        int numNodes = 15;
        Random random = new Random();

        // Add nodes to the graph
        for (int i = 0; i < numNodes; i++) {
            Node node = graph.addNode("A " + i);
            node.setAttribute("ui.label", "A" + i);

            // Assign random coordinates to each node
            double x = random.nextDouble() * 100;  // Random X coordinate
            double y = random.nextDouble() * 100;  // Random Y coordinate
            node.setAttribute("x", x);  // Set X attribute
            node.setAttribute("y", y);  // Set Y attribute
        }

        // Connect each node to a random previous node to form a tree
        for (int i = 1; i < numNodes; i++) {
            int parent = random.nextInt(i);  // Ensure we only connect to previous nodes to keep it acyclic
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

        // Calculate and display heuristic values
        Node targetNode = graph.getNode("A 14");
        for (Node node : graph) {
            double heuristic = HillClimb.getHeuristicValue(node, targetNode);
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
                            Astar.astar(graph, graph.getNode("A 0"), graph.getNode("A 14"), 5, 500, 500);
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
