package Main;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {

    static int NUMERICAL = 0;
    static int ALPHABETICAL = 1;
    int labelType;

    Node rootNode;
    ArrayList<Node> goalNodes = new ArrayList<>();
    List<Node> nodes = new ArrayList<>();
    Map<Node, List<Edge>> edgesByNode = new HashMap<>();

    TreeSet<Integer> nodesRemoved = new TreeSet<>();


    public void addNode(Node node) {
        nodes.add(node);

        if (nodesRemoved.isEmpty()) {
            node.setNodeID(nodes.size() - 1);
        } else {
            node.setNodeID(nodesRemoved.first());
            nodesRemoved.remove(nodesRemoved.first());
        }

        node.nodeLabel = getNodeLabel(labelType, node.nodeID);

        //Add new empty list to the outEdges map
        edgesByNode.put(node, new ArrayList<>());
        Log.logMessage("Node " + node.nodeLabel + " created at (x, y): (" + node.x + ", " + node.y + ")");
    }

    public void addEdge(Node node1, Node node2) {
        if (node1 == node2) {
            Log.logMessage("Cannot create edge between nodes " + node1.nodeLabel + " and " + node2.nodeLabel);
            return;
        } else if (edgeExists(node1, node2)) {
            Log.logMessage("Edge exists between nodes " + node1.nodeLabel + " and " + node2.nodeLabel);
            return;
        }


        //NOT Directed
        edgesByNode.get(node1).add(new Edge(node2));
        edgesByNode.get(node2).add(new Edge(node1));

        Log.logMessage("Node edge created between nodes " + node1.nodeLabel + " and " + node2.nodeLabel);
    }

    public boolean edgeExists(Node node1, Node node2) {
        //NOT DIRECTED
        return edgesByNode.get(node1).contains(new Edge(node2)) || (edgesByNode.get(node2).contains(new Edge(node1)));
    }

    public void removeNode(Node nodeToRemove) {

        if (nodeToRemove.isRootNode()) {
            rootNode = null;
        }

        //NOT Directed
        //Remove nodeToRemove's edges from map
        edgesByNode.remove(nodeToRemove);
        nodes.remove(nodeToRemove);

        //Node labelling
        nodesRemoved.add(nodeToRemove.nodeID);

        //Remove edges between any other nodeToRemove and the removed nodeToRemove
        for (Node node : edgesByNode.keySet()) {
            edgesByNode.get(node).remove(new Edge(nodeToRemove));
        }

        Log.logMessage("Node " + nodeToRemove.nodeLabel + " deleted");
    }

    public void resetNodes() {
        for (Node node : nodes) {
            node.setVisited(false);
            node.setExplored(false);
            node.setCurrentNode(false);
            node.setConsideredNode(false);
            node.previousNode = null;
        }

        for (List<Edge> edges : edgesByNode.values()) {
            for (Edge edge : edges) {
                edge.setBackTrack(false);
            }
        }

        Log.logMessage("Nodes reset");
    }

    public void clearNodes() {
        rootNode = null;
        nodes.clear();
        edgesByNode.clear();
        nodesRemoved.clear();
        Log.logMessage("Nodes and edges cleared");
    }

    public List<Node> cloneNodes() {
        //Cloned nodes for use for AlgorithmState
        //when updating the node states in the algorithm we do not want to change
        //the actual nodes' flags until visualisation

        List<Node> returnedNodes = new ArrayList<>();

        for (Node node : nodes) {
            returnedNodes.add(node.clone());
        }

        return returnedNodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void updateNodes(List<Node> nodesState) {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).updateFlags(nodesState.get(i));
            nodes.get(i).previousNode = nodesState.get(i).previousNode;
        }
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public Map<Node, List<Edge>> cloneEdges() {
        Map<Node, List<Edge>> returnedEdges = new HashMap<>();

        for (Node node : edgesByNode.keySet()) {
            returnedEdges.put(node, new ArrayList<>());

            for (Edge edge : edgesByNode.get(node)) {
                returnedEdges.get(node).add(edge.clone());
            }
        }

        return returnedEdges;
    }

    public Map<Node, List<Edge>> getEdges() {
        return edgesByNode;
    }

    public void updateEdges(Map<Node, List<Edge>> edgesOfNodeState) {
        for (Map.Entry<Node, List<Edge>> entry : edgesOfNodeState.entrySet()) {

            //Edges of the current key (node)
            List<Edge> edgesOfNode = edgesByNode.get(entry.getKey());

            for (int i = 0; i < edgesOfNode.size(); i++) {
                //Edges of the current key (node) in the state
                List<Edge> stateEdges = entry.getValue();

                boolean t = stateEdges.get(i).isBackTrack();
                //Update edge from the value of the state
                edgesOfNode.get(i).setBackTrack(t);
            }
        }
    }

    public List<Edge> getOutEdgesOfNode(Node node) {
        return edgesByNode.get(node);
    }

    public void removeEdgesBetweenNodes(Node start, Node end) {
        edgesByNode.get(start).remove(new Edge(end));
        edgesByNode.get(end).remove(new Edge(start));

        if (start.previousNode == end) {
            start.previousNode = null;
        }
        if (end.previousNode == start) {
            end.previousNode = null;
        }

        Log.logMessage("Edge deleted between nodes " + start + " and " + end);
    }


    public void changeLabelType(int labelType) {
        this.labelType = labelType;

        for (Node node : nodes) {
            node.nodeLabel = getNodeLabel(labelType, node.nodeID);
        }

        Log.logMessage("Node labels changed to " + (labelType == ALPHABETICAL ? "alphabetical" : "numerical"));
    }

    private String getNodeLabel(int labelType, int nodeID) {
        String nodeLabel;

        if (labelType == ALPHABETICAL) {
            if (nodeID < 26) {
                nodeLabel = String.valueOf((char) (nodeID + 'A'));

            } else {
                StringBuilder sb = new StringBuilder();
                while (nodeID >= 26) {
                    int letter = nodeID % 26;
                    sb.insert(0, (char) (letter + 'A'));

                    //divide by 26 since I am using a base-26 numbering system
                    nodeID = (nodeID - letter) / 26;
                }

                //Insert last letter
                sb.insert(0, (char) (nodeID + 'A' - 1));
                nodeLabel = sb.toString();
            }
        } else {
            nodeLabel = String.valueOf(nodeID);
        }

        return nodeLabel;
    }

    public void generateRandomGraph(Dimension size, int numberOfNodes) {
        //Generates a random graph and adds edges based on Delaunay's triangulation algorithm
        Log.logMessage("Auto generating graph with " + numberOfNodes + " nodes");

        Random r = new Random();

        //Attempts to add 'numNodes' of nodes
        for (int i = 0; i < numberOfNodes; i++) {
            int x = r.nextInt(size.width - Node.diameter - View.padding) + Node.radius;
            int y = r.nextInt(size.height - Node.diameter - View.steppingPanelHeight) + Node.radius;
            Node node = new Node(x, y);


            //it attempts to choose a random position, if that position is overlapping it will try again up to
            //maxIteration times, if it reaches this it will stop adding more nodes
            int iterations = 0;
            int maxIterations = 5000;

            //Re-place node randomly
            while (isNodeOverlapping(node) && iterations <= maxIterations) {
                iterations++;
                x = r.nextInt(size.width - Node.diameter - View.padding) + Node.radius;
                y = r.nextInt(size.height - Node.diameter - View.steppingPanelHeight) + Node.radius;
                node = new Node(x, y);
            }

            //If max iterations reached
            if (iterations > maxIterations) {
                break;
            }

            addNode(node);
        }

        //Resources used to help implement Delaunay Triangulation
        //https://www.youtube.com/watch?v=4ySSsESzw2Y
        //https://en.wikipedia.org/wiki/Bowyer%E2%80%93Watson_algorithm

        //Create super triangle (triangle that encloses all points)
        int inc = 10000000;

        ArrayList<Node> superTriangle = new ArrayList<>();
        superTriangle.add(new Node(-inc, inc));
        superTriangle.add(new Node(inc, inc));
        superTriangle.add(new Node(size.width / 2, -inc));

        //Add super triangle to graph
        for (Node n : superTriangle) {
            addNode(n);
        }


        //Initialise the triangles list and add the super triangle to it
        ArrayList<List<Node>> triangles = new ArrayList<>();
        triangles.add(superTriangle);


        //For each node
        for (Node node : nodes) {
            ArrayList<List<Node>> badTriangles = new ArrayList<>();

            //Identify bad triangles
            //A bad triangle is a triangle whose circumcircle contains the current node
            for (List<Node> triangle : triangles) {
                Node a = triangle.get(0);
                Node b = triangle.get(1);
                Node c = triangle.get(2);

                //Circumcenter maths from:
                //https://www.omnicalculator.com/math/circumcenter-of-a-triangle
                double x1 = a.x;
                double y1 = a.y;
                double x2 = b.x;
                double y2 = b.y;
                double x3 = c.x;
                double y3 = c.y;

                double t = (x1 * x1 + y1 * y1 - x2 * x2 - y2 * y2);
                double u = x1 * x1 + y1 * y1 - x3 * x3 - y3 * y3;
                double J = (x1 - x2) * (y1 - y3) - (x1 - x3) * (y1 - y2);
                //Circumcenter x and y
                double x = (-(y1 - y2) * u + (y1 - y3) * t) / (2 * J);
                double y = ((x1 - x2) * u - (x1 - x3) * t) / (2 * J);

                //Radius of circumcircle is the distance from any node to the circumcenter
                double radius = Math.hypot(a.x - x, a.y - y);

                //Distance between the current node and the circumcenter
                double distanceToCircumcenter = Math.hypot(node.x - x, node.y - y);

                //If the node is inside the radius of the triangle
                if (distanceToCircumcenter < radius) {
                    badTriangles.add(triangle);
                }
            }


            //Find the boundary of the polygonal hole (the polygon that surrounds the node)
            //Only edges which are unique are added (completely ignore duplicate edges)
            Set<List<Node>> polygon = new HashSet<>();
            for (List<Node> triangle : badTriangles) {
                Node a = triangle.get(0);
                Node b = triangle.get(1);
                Node c = triangle.get(2);

                //Sort edges by node ID (remove issues like a-b and b-a)
                List<List<Node>> sortedEdges = new ArrayList<>();
                sortedEdges.add(getSortedEdge(a, b));
                sortedEdges.add(getSortedEdge(b, c));
                sortedEdges.add(getSortedEdge(c, a));

                for (List<Node> in : sortedEdges) {
                    if (!polygon.add(in)) {
                        polygon.remove(in);
                    }
                }
            }


            //Remove bad triangles from the triangles list
            for (List<Node> triangle : badTriangles) {
                triangles.remove(triangle);
            }


            //Add new triangles for each edge in the polygon
            //since each edge contains 2 points adding the current node as the third
            //point creates the new triangle
            for (List<Node> edge : polygon) {
                Node b = edge.get(0);
                Node c = edge.get(1);

                ArrayList<Node> tri = new ArrayList<>();
                tri.add(node);
                tri.add(b);
                tri.add(c);
                triangles.add(tri);
            }
        }


        //Add edges from the resultant triangles list (providing they don't already exist)
        for (List<Node> triangle : triangles) {
            Node a = triangle.get(0);
            Node b = triangle.get(1);
            Node c = triangle.get(2);

            //Ignore if any point is part of the super triangle
            if (a.nodeID >= nodes.size() - 3 ||
                    b.nodeID >= nodes.size() - 3 ||
                    c.nodeID >= nodes.size() - 3) {
                continue;
            }

            //Adds the edge if it does not exist (checks a-b and b-a)
            if (!edgeExists(a, b))
                addEdge(a, b);
            if (!edgeExists(b, c))
                addEdge(b, c);
            if (!edgeExists(c, a))
                addEdge(c, a);
        }


        //Remove the super triangle
        removeNode(superTriangle.get(0));
        removeNode(superTriangle.get(1));
        removeNode(superTriangle.get(2));
    }

    private static ArrayList<Node> getSortedEdge(Node a, Node b) {
        ArrayList<Node> sortedEdge = new ArrayList<>();

        //Ensures the smallest nodeID is the 'start' of the edge (avoids checking both a-b and b-a)
        if (a.nodeID < b.nodeID) {
            sortedEdge.add(a);
            sortedEdge.add(b);
        } else {
            sortedEdge.add(b);
            sortedEdge.add(a);
        }

        return sortedEdge;
    }

    boolean isNodeOverlapping(Node n) {
        //Returns true if a node is within the diameter * 2 of any other node, false if not

        for (Node node : nodes) {
            if (node != n) {
                double d = Math.sqrt(Math.pow((node.x - n.x), 2) + Math.pow(((node.y - n.y)), 2));
                if (d < Node.diameter * 2) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setGoalNode(Node node) {
        if (rootNode == node) {
            Log.logMessage("Node " + node + " unset as root node");
            node.setRootNode(false);
            rootNode = null;
        }

        Log.logMessage("Node " + node + " set as goal node");
        node.setGoalNode(true);
        goalNodes.add(node);
    }

    public void setRootNode(Node node) {
        if (rootNode != null) {
            Log.logMessage("Node " + rootNode + " unset as root node");
            rootNode.setRootNode(false);
            rootNode = null;
        }

        if (node.isGoalNode()) {
            node.setGoalNode(false);
            Log.logMessage("Node " + node + " unset as goal node");
            goalNodes.remove(node);
        }

        rootNode = node;
        rootNode.setRootNode(true);
        Log.logMessage("Node " + node + " set as root node");
    }

    public Node getRootNode() {
        return rootNode;
    }

    public ArrayList<Node> getGoalNodes() {
        return goalNodes;
    }
}
