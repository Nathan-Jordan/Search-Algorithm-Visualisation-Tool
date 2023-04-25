package AlgorithmEvaluation.Algorithms;

import Main.Edge;

import Main.Node;

import java.util.LinkedList;

import java.util.Queue;

public class BaseBFS extends BaseSearchAlgorithm {

    @Override
    public void runAlgorithm() {
        //Init the queue
        Queue<Node> q = new LinkedList<>();

        //Set the root node as visited
        Node root = g.getRootNode();
        root.setVisited(true);

        //Add the root node to the queue
        q.add(root);

        //Loop until the queue is empty
        while (!q.isEmpty()) {
            //Remove the first node in the queue
            Node node = q.remove();

            //Increment node visited count
            nodesVisitedCount++;

            //If the node is the goal node
            if (node.isGoalNode()) {
                break;
            }

            //Loop for each node neighbour of node
            for (Edge edge : g.getOutEdgesOfNode(node)) {
                Node neighbour = edge.linkedNode;

                //Check if the neighbour has already been visited
                if (!neighbour.isVisited()) {

                    //Set the neighbour as visited
                    neighbour.setVisited(true);

                    //Set the previous node of the neighbour as node
                    neighbour.previousNode = node;

                    //Add the neighbour to the queue
                    q.add(neighbour);
                }
            }
        }
    }
}
