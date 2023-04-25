package AlgorithmEvaluation.Algorithms;

import Main.*;

public abstract class BaseSearchAlgorithm {

     Graph g;
     public int nodesVisitedCount;

     public void init(Graph g) {
          this.g = g;
     }
     public abstract void runAlgorithm();
}
