package Main;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void updateFlags() {
        Node node1 = new Node(10, 10);
        Node node2 = new Node(10, 10);

        node1.setExplored(true);
        node1.setCurrentNode(true);
        node1.setVisited(true);

        node2.updateFlags(node1);

        assertTrue(node2.isCurrentNode());
        assertTrue(node2.isExplored());
        assertTrue(node2.isVisited());
    }

    @Test
    void getBoundaryPointX() {
        Viewport viewport = new Viewport(null);

        Node node1 = new Node(10, 10);
        Node node2 = new Node(100, 10);
        Point boundaryPoint = viewport.getBoundaryPointOfNode(node1, node2);
        assertEquals(node1.x + Node.radius, boundaryPoint.x);
    }

    @Test
    void getBoundaryPointY() {
        Viewport viewport = new Viewport(null);

        Node node1 = new Node(10, 10);
        Node node2 = new Node(10, 100);
        Point boundaryPoint = viewport.getBoundaryPointOfNode(node1, node2);
        assertEquals(node1.y + Node.radius, boundaryPoint.y);
    }
}