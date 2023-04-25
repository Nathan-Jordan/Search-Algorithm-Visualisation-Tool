package Algorithms;

import Main.Edge;
import Main.Log;
import Main.Node;

import java.util.*;

public class DijkstraSearch extends SearchAlgorithm{

    public DijkstraSearch() {
        addPseudocodeLine("Dijkstra's algorithm");
        addPseudocodeLine("let <font color='Blue'>S</font> be a set");
        addPseudocodeLine("let dist be a map of node-double");
        addPseudocodeLine("for each <font color='#00B2B2'>node</font> in the graph");
        addPseudocodeLine("dist[<font color='#00B2B2'>node</font>] = INFINITY", 1);
        addPseudocodeLine("<font color='Blue'>S.add(</font><font color='#00B2B2'>node</font><font color='Blue'>)</font>", 1);
        addPseudocodeLine("dist[<font color='Red'>root</font>] = 0");
        addPseudocodeLine("");
        addPseudocodeLine("while <font color='Blue'>S</font> is not empty do");
        addPseudocodeLine("<font color='#FFC800'>N</font> = node in <font color='Blue'>S</font> with min dist[<font color='#FFC800'>N</font>]", 1);
        addPseudocodeLine("if <font color='#FFC800'>N</font> is a <font color='#00B200'>goal</font> then", 1);
        addPseudocodeLine("return getPathList(<font color='#FFC800'>N</font>)", 2);
        addPseudocodeLine("remove <font color='#FFC800'>N</font> from <font color='Blue'>S</font>", 1);
        addPseudocodeLine("for each <font color='#00B2B2'>neighbour</font> of <font color='#FFC800'>N</font> that is still in <font color='Blue'>S</font>", 1);
        addPseudocodeLine("alt = dist[<font color='#FFC800'>N</font>] + d(<font color='#FFC800'>N</font>, <font color='#00B2B2'>neighbour</font>)*", 2);
        addPseudocodeLine("if alt &lt;< dist[<font color='#00B2B2'>neighbour</font>]", 2);
        addPseudocodeLine("set <font color='#00B2B2'>neighbour's</font> previous node to <font color='#FFC800'>N</font>", 3);
        addPseudocodeLine("dist[<font color='#00B2B2'>neighbour</font>] = alt", 3);
        addPseudocodeLine("label <font color='#FFC800'>N</font> as <font color='Gray'>explored</font>", 1);
        addPseudocodeLine("");
        addPseudocodeLine("");
        addPseudocodeLine("*d() distance between the 2 nodes.");
    }

    @Override
    public List<AlgorithmState> runAlgorithm() {
        Log.logMessage("Dijkstra's algorithm starting");

        //Setup line numbers for fixed lines
        int setupLoopCondition = 2;
        int whileLoopCondition = 7;
        int forLoopCondition = 12;
        int afterForLoopBlock = 17;

        //Reset complete flag
        complete = false;
        pseudocodeLine = 0;

        //Create a new state set
        states = new ArrayList<>();



        //Distance from the root to each other node
        Map<Node, Double> dist = new HashMap<>();
        //Set for unvisited nodes
        Set<Node> set = new HashSet<>();

        //For each node
        for (Node node : g.getNodes()) {
            node.setConsideredNode(true);
            //Set the pseudocode line to the start of the for loop
            pseudocodeLine = setupLoopCondition;
            addCurrentState();

            //Set distance to node to max - 1 (-1 because max is used later to get min dist)
            dist.put(node, Double.POSITIVE_INFINITY);
            addCurrentState();

            //Add node to the set
            set.add(node);
            node.setVisited(true);
            addCurrentState();
            node.setConsideredNode(false);
        }



        //Set root's distance to 0
        Node root = g.getRootNode();
        dist.put(root, 0.0);
        //Label root as visited
        root.setVisited(true);
        addCurrentState();

        //While set is not empty
        while (!set.isEmpty()) {
            //Set the pseudocode line to the start of the while loop
            pseudocodeLine = whileLoopCondition;
            addCurrentState();

            //Get node in set with the smallest distance
            double minDist = Double.POSITIVE_INFINITY;
            Node current = null;

            boolean firstIteration = true;

            for (Node node : set) {
                if (firstIteration) {
                    current = node;
                    firstIteration = false;
                }

                double distToNode = dist.get(node);
                if (distToNode < minDist) {
                    current = node;
                    minDist = distToNode;
                }
            }

            //Set the node as the currently selected node
            current.setCurrentNode(true);
            addCurrentState();

            //Increment node visited count
            nodesVisitedCount++;

            addCurrentState();
            if (current.isGoalNode()) {
                goalNodeFound(current);    //Ignore result, used for unit testing
                break;
            }


            //Remove node from the set
            set.remove(current);
            pseudocodeLine++;
            addCurrentState();

            //For each neighbour
            for (Edge edge : g.getOutEdgesOfNode(current)) {
                Node neighbour = edge.linkedNode;

                neighbour.setConsideredNode(true);

                //If the neighbour is still in the set
                if (set.contains(neighbour)) {
                    pseudocodeLine = forLoopCondition;
                    addCurrentState();

                    //Get the new distance to the neighbour
                    double distance = dist.get(current) + Math.hypot(current.x - neighbour.x, current.y - neighbour.y);
                    addCurrentState();

                    //If the new distance is smaller than the current distance
                    addCurrentState();
                    if (distance < dist.get(neighbour)) {

                        //Set the previous node of the neighbour to node
                        neighbour.previousNode = current;
                        addCurrentState();

                        //Update distance
                        dist.put(neighbour, distance);
                        addCurrentState();
                    }
                }

                neighbour.setConsideredNode(false);
            }

            //Set the node as explored
            current.setExplored(true);
            //Unset the node as the currently selected node
            pseudocodeLine = afterForLoopBlock;
            addCurrentState();
            current.setCurrentNode(false);
        }

        Log.logMessage("Dijkstra search completed");

        return states;
    }

    @Override
    public String toString() {
        return "Dijkstra's Algorithm";
    }
}
