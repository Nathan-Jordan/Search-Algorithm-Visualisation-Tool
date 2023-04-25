package Algorithms;

import Main.*;

import java.util.*;
import java.util.List;

public abstract class SearchAlgorithm {

     ArrayList<PseudocodeLine> pseudocodeLines = new ArrayList<>();

     Graph g;
     View view;
     PseudocodeView pseudoView;
     List<AlgorithmState> states;
     int pseudocodeLine = 0;
     boolean complete;
     public int nodesVisitedCount = 0;


     public void init(Graph g, View view, PseudocodeView pseudoView) {
          this.g = g;
          this.view = view;
          this.pseudoView = pseudoView;
     }
     public abstract List<AlgorithmState> runAlgorithm();

     public void addPseudocodeLine(String text) {
          pseudocodeLines.add(new PseudocodeLine(text, 1));
     }

     public void addPseudocodeLine(String text, int indent) {
          pseudocodeLines.add(new PseudocodeLine(text, indent + 1));
     }

     public void addCurrentState(Collection<Node> collection) {
          ArrayList<Node> newCollection = new ArrayList<>(collection);
          states.add(new AlgorithmState(pseudocodeLine, g.cloneNodes(), g.cloneEdges(), complete, newCollection));
          pseudocodeLine++;
     }

     public void addCurrentState() {
          states.add(new AlgorithmState(pseudocodeLine, g.cloneNodes(), g.cloneEdges(), complete));
          pseudocodeLine++;
     }

     public void goalNodeFound(Node node) {
          //Set start line
          addCurrentState();
          pseudocodeLine = 0;
          complete = true;    //Changes from pseudocode to backtracking pseudocode

          //Move to next pseudocode line
          addCurrentState();
          pseudocodeLine = 0;

          Log.logMessage("Goal node reached " + node);

          //Setup line numbers for fixed lines
          int whileLoopCondition = 1;


          //Backtracking loop
          while (!node.isRootNode()) {
               //Set the pseudocode line to the start of the while loop
               pseudocodeLine = whileLoopCondition;
               addCurrentState();

               //Set previousNode to the previous node of node
               Node previousNode = node.previousNode;
               previousNode.setConsideredNode(true);
               addCurrentState();

               List<Edge> previousNodesNeighbours = g.getOutEdgesOfNode(previousNode);

               //Get relevant edge and set backtrack to true (NOT DIRECTED)
               for (Edge edge: previousNodesNeighbours) {
                    if (edge.linkedNode == node) {
                         edge.setBackTrack(true);
                         break;
                    }
               }
               for (Edge edge: g.getOutEdgesOfNode(node)) {
                    if (edge.linkedNode == previousNode) {
                         edge.setBackTrack(true);
                         break;
                    }
               }

               //'Add' node to a path list for pseudocode visualisation
               addCurrentState();


               //Set node to previous node
               node.setCurrentNode(false);
               previousNode.setConsideredNode(false);
               node = previousNode;
               node.setCurrentNode(true);
               addCurrentState();
          }

          //'Returning' path list for pseudocode visualisation
          addCurrentState();
     }

     public void resetNodesVisitedCount() {
          nodesVisitedCount = 0;
     }

     public ArrayList<PseudocodeLine> getPseudocodeLines() {
          return pseudocodeLines;
     }

     public ArrayList<PseudocodeLine> getBacktrackPseudocode() {
          ArrayList<PseudocodeLine> backTrackingPseudocode = new ArrayList<>();

          backTrackingPseudocode.add(new PseudocodeLine("getPathList(node <font color='#FFC800'>N</font>)", 1));
          backTrackingPseudocode.add(new PseudocodeLine("let pathList be a list", 1));
          backTrackingPseudocode.add(new PseudocodeLine("while <font color='#FFC800'>N</font> is not the <font color='Red'>root</font> do", 1));
          backTrackingPseudocode.add(new PseudocodeLine("<font color='#00B2B2'>previousNode</font> = <font color='#FFC800'>N</font>.getPreviousNode()", 2));
          backTrackingPseudocode.add(new PseudocodeLine("insert <font color='#00B2B2'>previousNode</font> at head of pathList", 3));
          backTrackingPseudocode.add(new PseudocodeLine("<font color='#FFC800'>N</font> = <font color='#00B2B2'>previousNode</font>", 2));
          backTrackingPseudocode.add(new PseudocodeLine("return pathList", 1));

          return backTrackingPseudocode;
     }
}
