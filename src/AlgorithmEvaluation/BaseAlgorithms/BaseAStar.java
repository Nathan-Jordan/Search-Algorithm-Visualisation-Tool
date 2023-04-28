package AlgorithmEvaluation.BaseAlgorithms;

import Main.Edge;
import Main.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BaseAStar extends BaseSearchAlgorithm {

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
    public void runAlgorithm() {
        Set<Node> open = new HashSet<>();
        //Distance from the root to each other node
        HashMap<Node, Double> dist = new HashMap<>();
        //Estimated distance from root to a goal through the node (dist + heuristic())
        HashMap<Node, Double> fScore = new HashMap<>();

        //For each node
        for (Node node : g.getNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
            fScore.put(node, Double.POSITIVE_INFINITY);
        }

        open.add(g.getRootNode());
        dist.put(g.getRootNode(), 0.0);
        fScore.put(g.getRootNode(), heuristic(g.getRootNode()));


        while (!open.isEmpty()) {
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

            //Increment node visited count
            nodesVisitedCount++;

            if (current.isGoalNode()) {
                break;
            }

            //Remove node from the list
            open.remove(current);


            //For each neighbour
            for (Edge edge : g.getOutEdgesOfNode(current)) {
                Node neighbour = edge.linkedNode;

                //Get the new distance to the neighbour
                double distance = dist.get(current) + Math.hypot(current.x - neighbour.x, current.y - neighbour.y);

                //If the new distance is smaller than the current distance
                if (distance < dist.get(neighbour)) {
                    //Set the previous node of the neighbour to node
                    neighbour.previousNode = current;

                    //Update distance
                    dist.put(neighbour, distance);
                    fScore.put(neighbour, distance + heuristic(neighbour));

                    if (!open.contains(neighbour)) {
                        open.add(neighbour);
                    }
                }
            }
        }
    }
}
