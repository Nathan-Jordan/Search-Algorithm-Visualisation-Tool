package AlgorithmEvaluation;

import AlgorithmEvaluation.BaseAlgorithms.*;
import Main.Graph;
import Main.Log;
import Main.Node;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class AlgorithmEvaluationV2 {

    static Graph graph = new Graph();
    static Random random = new Random();

    static BaseSearchAlgorithm BFS = new BaseBFS();
    static BaseSearchAlgorithm DFS = new BaseDFS();
    static BaseSearchAlgorithm Dijkstra = new BaseDijkstra();
    static BaseSearchAlgorithm AStar = new BaseAStar();

    public static void main(String[] args) {
        int numberOfRuns = 10;
        int numberOfNodes = 40000;

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

        int[] nodesVisitedCounts1 = new int[numberOfRuns];
        long[] timeTakenToRunAlgorithms1 = new long[numberOfRuns];

        int[] nodesVisitedCounts2 = new int[numberOfRuns];
        long[] timeTakenToRunAlgorithms2 = new long[numberOfRuns];

        int[] nodesVisitedCounts3 = new int[numberOfRuns];
        long[] timeTakenToRunAlgorithms3 = new long[numberOfRuns];

        int[] nodesVisitedCounts4 = new int[numberOfRuns];
        long[] timeTakenToRunAlgorithms4 = new long[numberOfRuns];

        for (int i = 0; i < numberOfRuns; i++) {
            generateNewGraph(numberOfNodes);

            //Run each algorithm on the generated graph and record the results
            long timeTaken = getTimeTakenToRunAlgorithm(BFS);
            nodesVisitedCounts1[i] = BFS.nodesVisitedCount;
            timeTakenToRunAlgorithms1[i] = timeTaken;

            BFSTime += timeTaken;
            BFSCount += nodesVisitedCounts1[i];




            timeTaken = getTimeTakenToRunAlgorithm(DFS);
            nodesVisitedCounts2[i] = DFS.nodesVisitedCount;
            timeTakenToRunAlgorithms2[i] = timeTaken;

            DFSTime += timeTaken;
            DFSCount += nodesVisitedCounts2[i];




            timeTaken = getTimeTakenToRunAlgorithm(Dijkstra);
            nodesVisitedCounts3[i] = Dijkstra.nodesVisitedCount;
            timeTakenToRunAlgorithms3[i] = timeTaken;

            DijkstraTime += timeTaken;
            DijkstraCount += nodesVisitedCounts3[i];




            timeTaken = getTimeTakenToRunAlgorithm(AStar);
            nodesVisitedCounts4[i] = AStar.nodesVisitedCount;
            timeTakenToRunAlgorithms4[i] = timeTaken;

            AStarTime += timeTaken;
            AStarCount += nodesVisitedCounts4[i];
        }

        double meanNodesVisitedCount1 = Arrays.stream(nodesVisitedCounts1).average().orElse(Double.NaN);
        double meanTimeTakenToRunAlgorithm1 = Arrays.stream(timeTakenToRunAlgorithms1).average().orElse(Double.NaN);
        double standardDeviationNodesVisitedCount1 = Math.sqrt(Arrays.stream(nodesVisitedCounts1)
                .mapToDouble(count -> Math.pow(count - meanNodesVisitedCount1, 2))
                .sum() / (numberOfRuns - 1));
        double standardDeviationTimeTakenToRunAlgorithm1 = Math.sqrt(Arrays.stream(timeTakenToRunAlgorithms1)
                .mapToDouble(time -> Math.pow(time - meanTimeTakenToRunAlgorithm1, 2))
                .sum() / (numberOfRuns - 1));
        double standardErrorNodesVisitedCount1 = standardDeviationNodesVisitedCount1 / Math.sqrt(numberOfRuns);
        double standardErrorTimeTakenToRunAlgorithm1 = standardDeviationTimeTakenToRunAlgorithm1 / Math.sqrt(numberOfRuns);



        double meanNodesVisitedCount2 = Arrays.stream(nodesVisitedCounts2).average().orElse(Double.NaN);
        double meanTimeTakenToRunAlgorithm2 = Arrays.stream(timeTakenToRunAlgorithms2).average().orElse(Double.NaN);
        double standardDeviationNodesVisitedCount2 = Math.sqrt(Arrays.stream(nodesVisitedCounts2)
                .mapToDouble(count -> Math.pow(count - meanNodesVisitedCount2, 2))
                .sum() / (numberOfRuns - 1));
        double standardDeviationTimeTakenToRunAlgorithm2 = Math.sqrt(Arrays.stream(timeTakenToRunAlgorithms2)
                .mapToDouble(time -> Math.pow(time - meanTimeTakenToRunAlgorithm2, 2))
                .sum() / (numberOfRuns - 1));
        double standardErrorNodesVisitedCount2 = standardDeviationNodesVisitedCount2 / Math.sqrt(numberOfRuns);
        double standardErrorTimeTakenToRunAlgorithm2 = standardDeviationTimeTakenToRunAlgorithm2 / Math.sqrt(numberOfRuns);




        double meanNodesVisitedCount3 = Arrays.stream(nodesVisitedCounts3).average().orElse(Double.NaN);
        double meanTimeTakenToRunAlgorithm3 = Arrays.stream(timeTakenToRunAlgorithms3).average().orElse(Double.NaN);
        double standardDeviationNodesVisitedCount3 = Math.sqrt(Arrays.stream(nodesVisitedCounts3)
                .mapToDouble(count -> Math.pow(count - meanNodesVisitedCount3, 2))
                .sum() / (numberOfRuns - 1));
        double standardDeviationTimeTakenToRunAlgorithm3 = Math.sqrt(Arrays.stream(timeTakenToRunAlgorithms3)
                .mapToDouble(time -> Math.pow(time - meanTimeTakenToRunAlgorithm3, 2))
                .sum() / (numberOfRuns - 1));
        double standardErrorNodesVisitedCount3 = standardDeviationNodesVisitedCount3 / Math.sqrt(numberOfRuns);
        double standardErrorTimeTakenToRunAlgorithm3 = standardDeviationTimeTakenToRunAlgorithm3 / Math.sqrt(numberOfRuns);




        double meanNodesVisitedCount4 = Arrays.stream(nodesVisitedCounts4).average().orElse(Double.NaN);
        double meanTimeTakenToRunAlgorithm4 = Arrays.stream(timeTakenToRunAlgorithms4).average().orElse(Double.NaN);
        double standardDeviationNodesVisitedCount4 = Math.sqrt(Arrays.stream(nodesVisitedCounts4)
                .mapToDouble(count -> Math.pow(count - meanNodesVisitedCount4, 2))
                .sum() / (numberOfRuns - 1));
        double standardDeviationTimeTakenToRunAlgorithm4 = Math.sqrt(Arrays.stream(timeTakenToRunAlgorithms4)
                .mapToDouble(time -> Math.pow(time - meanTimeTakenToRunAlgorithm4, 2))
                .sum() / (numberOfRuns - 1));
        double standardErrorNodesVisitedCount4 = standardDeviationNodesVisitedCount4 / Math.sqrt(numberOfRuns);
        double standardErrorTimeTakenToRunAlgorithm4 = standardDeviationTimeTakenToRunAlgorithm4 / Math.sqrt(numberOfRuns);


        //Display results
        System.out.println("Results on " + numberOfRuns + " randomly generated graphs with " + numberOfNodes + " nodes each:");

        System.out.println("┌───────────────────────┬───────────────────────┬──────────────────┐");
        System.out.printf( "│%-22s │ %-22s│ %-17s│\n", "Search Algorithm", "Average Nodes Visited", "Total time taken");
        System.out.println("├───────────────────────┼───────────────────────┼──────────────────┤");
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Breadth-first search", (float) BFSCount / numberOfRuns, (float) (BFSTime / numberOfRuns));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Depth-first search", (float) DFSCount / numberOfRuns, (float) (DFSTime / numberOfRuns));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "Dijkstra's algorithm", (float) DijkstraCount / numberOfRuns, (float) (DijkstraTime / numberOfRuns));
        System.out.printf( "│%-22s │ %16.2f nodes│ %14.0f ms│\n", "A*", (float) AStarCount / numberOfRuns, (float) (AStarTime / numberOfRuns));
        System.out.println("└───────────────────────┴───────────────────────┴──────────────────┘");

        System.out.println("BFS Visited: " + standardErrorNodesVisitedCount1);
        System.out.println("BFS Time: " + standardErrorTimeTakenToRunAlgorithm1);

        System.out.println("DFS Visited: " + standardErrorNodesVisitedCount2);
        System.out.println("DFS Time: " + standardErrorTimeTakenToRunAlgorithm2);

        System.out.println("Dijk Visited: " + standardErrorNodesVisitedCount3);
        System.out.println("Dijk Time: " + standardErrorTimeTakenToRunAlgorithm3);

        System.out.println("AStar Visited: " + standardErrorNodesVisitedCount4);
        System.out.println("AStar Time: " + standardErrorTimeTakenToRunAlgorithm4);

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
