package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    Graph g;

    @BeforeEach
    public void init() {
        Random r = new Random();
        g = new Graph();
        for (int i = 0; i < r.nextInt(8) + 4; i++) {
            g.addNode(new Node(10, 10));
        }
    }

    @AfterEach
    public void teardown() {
        g.clearNodes();
        System.out.println();
    }


    @Test
    void nodeGetsAddedToNodesList() {
        int initSize = g.nodes.size();
        g.addNode(new Node(10, 10));

        assertEquals(initSize + 1, g.nodes.size());
    }

    @Test
    void emptyEdgesAddedWhenNodeCreated() {
        int initSize = g.edgesByNode.size();
        g.addNode(new Node(10, 10));

        assertEquals(initSize + 1, g.edgesByNode.size());
    }

    @Test
    void addNodeAlphabetical() {
        g.clearNodes();
        g.changeLabelType(Graph.ALPHABETICAL);
        g.addNode(new Node(10, 10));

        String nodeLabel = g.getNode(0).nodeLabel;

        assertEquals(nodeLabel, "A");
    }

    @Test
    void addNodeNumerical() {
        g.clearNodes();
        g.changeLabelType(Graph.NUMERICAL);
        g.addNode(new Node(10, 10));

        String nodeLabel = g.getNode(0).nodeLabel;

        assertEquals(nodeLabel, "0");
    }

    @Test
    void addEdgeUndirected() {
        Node n1 = g.nodes.get(0);
        Node n2 = g.nodes.get(g.nodes.size() - 1);

        g.addEdge(n1, n2);

        assertEquals(g.getOutEdgesOfNode(n1).size(), 1);
    }

    @Test
    void addEdgeToSameNode() {
        Node n1 = new Node(10, 10);
        g.addNode(n1);
        g.addEdge(n1, n1);

        assertEquals(g.getOutEdgesOfNode(n1), new ArrayList<Edge>());
    }

    @Test
    void addEdgeThatAlreadyExists() {
        Node n1 = g.nodes.get(0);
        Node n2 = g.nodes.get(g.nodes.size() - 1);

        g.addEdge(n1, n2);
        List<Edge> node1inEdge = g.getOutEdgesOfNode(n1);
        g.addEdge(n2, n1);

        assertEquals(node1inEdge, g.getOutEdgesOfNode(n1));
    }

    @Test
    void edgeExistsTrueNotDirected() {
        Node n1 = new Node(10, 10);
        Node n2 = new Node(10, 10);

        g.addNode(n1);
        g.addNode(n2);
        g.addEdge(n1, n2);

        assertTrue(g.edgeExists(n1, n2) && g.edgeExists(n2, n1));
    }

    @Test
    void removeNode() {
        int initSize = g.nodes.size();
        g.removeNode(g.nodes.get(0));

        assertEquals(initSize - 1, g.nodes.size());
    }

    @Test
    void removeNodeRemovesAllRelevantEdges() {
        int initSize = g.edgesByNode.size();

        Node n1 = g.nodes.get(0);
        Node n2 = g.nodes.get(g.nodes.size() - 1);
        Node n3 = g.nodes.get(g.nodes.size() - 2);

        g.addEdge(n1, n2);
        g.addEdge(n2, n3);
        g.addEdge(n3, n1);
        g.removeNode(n2);

        assertTrue(initSize - 1 == g.edgesByNode.size() &&
                !g.getOutEdgesOfNode(n1).contains(new Edge(n2)) &&
                !g.getOutEdgesOfNode(n3).contains(new Edge(n2)));
    }

    @Test
    void resetNode() {
        Node node = g.nodes.get(0);
        node.setExplored(true);
        node.setVisited(true);
        g.resetNodes();

        assertFalse(node.isExplored() || node.isVisited());
    }

    @Test
    void clearNodes() {
        g.clearNodes();

        assertEquals(g.nodes, new ArrayList<Node>());
    }

    @Test
    void cloneNodes() {
        List<Node> nodes = g.getNodes();
        List<Node> clonedNodes = g.cloneNodes();

        assertNotSame(nodes, clonedNodes);
    }

    @Test
    void updateNodes() {
        Node n1 = getNodeWithFlags();
        Node n2 = getNodeWithFlags();

        List<Node> nodeStates = new ArrayList<>();
        nodeStates.add(n1);
        nodeStates.add(n2);


        Graph graph = new Graph();
        Node n3 = new Node(10, 10);
        Node n4 = new Node(10, 10);

        graph.addNode(n3);
        graph.addNode(n4);

        graph.updateNodes(nodeStates);
        //Since nodes are compared by their index value n1 and n2 will update n3 and n4 respectively
        //even if they are created separately
        assertTrue(allNodeFlagsTrue(graph.getNodes()));
    }

    private Node getNodeWithFlags() {
        Node node = new Node(10, 10);
        node.setCurrentNode(true);
        node.setVisited(true);
        node.setExplored(true);
        return node;
    }

    private boolean allNodeFlagsTrue(List<Node> nodes) {
        for (Node n: nodes) {
            if (!n.isVisited() &&
                    !n.isCurrentNode()&&
                    !n.isExplored()) {
                return false;
            }
        }
        return true;
    }

    @Test
    void cloneEdges() {
        g.addEdge(g.getNode(0), g.getNode(3));
        g.addEdge(g.getNode(1), g.getNode(2));

        Map<Node, List<Edge>> edges = g.getEdges();
        Map<Node, List<Edge>> edgesCloned = g.cloneEdges();

        assertNotSame(edges, edgesCloned);
    }

    @Test
    void updateEdges() {
        g.clearNodes();
        Node n1 = new Node(10, 10);
        Node n2 = new Node(10, 10);
        Node n3 = new Node(10, 10);

        g.addNode(n1);g.addNode(n2);g.addNode(n3);
        g.addEdge(n1, n2);
        g.addEdge(n2, n3);

        //Set all edges as backtrack (simulating a path from n1(root) to n3(goal) n1-n2-n3)
        for (Node node : g.getNodes()) {
            for (Edge edge : g.getOutEdgesOfNode(node)) {
                edge.setBackTrack(true);
            }
        }

        Graph graph = new Graph();
        Node n4 = new Node(10, 10);
        Node n5 = new Node(10, 10);
        Node n6 = new Node(10, 10);

        graph.addNode(n4);graph.addNode(n5);graph.addNode(n6);
        graph.addEdge(n4, n5);
        graph.addEdge(n5, n6);

        graph.updateEdges(g.getEdges());
        assertTrue(allEdgesBacktracked(graph.getEdges()));
    }

    private boolean allEdgesBacktracked(Map<Node, List<Edge>> edges) {
        for (List<Edge> entry : edges.values()) {
            for (Edge edge : entry) {
                if (!edge.isBackTrack()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Test
    void changeLabelTypeAlphabetical() {
        String n1Label = g.getNode(0).nodeLabel;
        g.changeLabelType(Graph.ALPHABETICAL);

        assertNotEquals(n1Label, g.getNode(0).nodeLabel);
    }

    @Test
    void changeLabelTypeNumerical() {
        g.changeLabelType(Graph.ALPHABETICAL);
        String n1Label = g.getNode(0).nodeLabel;
        g.changeLabelType(Graph.NUMERICAL);

        assertNotEquals(n1Label, g.getNode(0).nodeLabel);
    }

    @Test
    void generateRandomGraphEulersFormula() {
        //Less than or equal to Euler's formula, since we do not know how many
        //outside edges there are we check if it is less than Euler's formula
        clearNodes();
        g.generateRandomGraph(new Dimension(400, 600), 10);
        Set<List<Node>> uniqueEdges = new HashSet<>();

        for (Map.Entry<Node, List<Edge>> entry : g.edgesByNode.entrySet()) {
            Node node = entry.getKey();
            List<Edge> edges = entry.getValue();

            for (Edge edge : edges) {
                //If the inverse exists do not add
                //Can add same edge since it does not increase the total count
                if (!uniqueEdges.contains(Arrays.asList(edge.linkedNode, node))) {
                    uniqueEdges.add(Arrays.asList(node, edge.linkedNode));
                }
            }
        }

        assertTrue(uniqueEdges.size() <= 3 * g.nodes.size() - 6);
    }

    @Test
    void generateRandomGraphTriangleEulersFormula() {
        //Euler's formula ensuring that the outer face is a triangle, since without we do not know how many
        //extra edges there are, if we know it is a triangle then we know it is equal to Euler's formula
        clearNodes();

        g.addNode(new Node(0, 0));
        g.addNode(new Node(0, 20000));
        g.addNode(new Node(20000, 0));

        g.generateRandomGraph(new Dimension(400, 600), 10);
        Set<List<Node>> uniqueEdges = new HashSet<>();

        for (Map.Entry<Node, List<Edge>> entry : g.edgesByNode.entrySet()) {
            Node node = entry.getKey();
            List<Edge> edges = entry.getValue();

            for (Edge edge : edges) {
                //If the inverse exists do not add
                //Can add same edge since it does not increase the total count
                if (!uniqueEdges.contains(Arrays.asList(edge.linkedNode, node))) {
                    uniqueEdges.add(Arrays.asList(node, edge.linkedNode));
                }
            }
        }

        assertEquals(uniqueEdges.size(), 3 * g.nodes.size() - 6);
    }

    @Test
    void isNodeOverlappingTrue() {
        assertTrue(g.isNodeOverlapping(g.nodes.get(0)));
    }

    @Test
    void isNodeOverlappingFalse() {
        Node node = new Node(100, 100);
        g.addNode(node);
        assertFalse(g.isNodeOverlapping(node));
    }


    @Test
    void getRootNode() {
        Node node = g.getNode(0);
        g.setRootNode(node);

        Node rootNode = g.getRootNode();
        assertEquals(node, rootNode);
    }

    @Test
    void getRootNodeNull() {
        assertNull(g.getRootNode());
    }
}