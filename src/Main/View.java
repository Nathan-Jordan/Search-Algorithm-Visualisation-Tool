package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class View extends JComponent {

    public static int windowHeight;
    public static int windowWidth;
    public static int padding = 2;
    public static int steppingPanelHeight = 37;

    private boolean leftMouseDown, rightMouseDown;
    private Point drawEdgeStart = null;
    private Point drawEdgeEnd = null;
    private Node selectedNode;
    private boolean highlightedObject = false;

    private final Point mouse = new Point();
    private final Point delta = new Point();  //Distance from nodes center as to not "snap" nodes to cursor when moving

    private final Graph graph;
    private Visualiser visualiser;
    Viewport viewport;

    JButton stop;
    JButton pauseResume;
    JButton stepForward;
    JButton stepBackwards;

    public View(Graph graph) {
        addMouseListener(new mouseEvent());
        addMouseMotionListener(new mouseMoveEvent());
        this.graph = graph;
        this.viewport = new Viewport(graph);

        setLayout(new BorderLayout());

        createSteppingPanel();
    }

    private void createSteppingPanel() {
        stop = new JButton("Stop");
        stop.addActionListener(e -> {
            //"Pause" current visualising
            visualiser.visualising = false;

            //Stop current visualising
            visualiser.visualiserStopped = true;
            setSteppingButtonsEnabled(false);
            Main.setMenuItemsEnabled(true);
            pauseResume.setText("Pause");
            Log.logMessage("Stopping visualisation");
        });

        pauseResume = new JButton("Pause");
        pauseResume.addActionListener(e -> {
            if (Objects.equals(pauseResume.getText(), "Pause")) {
                visualiser.visualising = false;
                stepForward.setEnabled(true);
                stepBackwards.setEnabled(true);
                pauseResume.setText("Resume");
                Log.logMessage("Pausing visualisation");

            } else if (Objects.equals(pauseResume.getText(), "Restart")) {
                pauseResume.setText("Pause");
                visualiser.visualiseAlgorithm(0);
                Log.logMessage("Restarting visualisation");
            } else {
                visualiser.resumeVisualisationAtCurrentIndex();
                pauseResume.setText("Pause");
                Log.logMessage("Resuming visualisation");
            }
        });

        //Adding step forward button
        stepForward = new JButton(">>");
        stepForward.addActionListener(e -> {
            Log.logMessage("Stepping forwards");
            visualiser.stepForwards();
        });

        //Adding step backwards button
        stepBackwards = new JButton("<<");
        stepBackwards.addActionListener(e -> {
            Log.logMessage("Stepping backwards");
            visualiser.stepBackwards();
        });


        JPanel steppingButtonPanel = new JPanel();

        steppingButtonPanel.add(stepBackwards);
        steppingButtonPanel.add(stop);
        steppingButtonPanel.add(pauseResume);
        steppingButtonPanel.add(stepForward);

        add(steppingButtonPanel, BorderLayout.SOUTH);

        setSteppingButtonsEnabled(false);
    }

    public void setSteppingButtonsEnabled(boolean b) {
        pauseResume.setEnabled(b);
        stepForward.setEnabled(b);
        stepBackwards.setEnabled(b);
        stop.setEnabled(b);
    }

    public void addVisualizer(Visualiser visualiser) {
        this.visualiser = visualiser;
    }

    private class mouseMoveEvent extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            mouse.x = e.getX();
            mouse.y = e.getY();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedNode == null) {
                return;
            }
            mouse.x = e.getX();
            mouse.y = e.getY();

            //Moving node
            if (leftMouseDown) {
                selectedNode.x = mouse.x - delta.x;
                selectedNode.y = mouse.y - delta.y;

                //Drawing edge
            } else if (rightMouseDown && visualiser.visualiserStopped) {
                if (drawEdgeStart == null) {
                    drawEdgeStart = new Point();
                    drawEdgeEnd = new Point();
                }

                drawEdgeStart.setLocation(selectedNode.x, selectedNode.y);
                drawEdgeEnd.setLocation(e.getX(), e.getY());
            }

            repaint();
        }
    }

    private class mouseEvent extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            //Set initially clicked node
            selectedNode = viewport.getNodeFromMouse(e.getPoint());

            //Set the delta of the selected node
            if (selectedNode != null) {
                delta.x = e.getX() - selectedNode.x;
                delta.y = e.getY() - selectedNode.y;
            }

            //Store what button is clicked
            if (e.getButton() == 1) {
                leftMouseDown = true;
            } else if (e.getButton() == 3) {
                rightMouseDown = true;
            }
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            //Reset left mouse button
            if (e.getButton() == 1) {
                leftMouseDown = false;

                //Reset right mouse button and edge drawings
            } else if (e.getButton() == 3 && visualiser.visualiserStopped) {
                rightMouseDown = false;
                drawEdgeStart = null;
                drawEdgeEnd = null;

                //If an edge should be created
                Node endNode = viewport.getNodeFromMouse(e.getPoint());
                if (endNode != null && selectedNode != null) {
                    graph.addEdge(selectedNode, endNode);
                }
            }

            //Reset selected node
            selectedNode = null;
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent mEvent) {
            if (!visualiser.visualiserStopped) {
                return;
            }

            //Left click creates a node or flips goal node
            if (mEvent.getButton() == 1) {

                //Get node from click - directly on top of node
                Node node = viewport.getNodeFromMouse(mEvent.getPoint(), 1f);

                //If no node is found near the click point
                if (node == null) {
                    //If the click point is inside the window (includes the node size as padding)
                    if (!viewport.isPositionOutOfWindow(mEvent.getPoint())) {
                        node = new Node(mEvent.getX(), mEvent.getY());
                        graph.addNode(node);
                    }

                } else {
                    //Double click
                    if (mEvent.getClickCount() == 2) {

                        //Attempt to get node from click - at least 1 diameter away from other nodes
                        node = viewport.getNodeFromMouse(mEvent.getPoint());

                        if (node != null) {
                            String[] options = {"Root", "Goal", "Unset"};
                            int result = JOptionPane.showOptionDialog(Main.mainFrame, "Set node " + node.nodeLabel + ":"
                                    , "Set node", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if (result == 0) {
                                graph.setRootNode(node);
                            } else if (result == 1) {
                                graph.setGoalNode(node);
                            } else if (result == 2) {
                                Log.logMessage("Node " + node + " unset");

                                if (node.isRootNode()) {
                                    graph.rootNode = null;
                                }

                                node.setGoalNode(false);
                                node.setRootNode(false);
                            }
                        }
                    }
                }


                //Removes nodes/edges on right click
            } else if (mEvent.getButton() == 3) {
                Node node = viewport.getNodeFromMouse(mEvent.getPoint());
                //Remove node if identified
                if (node != null) {
                    graph.removeNode(node);

                } else {
                    //Remove edge if identified
                    ArrayList<Node> edgeBetweenNodes = viewport.getEdgeFromMouse(mEvent.getPoint());
                    if (edgeBetweenNodes != null) {
                        graph.removeEdgesBetweenNodes(edgeBetweenNodes.get(0), edgeBetweenNodes.get(1));
                    }
                }
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Set new window size
        windowWidth = getWidth();
        windowHeight = getHeight();

        //Check for any nodes that are off-screen
        checkForNodesOutOfScreen();

        //Initialise font
        g2.setFont(new Font("", Font.BOLD, 16));

        //Draw all graph components
        drawGraph(g2);
    }

    private void drawGraph(Graphics2D g2) {
        //Draw background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        //Draw edges (to go 'under' nodes)
        drawEdges(g2);

        //Highlighting edge after all edges are drawn
        highlightEdge(g2);

        //Draw nodes
        drawNodes(g2);

        //Highlighting node after all nodes are drawn
        highlightNode(g2);

        //Draws an edge between node and cursor, if a node has been selected
        drawEdgeBetweenNodeAndCursor(g2);
    }

    private void checkForNodesOutOfScreen() {
        //If node is outside the window
        for (Node node : graph.getNodes()) {
            //Off right
            if (viewport.isOutOfBoundaryRight(node.x)) {
                node.x = windowWidth - Node.radius - padding;
            //Off left
            } else if (viewport.isOutOfBoundaryLeft(node.x)) {
                node.x = Node.radius + padding;
            }

            //Off bottom (including stepping panel)
            if (viewport.isOutOfBoundaryBottom(node.y)) {
                node.y = windowHeight - Node.radius - steppingPanelHeight - padding;
            //Off top
            } else if (viewport.isOutOfBoundaryTop(node.y)) {
                node.y = Node.radius + padding;
            }
        }
    }

    private void drawEdges(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        resetStroke(g2);

        //Edge drawing
        //NOT Directed        //Currently draws both lines e.g (0-1 and 1-0)
        for (Node currentNode : graph.edgesByNode.keySet()) {
            //For each edge of the current node
            for (Edge edge : graph.getOutEdgesOfNode(currentNode)) {

                Node node = edge.linkedNode;

                if (edge.isBackTrack()) {
                    g2.setColor(Color.BLUE);
                } else {
                    g2.setColor(Color.BLACK);
                }

                g2.drawLine(currentNode.x, currentNode.y, node.x, node.y);

                //Check that the current node's previous node is the node of the current edge
                // (otherwise causes the wrong arrow to be drawn)
                if (currentNode.previousNode == node) {
                    drawArrow(g2, currentNode);
                }
            }
        }
    }

    private void drawArrow(Graphics2D g2, Node currentNode) {
        Node previousNode = currentNode.previousNode;

        //Get boundary points of both nodes
        Point boundary1 = viewport.getBoundaryPointOfNode(previousNode, currentNode);
        Point boundary2 = viewport.getBoundaryPointOfNode(currentNode, previousNode);

        //Get mid-point of edge
        int midX = Math.abs(boundary1.x + boundary2.x) / 2;
        int midY = Math.abs(boundary1.y + boundary2.y) / 2;

        int[][] arrowPoints = viewport.drawArrow(currentNode, midX, midY);

        resetStroke(g2);
        g2.drawPolyline(arrowPoints[0], arrowPoints[1], 3);
        resetStroke(g2);
    }

    private void highlightEdge(Graphics2D g2) {
        ArrayList<Node> edgeBetweenNodes = viewport.getEdgeFromMouse(mouse);

        if (edgeBetweenNodes != null && !highlightedObject) {
            Node node1 = edgeBetweenNodes.get(0);
            Node node2 = edgeBetweenNodes.get(1);

            g2.setColor(new Color(255, 242, 0, 160));
            g2.setStroke(new BasicStroke(6));

            g2.drawLine(node1.x, node1.y, node2.x, node2.y);

            resetStroke(g2);
        }
    }

    private void drawNodes(Graphics2D g2) {
        //Node drawing
        for (Node node : graph.nodes) {
            node.drawNode(g2);
        }
    }

    private void highlightNode(Graphics2D g2) {
        Node node = viewport.getNodeFromMouse(mouse, 2.1f);

        if (selectedNode != null) {
            node = selectedNode;
        }

        if (node != null) {
            highlightedObject = true;
            g2.setStroke(new BasicStroke(6));
            g2.setColor(new Color(255, 242, 0, 160));

            g2.drawOval(node.x - Node.radius, node.y - Node.radius,
                    Node.diameter, Node.diameter);

            resetStroke(g2);
        } else {
            highlightedObject = false;
        }
    }

    static void resetStroke(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
    }

    private void drawEdgeBetweenNodeAndCursor(Graphics2D g2) {
        //Drawing edge between selected node and cursor
        if (drawEdgeStart != null && drawEdgeEnd != null) {
            g2.setColor(Color.BLACK);
            g2.drawLine(drawEdgeStart.x, drawEdgeStart.y, drawEdgeEnd.x, drawEdgeEnd.y);
        }
    }
}