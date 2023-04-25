package AlgorithmEvaluation.Algorithms;

import Main.Edge;
import Main.Node;

import java.util.Stack;

public class BaseDFS extends BaseSearchAlgorithm {

    @Override
    public void runAlgorithm() {
        //Init the stack
        Stack<Node> s = new Stack<>();

        //Set the root node as visited
        Node root = g.getRootNode();
        root.setVisited(true);

        //Add the root node to the stack
        s.push(root);

        //Loop until the stack is empty
        while (!s.isEmpty()) {
            //pop from the stack
            Node node = s.pop();

            //Increment node visited count
            nodesVisitedCount++;

            //If the node is the goal node
            if (node.isGoalNode()) {
                break;
            }

            //Check if node has already been explored
            if (!node.isExplored()) {
                //Loop for each node neighbour of node
                for (Edge edge : g.getOutEdgesOfNode(node)) {
                    Node neighbour = edge.linkedNode;

                    //Check if the neighbour has already been visited
                    if (!neighbour.isVisited()) {

                        //Set the neighbour as visited
                        neighbour.setVisited(true);

                        //Set the previous node of the neighbour as node
                        neighbour.previousNode = node;

                        //Add the neighbour to the stack
                        s.push(neighbour);
                    }
                }
            }

            //Set node as explored
            node.setExplored(true);
        }
    }
}
