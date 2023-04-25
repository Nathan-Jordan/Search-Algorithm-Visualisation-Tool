package Main;

public class Edge {

    public Node linkedNode;
    boolean backTrack;

    public Edge(Node linkedNode) {
        this.linkedNode = linkedNode;
        backTrack = false;
    }

    public boolean isBackTrack() {
        return backTrack;
    }

    public void setBackTrack(boolean backTrack) {
        this.backTrack = backTrack;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Edge other)) {
            return false;
        }

        return this.linkedNode == other.linkedNode;
    }

    @Override
    protected Edge clone() {
        Edge edge = new Edge(this.linkedNode);
        edge.backTrack = this.backTrack;
        return edge;
    }

    @Override
    public String toString() {
        return linkedNode.toString();
    }
}
