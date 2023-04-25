package Algorithms;

import Main.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch extends SearchAlgorithm {


    public BreadthFirstSearch() {
        addPseudocodeLine("Breadth-first search");
        addPseudocodeLine("let Q be a queue");
        addPseudocodeLine("label <font color='Red'>root</font> as <font color='Blue'>visited</font>");
        addPseudocodeLine("Q.enqueue(<font color='Red'>root</font>)");
        addPseudocodeLine("while Q is not empty do");
        addPseudocodeLine("<font color='#FFC800'>N</font> = Q.dequeue()", 1);
        addPseudocodeLine("if <font color='#FFC800'>N</font> is a <font color='#00B200'>goal</font> then", 1);
        addPseudocodeLine("return getPathList(<font color='#FFC800'>N</font>)", 2);
        addPseudocodeLine("for each <font color='#00B2B2'>neighbour</font> of <font color='#FFC800'>N</font>", 1);
        addPseudocodeLine("if <font color='#00B2B2'>neighbour</font> is not <font color='Blue'>visited</font> then", 2);
        addPseudocodeLine("label <font color='#00B2B2'>neighbour</font> as <font color='Blue'>visited</font>", 3);
        addPseudocodeLine("set <font color='#00B2B2'>neighbour's</font> previous node to <font color='#FFC800'>N</font>", 3);
        addPseudocodeLine("Q.enqueue(<font color='#00B2B2'>neighbour</font>)", 3);
        addPseudocodeLine("label <font color='#FFC800'>N</font> as <font color='Gray'>explored</font>", 1);
    }

    @Override
    public List<AlgorithmState> runAlgorithm() {
        Log.logMessage("Breadth-first search starting");

        //Setup line numbers for fixed lines
        int whileLoopCondition = 3;
        int forLoopCondition = 7;
        int afterForLoopBlock = 12;

        //Reset complete flag
        complete = false;
        pseudocodeLine = 0;

        //Create a new state list
        states = new ArrayList<>();

        //Init the queue
        Queue<Node> q = new LinkedList<>();
        //Add new state
        addCurrentState(q);

        //Set the root node as visited
        Node root = g.getRootNode();
        root.setRootNode(true);
        root.setVisited(true);
        addCurrentState(q);

        //Add the root node to the queue
        q.add(root);
        addCurrentState(q);

        //Loop until the queue is empty
        while (!q.isEmpty()) {
            //Set the pseudocode line to the start of the while loop
            pseudocodeLine = whileLoopCondition;
            addCurrentState(q);

            //Remove the first node in the queue
            Node node = q.remove();
            //Set the node as the currently selected node
            node.setCurrentNode(true);
            addCurrentState(q);

            //Increment node visited count
            nodesVisitedCount++;

            addCurrentState(q);
            //If the node is the goal node
            if (node.isGoalNode()) {
                goalNodeFound(node);
                //Stop BFS
                break;
            }

            //Loop for each node neighbour of node
            for (Edge edge : g.getOutEdgesOfNode(node)) {
                Node neighbour = edge.linkedNode;
                neighbour.setConsideredNode(true);

                //Set the pseudocode line to the start of the for loop
                pseudocodeLine = forLoopCondition;
                addCurrentState(q);

                addCurrentState(q);
                //Check if the neighbour has already been explored
                if (!neighbour.isVisited()) {

                    //Set the neighbour as visited
                    neighbour.setVisited(true);
                    addCurrentState(q);

                    //Set the previous node of the neighbour as node
                    neighbour.previousNode = node;
                    addCurrentState(q);

                    //Add the neighbour to the queue
                    q.add(neighbour);
                    addCurrentState(q);
                }

                neighbour.setConsideredNode(false);
            }

            //Set the node as explored
            node.setExplored(true);
            //Set the pseudocode line to after the for loop block
            pseudocodeLine = afterForLoopBlock;
            addCurrentState(q);
            //Unset the node as currently selected node
            node.setCurrentNode(false);
        }

        Log.logMessage("Breadth-first search completed");

        return states;
    }

    @Override
    public String toString() {
        return "Breadth-First Search";
    }
}
