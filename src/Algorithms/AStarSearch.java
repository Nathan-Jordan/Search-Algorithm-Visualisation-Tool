package Algorithms;

import Main.Edge;
import Main.Log;
import Main.Node;

import java.util.*;
import java.util.List;

public class AStarSearch extends SearchAlgorithm {


    public AStarSearch() {
        addPseudocodeLine("A* Search");
        addPseudocodeLine("let <font color='Blue'>S</font> be a set");
        addPseudocodeLine("let dist and fScore be a map");
        addPseudocodeLine("populate maps by mapping all nodes to INFINITY");
        addPseudocodeLine("<font color='Blue'>S.add(</font><font color='Red'>root</font><font color='Blue'>)</font>");
        addPseudocodeLine("dist[<font color='Red'>root</font>] = 0");
        addPseudocodeLine("fScore[<font color='Red'>root</font>] = h(<font color='Red'>root</font>)*"); // h() is the heuristic function
        addPseudocodeLine("while <font color='Blue'>S</font> is not empty do");
        addPseudocodeLine("<font color='#FFC800'>N</font> = node in <font color='Blue'>S</font> with the lowest fScore", 1);
        addPseudocodeLine("if <font color='#FFC800'>N</font> is a <font color='#00B200'>goal</font> then", 1);
        addPseudocodeLine("return getPathList(<font color='#FFC800'>N</font>)", 2);
        addPseudocodeLine("remove <font color='#FFC800'>N</font> from <font color='Blue'>S</font>", 1);
        addPseudocodeLine("for each <font color='#00B2B2'>neighbour</font> of <font color='#FFC800'>N</font>", 1);
        addPseudocodeLine("score = dist[<font color='#FFC800'>N</font>] + d(<font color='#FFC800'>N</font>, <font color='#00B2B2'>neighbour</font>)**", 2);
        addPseudocodeLine("if score &lt;< dist[<font color='#00B2B2'>neighbour</font>] then", 2);
        addPseudocodeLine("set <font color='#00B2B2'>neighbour's</font> previous node to <font color='#FFC800'>N</font>", 3);
        addPseudocodeLine("dist[<font color='#00B2B2'>neighbour</font>] = score", 3);
        addPseudocodeLine("fScore[<font color='#00B2B2'>neighbour</font>] = score + h(<font color='#00B2B2'>neighbour</font>)*", 3);
        addPseudocodeLine("if <font color='#00B2B2'>neighbour</font> is not in <font color='Blue'>S</font> then", 3);
        addPseudocodeLine("<font color='Blue'>S.add(</font><font color='#00B2B2'>neighbour</font><font color='Blue'>)</font>", 4);
        addPseudocodeLine("label <font color='#FFC800'>N</font> as <font color='Gray'>explored</font>", 1);
        addPseudocodeLine("");
        addPseudocodeLine("");
        addPseudocodeLine("*h() heuristic (distance to the closest <font color='#00B200'>goal</font>).");
        addPseudocodeLine("**d() distance between the 2 nodes.");
    }

    private double heuristic(Node node) {
        if (g.getGoalNodes().isEmpty()) {
            return 1;
        }

        double minDistance = Double.POSITIVE_INFINITY;

        for (Node goal : g.getGoalNodes()) {
            double distance = Math.hypot(node.x - goal.x, node.y - goal.y);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    @Override
    public List<AlgorithmState> runAlgorithm() {
        Log.logMessage("A* search starting");

        //Setup line numbers for fixed lines
        int whileLoopCondition = 6;
        int forLoopCondition = 11;
        int afterForLoopBlock = 19;

        //Reset complete flag
        complete = false;
        pseudocodeLine = 0;

        //Create a new state list
        states = new ArrayList<>();


        Set<Node> open = new HashSet<>();
        addCurrentState(open);
        HashMap<Node, Double> dist = new HashMap<>();
        HashMap<Node, Double> fScore = new HashMap<>();
        addCurrentState(open);

        for (Node node : g.getNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
            fScore.put(node, Double.POSITIVE_INFINITY);
        }
        addCurrentState(open);


        open.add(g.getRootNode());
        addCurrentState(open);
        dist.put(g.getRootNode(), 0.0);
        addCurrentState(open);
        fScore.put(g.getRootNode(), heuristic(g.getRootNode()));
        addCurrentState(open);



        while (!open.isEmpty()) {
            //Set the pseudocode line to the start of the while loop
            pseudocodeLine = whileLoopCondition;
            addCurrentState(open);

            //Get node in 'open' with the smallest fScore
            double minScore = Double.POSITIVE_INFINITY;
            Node current = null;

            for (Node node : open) {
                double score = fScore.get(node);
                if (score < minScore) {
                    current = node;
                    minScore = score;
                }
            }

            //Set the node as the currently selected node
            current.setCurrentNode(true);
            addCurrentState(open);

            addCurrentState(open);
            if (current.isGoalNode()) {
                goalNodeFound(current);    //Ignore result, used for unit testing
                break;
            }

            open.remove(current);
            pseudocodeLine++;
            addCurrentState(open);


            for (Edge edge : g.getOutEdgesOfNode(current)) {
                Node neighbour = edge.linkedNode;
                neighbour.setConsideredNode(true);

                //Set the pseudocode line to the start of the for loop
                pseudocodeLine = forLoopCondition;
                addCurrentState(open);

                double distance = dist.get(current) + Math.hypot(current.x - neighbour.x, current.y - neighbour.y);
                addCurrentState(open);

                addCurrentState(open);
                if (distance < dist.get(neighbour)) {
                    neighbour.previousNode = current;
                    addCurrentState(open);

                    dist.put(neighbour, distance);
                    addCurrentState(open);

                    fScore.put(neighbour, distance + heuristic(neighbour));
                    addCurrentState(open);

                    if (!open.contains(neighbour)) {
                        addCurrentState(open);

                        open.add(neighbour);
                        neighbour.setVisited(true);
                        addCurrentState(open);
                    }
                }

                neighbour.setConsideredNode(false);
            }

            //Set the node as explored
            current.setExplored(true);

            //Set the pseudocode line to after the for loop block
            pseudocodeLine = afterForLoopBlock;
            addCurrentState(open);
            //Unset the node as the currently selected node
            current.setCurrentNode(false);
        }


        Log.logMessage("A* search completed");

        return states;
    }

    @Override
    public String toString() {
        return "Breadth-First Search";
    }
}
