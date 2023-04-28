package Main;

import java.awt.*;
import java.util.ArrayList;

import static Main.View.*;

public class Viewport {

    Graph graph;

    public Viewport(Graph graph) {
        this.graph = graph;
    }

    public Point getBoundaryPointOfNode(Node node1, Node node2) {
        //Returns the boundary point of node1 when linked to node2
        //which is the point on node1's circle which faces towards node2

        double dx = node1.x - node2.x;
        double dy = node1.y - node2.y;
        double dL = Math.hypot(dx, dy);

        double nx = dx/dL;
        double ny = dy/dL;

        double x = node1.x - (Node.radius * nx);
        double y = node1.y - (Node.radius * ny);

        return new Point((int) x, (int) y);
    }

    public int[][] drawArrow(Node currentNode, int midX, int midY) {
        //http://www.java2s.com/example/java/2d-graphics/draw-arrow.html
        int headAngle = 50;
        int headLength = 15;
        int x = currentNode.x;
        int y = currentNode.y;

        double offset = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y - midY, x - midX);
        int[] xs = { midX + (int) (headLength * Math.cos(angle + offset)), midX,
                midX + (int) (headLength * Math.cos(angle - offset)) };
        int[] ys = { midY + (int) (headLength * Math.sin(angle + offset)), midY,
                midY + (int) (headLength * Math.sin(angle - offset)) };

        return new int[][]{xs, ys};
    }

    public Node getNodeFromMouse(Point mouse, float divider) {
        //Divider is used to identify whether to return a node
        //either exactly in the node's circle (2)
        //or at least 2 times away from the node (1) (as to not overlap nodes when creating)

        for (int i = graph.getNodes().size() - 1; i >= 0; i--) {
            Node node = graph.getNode(i);

            double a = Math.pow((mouse.x - node.x), 2);
            double b = Math.pow((mouse.y - node.y), 2);

            //If cursor position is inside a node (circle)
            if (a + b < Math.pow(Node.diameter / divider, 2)) {
                return node;
            }
        }

        return null;
    }

    public Node getNodeFromMouse(Point mouse) {
        //Gets node from mouse exactly in the node's circle (divider = 2)
        return getNodeFromMouse(mouse, 2);
    }

    public ArrayList<Node> getEdgeFromMouse(Point mouse) {
        double threshold = 8;
        double closestDistanceToLine = Double.MAX_VALUE;

        Node candidateNode = null;
        Node candidateNodePair = null;

        for (Node node : graph.getEdges().keySet()) {
            for (Edge edge : graph.getEdges().get(node)) {
                //Get the boundary point of each node (point on the circumference where the line meets)
                Point v1 = getBoundaryPointOfNode(node, edge.linkedNode);
                Point v2 = getBoundaryPointOfNode(edge.linkedNode, node);


                //http://www.sunshine2k.de/coding/java/PointOnLine/PointOnLine.html#step5
                //Used the above link for this part

                //Get dot product of e1 and e2
                Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
                Point e2 = new Point(mouse.x - v1.x, mouse.y - v1.y);
                double valDp = e1.x * e2.x + e1.y * e2.y;

                //Get length of both lines
                double lenLineE1 = Math.sqrt(e1.x * e1.x + e1.y * e1.y);
                double lenLineE2 = Math.sqrt(e2.x * e2.x + e2.y * e2.y);

                //Get length of line from node to the projected point
                double cos = valDp / (lenLineE1 * lenLineE2);
                double lengthToProjectedPoint = cos * lenLineE2;

                //Get point on the line where the cursor is
                Point projectedPoint = new Point((int) (v1.x + (lengthToProjectedPoint * e1.x) / lenLineE1),
                                                (int) (v1.y + (lengthToProjectedPoint * e1.y) / lenLineE1));


                //Get length of the line between the 2 nodes
                double edgeLength = v1.distance(v2);


                //If the projected point is between the 2 nodes,
                //must be in between 0 and the total length of the line
                if (lengthToProjectedPoint > 0 && lengthToProjectedPoint < edgeLength) {

                    //Get distance between the mouse and the line (projected point)
                    double distanceToLine = mouse.distance(projectedPoint);

                    //If the mouse is close enough to the line
                    if (distanceToLine < threshold) {

                        //If the mouse is closer than the previously detected line
                        if (distanceToLine < closestDistanceToLine) {
                            closestDistanceToLine = distanceToLine;
                            candidateNode = node;
                            candidateNodePair = edge.linkedNode;
                        }
                    }
                }
            }
        }

        //Returning the node pair that contains the closest detected edge
        ArrayList<Node> result = new ArrayList<>();
        result.add(candidateNode);
        result.add(candidateNodePair);

        //Return null if no edges are detected
        return candidateNode == null ? null : result;
    }

    public boolean isOutOfBoundaryRight(int x) {
        return x > windowWidth - Node.radius - padding;
    }
    public boolean isOutOfBoundaryLeft(int x) {
        return x < Node.radius + padding;
    }
    public boolean isOutOfBoundaryTop(int y) {
        return y < Node.radius + padding;
    }
    public boolean isOutOfBoundaryBottom(int y) {
        return y > windowHeight - Node.radius - steppingPanelHeight - padding;
    }

    public boolean isPositionOutOfWindow(Point point) {
        return isOutOfBoundaryTop(point.y) ||
               isOutOfBoundaryBottom(point.y) ||
               isOutOfBoundaryRight(point.x) ||
               isOutOfBoundaryLeft(point.x);
    }
}
