package Algorithms;

import Main.Edge;
import Main.Log;
import Main.Node;

import java.util.*;

public class DepthFirstSearch extends SearchAlgorithm {



    public DepthFirstSearch() {
        addPseudocodeLine("Depth-first search");
        addPseudocodeLine("let S be a stack");
        addPseudocodeLine("label <font color='Red'>root</font> as <font color='Blue'>visited</font>");
        addPseudocodeLine("S.push(<font color='Red'>root</font>)");
        addPseudocodeLine("while S is not empty do");
        addPseudocodeLine("<font color='#FFC800'>N</font> = S.pop()", 1);
        addPseudocodeLine("if <font color='#FFC800'>N</font> is a <font color='#00B200'>goal</font> then", 1);
        addPseudocodeLine("return getPathList(<font color='#FFC800'>N</font>)", 2);
        addPseudocodeLine("if <font color='#FFC800'>N</font> is not <font color='Blue'>visited</font>", 1);
        addPseudocodeLine("for each <font color='#00B2B2'>neighbour</font> of <font color='#FFC800'>N</font>", 2);
        addPseudocodeLine("if <font color='#00B2B2'>neighbour</font> is not <font color='Gray'>explored</font>", 3);
        addPseudocodeLine("label <font color='#00B2B2'>neighbour</font> as <font color='Blue'>visited</font>", 4);
        addPseudocodeLine("set <font color='#00B2B2'>neighbour's</font> previous node to <font color='#FFC800'>N</font>", 4);
        addPseudocodeLine("S.push(<font color='#00B2B2'>neighbour</font>)", 4);
        addPseudocodeLine("label <font color='#FFC800'>N</font> as <font color='Gray'>explored</font>", 1);
    }

    @Override
    public List<AlgorithmState> runAlgorithm() {
        Log.logMessage("Depth-first search starting");

        //Setup line numbers for fixed lines
        int whileLoopCondition = 3;
        int forLoopCondition = 8;
        int afterForLoopBlock = 13;

        //Reset complete flag
        complete = false;
        pseudocodeLine = 0;

        //Create a new state list
        states = new ArrayList<>();



        //Init the stack
        Stack<Node> s = new Stack<>();
        //Add new state
        addCurrentState(s);

        //Set the root node as visited
        Node root = g.getRootNode();
        root.setRootNode(true);
        root.setVisited(true);
        addCurrentState(s);

        //Add the root node to the stack
        s.push(root);
        addCurrentState(s);

        //Loop until the stack is empty
        while (!s.isEmpty()) {
            //Set the pseudocode line to the start of the while loop
            pseudocodeLine = whileLoopCondition;
            addCurrentState(s);

            //pop from the stack
            Node node = s.pop();
            //Set the node as the currently selected node
            node.setCurrentNode(true);
            addCurrentState(s);

            //Increment node visited count
            nodesVisitedCount++;

            addCurrentState(s);
            //If the node is the goal node
            if (node.isGoalNode()) {
                goalNodeFound(node);    //Ignore result, used for unit testing
                //Stop DFS
                break;
            }

            pseudocodeLine++;
            addCurrentState(s);

            //Check if node has already been explored
            if (!node.isExplored()) {
                //Loop for each node neighbour of node
                for (Edge edge : g.getOutEdgesOfNode(node)) {
                    Node neighbour = edge.linkedNode;
                    neighbour.setConsideredNode(true);

                    //Set the pseudocode line to the start of the for loop
                    pseudocodeLine = forLoopCondition;
                    addCurrentState(s);

                    addCurrentState(s);
                    //todo either isExplored OR isVisited??
                    //Check if the neighbour has already been visited
                    if (!neighbour.isVisited()) {

                        //Set the neighbour as visited
                        neighbour.setVisited(true);
                        addCurrentState(s);


                        //Set the previous node of the neighbour as node
                        neighbour.previousNode = node;
                        addCurrentState(s);

                        //Add the neighbour to the stack
                        s.push(neighbour);
                        addCurrentState(s);
                    }

                    neighbour.setConsideredNode(false);
                }
            }

            //Set node as explored
            node.setExplored(true);

            //Set the pseudocode line to after the for loop block
            pseudocodeLine = afterForLoopBlock;
            addCurrentState(s);
            //Unset the node as currently selected node
            node.setCurrentNode(false);
        }

        Log.logMessage("Depth-first search completed");

        return states;
    }

    @Override
    public String toString() {
        return "Depth-First Search";
    }
}
