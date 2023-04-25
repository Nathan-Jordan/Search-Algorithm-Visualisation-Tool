package Algorithms;


import Main.Edge;
import Main.Node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AlgorithmState {

    public int lineIndex;
    public List<Node> nodes;
    public Map<Node, List<Edge>> edges;
    public boolean complete;
    public Collection<Node> collection;

    public AlgorithmState(int lineIndex, List<Node> nodes, Map<Node, List<Edge>> edges, boolean complete, Collection<Node> collection) {
        this(lineIndex, nodes, edges, complete);
        this.collection = collection;
    }

    public AlgorithmState(int lineIndex, List<Node> nodes, Map<Node, List<Edge>> edges, boolean complete) {
        this.lineIndex = lineIndex;
        this.nodes = nodes;
        this.edges = edges;
        this.complete = complete;
    }
}
