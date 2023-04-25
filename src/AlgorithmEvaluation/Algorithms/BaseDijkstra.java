package AlgorithmEvaluation.Algorithms;

import Main.Edge;
import Main.Node;

import java.util.*;

public class BaseDijkstra extends BaseSearchAlgorithm {

    @Override
    public void runAlgorithm() {
        //Distance from the root to each other node
        Map<Node, Double> dist = new HashMap<>();
        //Set for unvisited nodes
        Set<Node> set = new HashSet<>();

        //For each node
        for (Node node : g.getNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);

            //Add node to the set
            set.add(node);
        }

        //Set root's distance to 0
        dist.put(g.getRootNode(), 0.0);


        //While set is not empty
        while (!set.isEmpty()) {
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

            //Increment node visited count
            nodesVisitedCount++;

            if (current.isGoalNode()) {
                break;
            }

            //Remove node from the set
            set.remove(current);


            //For each neighbour
            for (Edge edge : g.getOutEdgesOfNode(current)) {
                Node neighbour = edge.linkedNode;

                //If the neighbour is still in the set
                if (set.contains(neighbour)) {

                    //Get the new distance to the neighbour
                    double distance = dist.get(current) + Math.hypot(current.x - neighbour.x, current.y - neighbour.y);

                    //If the new distance is smaller than the current distance
                    if (distance < dist.get(neighbour)) {
                        //Set the previous node of the neighbour to node
                        neighbour.previousNode = current;

                        //Update distance
                        dist.put(neighbour, distance);
                    }
                }
            }
        }
    }
}
