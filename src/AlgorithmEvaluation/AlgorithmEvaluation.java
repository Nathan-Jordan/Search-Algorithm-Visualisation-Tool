package AlgorithmEvaluation;

import AlgorithmEvaluation.BaseAlgorithms.*;
import Main.Graph;
import Main.Log;
import Main.Node;

import java.awt.*;
import java.util.Random;

public class AlgorithmEvaluation {

    static Graph graph = new Graph();
    static Random random = new Random();

    static BaseSearchAlgorithm BFS = new BaseBFS();
    static BaseSearchAlgorithm DFS = new BaseDFS();
    static BaseSearchAlgorithm Dijkstra = new BaseDijkstra();
    static BaseSearchAlgorithm AStar = new BaseAStar();

    public static void main(String[] args) {
        int numberOfRuns = 10;
        int numberOfNodes = 20000;

        //Initialise timers
        long BFSTime = 0;
        long DFSTime = 0;
        long DijkstraTime = 0;
        long AStarTime = 0;

        //Initialise counters
        int BFSCount = 0;
        int DFSCount = 0;
        int DijkstraCount = 0;
        int AStarCount = 0;

        //Initialise search algorithms
        initSearchAlgorithms();

        Log.disableLogging();

        for (int i = 0; i < numberOfRuns; i++) {
            generateNewGraph(numberOfNodes);

            //Run each algorithm on the generated graph and record the results
            BFSTime += getTimeTakenToRunAlgorithm(BFS);
            BFSCount += BFS.nodesVisitedCount;

            DFSTime += getTimeTakenToRunAlgorithm(DFS);
            DFSCount += DFS.nodesVisitedCount;

            DijkstraTime += getTimeTakenToRunAlgorithm(Dijkstra);
            DijkstraCount += Dijkstra.nodesVisitedCount;

            AStarTime += getTimeTakenToRunAlgorithm(AStar);
            AStarCount += AStar.nodesVisitedCount;
        }

        //Display results
        System.out.println("Results on " + numberOfRuns + " randomly generated graphs with " + numberOfNodes + " nodes each:");

        System.out.println("┌───────────────────────┬───────────────────────┬──────────────────┐");
        System.out.printf( "│%-22s │ %-22s│ %-17s│\n", "Search Algorithm", "Average Nodes Visited", "Total time taken");
        System.out.println("├───────────────────────┼───────────────────────┼──────────────────┤");
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Breadth-first search", (float) BFSCount / numberOfRuns, (BFSTime / 1e+6));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Depth-first search", (float) DFSCount / numberOfRuns, (DFSTime / 1e+6));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Dijkstra's algorithm", (float) DijkstraCount / numberOfRuns, (DijkstraTime / 1e+6));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "A*", (float) AStarCount / numberOfRuns, (AStarTime / 1e+6));
        System.out.println("└───────────────────────┴───────────────────────┴──────────────────┘");


        Log.enableLogging();
    }

    private static void initSearchAlgorithms() {
        BFS.init(graph);
        DFS.init(graph);
        Dijkstra.init(graph);
        AStar.init(graph);
    }

    private static long getTimeTakenToRunAlgorithm(BaseSearchAlgorithm algorithm) {
        //Reset the nodes on the graph since it is used for all algorithms
        graph.resetNodes();

        //Reset the nodes visited count
        algorithm.nodesVisitedCount = 0;

        //Run the current algorithm and record the time taken.
        long startTime = System.nanoTime();
        algorithm.runAlgorithm();

        return System.nanoTime() - startTime;
    }

    private static void generateNewGraph(int numberOfNodes) {
        //Clear existing nodes
        graph.clearNodes();

        //Generate a new random graph
        graph.generateRandomGraph(new Dimension(100000, 100000), numberOfNodes);

        setRootGoalNodes(numberOfNodes);
    }

    private static void setRootGoalNodes(int numberOfNodes) {
        //Choose a random goal and root node, ensuring they are different
        int rootID = random.nextInt(numberOfNodes);
        int goalID = random.nextInt(numberOfNodes);

        while (rootID == goalID) {
            goalID = random.nextInt(numberOfNodes);
        }


        Node root = graph.getNode(rootID);
        Node goal = graph.getNode(goalID);

        //Set the relevant node flags
        root.setRootNode(true);
        goal.setGoalNode(true);

        //Set the graph root and goal nodes
        graph.setRootNode(root);
        graph.setGoalNode(goal);
    }
}
