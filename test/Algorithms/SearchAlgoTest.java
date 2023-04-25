package Algorithms;

import Main.Edge;
import Main.Graph;
import Main.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class SearchAlgoTest {
    Graph graph;
    SearchAlgorithm searchAlgorithm;
    Random r = new Random();

    @BeforeEach
    public void init() {
        graph = new Graph();
        for (int i = 0; i < 5; i++) {
            graph.addNode(new Node( r.nextInt(100), r.nextInt(100)));
        }
    }

    @AfterEach
    public void teardown() {
        graph.clearNodes();
        System.out.println();
    }


    @Test
    public void exploresAllConnectedBFS() {
        createConnectedGraph();
        runAlgorithm(new BreadthFirstSearch());
        assertAllExplored();
    }
    @Test
    public void exploresAllButNonConnectedBFS() {
        createNonConnectedGraph();
        runAlgorithm(new BreadthFirstSearch());
        assertLastNodeNotExplored();
    }
    @Test
    public void exploresAllConnectedDFS() {
        createConnectedGraph();
        runAlgorithm(new DepthFirstSearch());
        assertAllExplored();
    }
    @Test
    public void exploresAllButNonConnectedDFS() {
        createNonConnectedGraph();
        runAlgorithm(new DepthFirstSearch());
        assertLastNodeNotExplored();
    }
    @Test
    public void exploresAllConnectedDijkstra() {
        createConnectedGraph();
        runAlgorithm(new DijkstraSearch());
        assertAllExplored();
    }
    @Test
    public void exploresAllConnectedAStar() {
        createConnectedGraph();
        runAlgorithm(new AStarSearch());
        assertAllExplored();
    }
    @Test
    public void exploresAllButNonConnectedAStar() {
        createNonConnectedGraph();
        runAlgorithm(new AStarSearch());
        assertLastNodeNotExplored();
    }

    @Test
    public void backTracking() {
        Node n0 = graph.getNode(0);
        Node n1 = graph.getNode(1);
        Node n2 = graph.getNode(2);
        Node n3 = graph.getNode(3);
        Node n4 = graph.getNode(4);

        graph.setRootNode(n0);
        n1.setGoalNode(true);
        graph.addEdge(n0, n2);
        graph.addEdge(n2, n3);
        graph.addEdge(n3, n1);
        graph.addEdge(n4, n2);
        graph.addEdge(n4, n3);

        searchAlgorithm = new BreadthFirstSearch();
        searchAlgorithm.init(graph, null, null);
        List<AlgorithmState> states = searchAlgorithm.runAlgorithm();


        int maxEdgesToGoal = graph.getNodes().size();
        boolean[] edgesBackTracked = new boolean[maxEdgesToGoal];
        StringBuilder resultantPath = new StringBuilder();

        for (AlgorithmState state : states) {
            //Only interested in the backtracking states
            if (state.complete) {

                //For the amount of edges between the goal and node - using i
                for (int i = 0; i < maxEdgesToGoal; i++) {
                    //If the relevant edge has been already identified
                    if (!edgesBackTracked[i]) {
                        Node node = graph.getNode(i);

                        //For each edge of the current node
                        for (Edge edge : state.edges.get(node)) {
                            if (edge.isBackTrack()) {
                                edgesBackTracked[i] = true; //Ignore this index from here on out

                                String str1 = node.nodeID + "-" + edge.linkedNode.nodeID;
                                String str2 = edge.linkedNode.nodeID + "-" + node.nodeID;
                                //If edge already exists do not add to the path list
                                if (!resultantPath.toString().contains(str1) &&
                                        !resultantPath.toString().contains(str2)) {
                                    resultantPath.append(str1);
                                }
                            }
                        }
                    }
                }
            }
        }

        assertEquals("1-32-30-2", resultantPath.toString());
    }


    private void runAlgorithm(SearchAlgorithm searchAlgo) {
        searchAlgorithm = searchAlgo;
        searchAlgorithm.init(graph, null, null);
        searchAlgorithm.runAlgorithm();
    }
    private void createConnectedGraph() {
        for (int i = 0; i < graph.getNodes().size() - 1; i++) {
            graph.addEdge(graph.getNode(i), graph.getNode(i + 1));
        }
        graph.setRootNode(graph.getNode(0));
    }
    private void createNonConnectedGraph() {
        //Last node is not connected
        for (int i = 0; i < graph.getNodes().size() - 2; i++) {
            graph.addEdge(graph.getNode(i), graph.getNode(i + 1));
        }
        graph.setRootNode(graph.getNode(0));
    }
    private void assertAllExplored() {
        boolean allExplored = true;

        for (Node node: graph.getNodes()) {
            if (!node.isExplored()) {
                allExplored = false;
                break;
            }
        }

        assertTrue(allExplored);
    }
    private void assertLastNodeNotExplored() {
        Node lastNode = graph.getNode(graph.getNodes().size() - 1);
        assertFalse(lastNode.isExplored());
    }
}
