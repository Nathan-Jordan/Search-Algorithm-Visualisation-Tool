package Main;

import java.awt.*;
import java.util.Objects;

public class Node implements Cloneable{

    public int nodeID;
    public String nodeLabel;

    public int x, y;
    public static int diameter = 50;
    public static int radius = diameter / 2;
    public int outerCirclePadding = 10;

    public Node previousNode;
    public boolean visited, explored, currentNode, goalNode, rootNode, consideredNode;

    public static Color visitedColour = Color.BLUE;
    public static Color exploredColour = Color.GRAY;
    public static Color defaultColour = Color.BLACK;
    public static Color goalColour = Color.GREEN.darker();
    public static Color rootColour = Color.RED;
    public static Color currentColour = Color.ORANGE;
    public static Color consideredColour = Color.CYAN.darker();



    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public void setCurrentNode(boolean currentNode) {
        this.currentNode = currentNode;
    }

    public void setRootNode(boolean rootNode) {
        this.rootNode = rootNode;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isExplored() {
        return explored;
    }

    public boolean isCurrentNode() {
        return currentNode;
    }

    public boolean isGoalNode() {
        return goalNode;
    }

    public boolean isRootNode() {
        return rootNode;
    }

    public void updateFlags(Node otherNode) {
        setExplored(otherNode.isExplored());
        setVisited(otherNode.isVisited());
        setCurrentNode(otherNode.isCurrentNode());
        setConsideredNode(otherNode.isConsideredNode());
    }

    public Color getNodeColour() {
        if (isExplored()) return exploredColour;
        if (isVisited()) return visitedColour;
        return defaultColour;
    }

    public boolean isConsideredNode() {
        return consideredNode;
    }

    public void setConsideredNode(boolean consideredNode) {
        this.consideredNode = consideredNode;
    }

    public void setGoalNode(boolean goalNode) {
        this.goalNode = goalNode;
    }

    public void drawNode(Graphics2D g2) {
        FontMetrics fontMetrics = g2.getFontMetrics();

        //Draw node background
        g2.setColor(Color.WHITE);
        g2.fillOval(x - radius, y - radius, diameter, diameter);


        //Draw node in red if it's a root node
        if (isRootNode()) drawNodeCircle(g2, rootColour);
        //Draw node in green if it's a goal node
        else if (isGoalNode()) drawNodeCircle(g2, goalColour);
        //Else draw node depending on it's state
        else drawNodeCircle(g2, getNodeColour());


        //Node label (inherits colour from the above calls)
        g2.drawString(nodeLabel,
                x - (fontMetrics.stringWidth(nodeLabel)/2),
                y + (fontMetrics.getAscent()/2));


        //Draw considered node identifier
        if (isConsideredNode()) drawConsideredNodeCircle(g2);

        //Draw current node identifier
        if (isCurrentNode()) drawNodeCircle(g2, currentColour, diameter + outerCirclePadding);
    }

    private void drawNodeCircle(Graphics2D g2, Color colour, int diameter) {
        g2.setColor(colour);
        g2.drawOval(x - (diameter/2), y - (diameter/2), diameter, diameter);
    }

    private void drawNodeCircle(Graphics2D g2, Color colour) {
        drawNodeCircle(g2, colour, diameter);
    }

    private void drawConsideredNodeCircle(Graphics2D g2) {
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{10}, 0);

        g2.setStroke(dashed);
        drawNodeCircle(g2, consideredColour, diameter + outerCirclePadding);
        View.resetStroke(g2);
    }

    public static Point getBoundaryPoint(Node node1, Node node2) {
        //Returns the boundary point of node1 when linked to node2
        //which is the point on node1's circle which faces towards node2

        Point v1 = new Point(node1.x, node1.y);
        Point v2 = new Point(node2.x, node2.y);

        double dx = v1.x - v2.x;
        double dy = v1.y - v2.y;
        double dl = Math.sqrt(dx*dx + dy*dy);
        double nx = dx/dl;
        double ny = dy/dl;

        Point p = new Point();
        p.x = (int) (v1.x - (radius) * nx);
        p.y = (int) (v1.y - (radius) * ny);
        return p;
    }

    @Override
    public String toString() {
        return nodeLabel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Node node)) {
            return false;
        }

        return this.nodeID == node.nodeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID);
    }

    @Override
    public Node clone() {
        try {
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
