package Main;

import Algorithms.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main extends Component {

    Graph graph = new Graph();
    View view = new View(graph);
    PseudocodeView pseudocodeView = new PseudocodeView();
    Visualiser visualiser = new Visualiser(view, pseudocodeView, graph);
    SearchAlgorithm searchAlgorithm;
    public static JFrame mainFrame;

    static JMenuItem bfs = new JMenuItem("Breadth-First Search");
    static JMenuItem dfs = new JMenuItem("Depth-First Search");
    static JMenuItem dijkstra = new JMenuItem("Dijkstra's Algorithm");
    static JMenuItem aStar = new JMenuItem("A* Search");

    static JMenuItem runAlgorithm = new JMenuItem("Run Algorithm");
    static JMenuItem resetNodes = new JMenuItem("Reset Nodes");
    static JMenuItem clearNodes = new JMenuItem("Clear Nodes");
    static JMenuItem generateRandomGraph = new JMenuItem("Generate Random Graph");

    static boolean directed = false;

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        main.displayWindow();
    }

    public void init() {
        searchAlgorithm = new BreadthFirstSearch();
        searchAlgorithm.init(graph, pseudocodeView);
        pseudocodeView.setAlgorithm(searchAlgorithm);
    }

    public void displayWindow() {
        Log.logMessage("Window init");

        mainFrame = new JFrame("Search Algorithm Visualiser");

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(view, BorderLayout.CENTER);
        mainFrame.add(pseudocodeView, BorderLayout.LINE_END);

        mainFrame.setJMenuBar(createMenuBar());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(950, 700);  //Set size
        mainFrame.setMinimumSize(new Dimension(950, 700));

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true); //Show window

        Log.logMessage("Window shown");
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        //Menu items
        runAlgorithm.addActionListener(e -> {
            if (graph.getRootNode() == null) {
                JOptionPane.showMessageDialog(mainFrame, "A root node must be set before an algorithm can be run.", "No Root Node Set", JOptionPane.ERROR_MESSAGE);
            } else {
                //Reset nodes
                graph.resetNodes();

                //Get all the states of the visualisation
                List<AlgorithmState> stateList = searchAlgorithm.runAlgorithm();

                //Set pseudocode line to the start
                pseudocodeView.setCurrentLineIndex(0);

                //Add the states to the visualiser
                visualiser.addStateList(stateList);

                //Reset pause/resume button
                view.pauseResume.setText("Pause");

                //Enable stepping buttons
                view.setSteppingButtonsEnabled(true);

                //Disable menu items
                setMenuItemsEnabled(false);

                //Set the visualiser to be not stopped
                visualiser.visualiserStopped = false;

                Log.logMessage("Starting visualisation");
                //Start visualisation starting at the first state
                visualiser.visualiseAlgorithm(0);
            }
        });
        clearNodes.addActionListener(e -> {
            graph.clearNodes();
            view.repaint();
            pseudocodeView.setCurrentLineIndex(0);
            pseudocodeView.changeToPseudocode();

            //Enable stepping buttons
            view.setSteppingButtonsEnabled(false);
        });
        resetNodes.addActionListener(e -> {
            graph.resetNodes();
            view.repaint();
            pseudocodeView.setCurrentLineIndex(0);
            pseudocodeView.changeToPseudocode();

            //Enable stepping buttons
            view.setSteppingButtonsEnabled(false);
        });

        JMenuItem showPseudocode = new JMenuItem("Hide Pseudocode");
        showPseudocode.addActionListener(e -> {
            if (pseudocodeView.isVisible()) {
                mainFrame.setMinimumSize(new Dimension(500, 700));
                mainFrame.setSize(mainFrame.getWidth() - pseudocodeView.getWidth(), mainFrame.getHeight());
                showPseudocode.setText("Show Pseudocode");
                Log.logMessage("Pseudocode pane hidden");
            } else {
                mainFrame.setMinimumSize(new Dimension(950, 700));
                showPseudocode.setText("Hide Pseudocode");
                Log.logMessage("Pseudocode pane shown");
            }

            pseudocodeView.setVisible(!pseudocodeView.isVisible());
            mainFrame.revalidate();
        });

        JMenuItem nodeLabelsNum = new JMenuItem("Numeric");
        nodeLabelsNum.addActionListener(e -> {
            graph.changeLabelType(Graph.NUMERICAL);
            view.repaint();
        });
        JMenuItem nodeLabelsAlpha = new JMenuItem("Alphabetic");
        nodeLabelsAlpha.addActionListener(e -> {
            graph.changeLabelType(Graph.ALPHABETICAL);
            view.repaint();
        });


        generateRandomGraph.addActionListener(e -> {
            boolean requestInput = true;

            while (requestInput) {
                String input = (String) JOptionPane.showInputDialog(mainFrame,
                        "Enter number of nodes to generate:\n(greater than 2)", "Number of nodes",
                        JOptionPane.QUESTION_MESSAGE, null, null, "10");

                //If user has not pressed cancel/escape or x
                if (input == null) {
                    requestInput = false;

                } else {
                    try {
                        //Attempt to parse the input to a number
                        input = input.trim();
                        int numberOfNodes = Integer.parseInt(input);

                        //If the input is less than 3
                        if (numberOfNodes < 3) {
                            JOptionPane.showMessageDialog(mainFrame, "Number of nodes must greater than 2.", "Invalid input", JOptionPane.ERROR_MESSAGE);

                        } else {
                            requestInput = false;

                            graph.clearNodes();
                            graph.generateRandomGraph(view.getSize(), numberOfNodes);
                            view.repaint();
                            pseudocodeView.repaint();

                            Log.logMessage("Graph generated with " + graph.nodes.size() + " nodes");


                            //If number of created nodes is less than number of input nodes
                            if (graph.nodes.size() < numberOfNodes) {
                                JOptionPane.showMessageDialog(mainFrame,
                                        "Unable to create a graph with " + numberOfNodes + " nodes\n" +
                                                graph.nodes.size() + " nodes created instead.", "Graph created", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(mainFrame, "Graph with " + numberOfNodes + " nodes created.",
                                        "Graph created", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(mainFrame, "Please enter a whole number.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        JMenu subLabelType = new JMenu("Node Label Type");
        subLabelType.add(nodeLabelsNum);
        subLabelType.add(nodeLabelsAlpha);
        menu.add(runAlgorithm);
        menu.add(clearNodes);
        menu.add(resetNodes);
        menu.add(showPseudocode);
        menu.add(subLabelType);
        menu.add(generateRandomGraph);


        JMenu algorithms = new JMenu("Algorithms");
        bfs.addActionListener(e -> changeAlgorithm(new BreadthFirstSearch()));
        dfs.addActionListener(e -> changeAlgorithm(new DepthFirstSearch()));
        dijkstra.addActionListener(e -> changeAlgorithm(new DijkstraSearch()));
        aStar.addActionListener(e -> changeAlgorithm(new AStarSearch()));
        algorithms.add(bfs);
        algorithms.add(dfs);
        algorithms.add(dijkstra);
        algorithms.add(aStar);

        menuBar.add(menu);
        menuBar.add(algorithms);

        return menuBar;
    }

    static void setMenuItemsEnabled(boolean b) {
        runAlgorithm.setEnabled(b);
        clearNodes.setEnabled(b);
        resetNodes.setEnabled(b);
        generateRandomGraph.setEnabled(b);
        bfs.setEnabled(b);
        dfs.setEnabled(b);
        dijkstra.setEnabled(b);
        aStar.setEnabled(b);
    }

    void changeAlgorithm(SearchAlgorithm searchAlgorithm) {
        if (this.searchAlgorithm.getClass() == searchAlgorithm.getClass()) {
            return;
        }

        this.searchAlgorithm = searchAlgorithm;
        searchAlgorithm.init(graph, pseudocodeView);
        pseudocodeView.setAlgorithm(searchAlgorithm);
        pseudocodeView.setCurrentLineIndex(0);

        Log.logMessage("Algorithm changed to " + searchAlgorithm);
    }
}
