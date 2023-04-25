package Main;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {


    @Test
    void getNodeFromClickInRange(){
        Graph g = new Graph();
        g.clearNodes();
        Node node = new Node(10, 10);
        g.addNode(node);
        View view = new View(g);

        Point p = new Point(10, 10);
        assertEquals(node, view.viewport.getNodeFromMouse(p));
    }

    @Test
    void getNodeFromClickNotInRange(){
        Graph g = new Graph();
        Node node = new Node(10, 10);
        g.addNode(node);
        View view = new View(g);

        Point p = new Point(100, 100);
        assertNull(view.viewport.getNodeFromMouse(p));
    }

    @Test
    void getEdgeFromClickInRange(){
        Graph g = new Graph();
        View view = new View(g);

        g.clearNodes();
        Node node1 = new Node(10, 10);
        Node node2 = new Node(100, 10);
        g.addNode(node1);
        g.addNode(node2);
        g.addEdge(node1, node2);

        Point mouse = new Point(50, 10);
        ArrayList<Node> edge = view.viewport.getEdgeFromMouse(mouse);

        assertTrue(edge.contains(node1));
        assertTrue(edge.contains(node2));
    }

    @Test
    void getEdgeFromClickNotInRange(){
        Graph g = new Graph();
        View view = new View(g);

        g.clearNodes();
        Node node1 = new Node(10, 10);
        Node node2 = new Node(100, 10);
        g.addNode(node1);
        g.addNode(node2);
        g.addEdge(node1, node2);

        Point mouse = new Point(200, 200);
        ArrayList<Node> edge = view.viewport.getEdgeFromMouse(mouse);

        assertNull(edge);
    }
}