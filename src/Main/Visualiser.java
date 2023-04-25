package Main;

import Algorithms.AlgorithmState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Visualiser {

    int currentIndex;
    boolean visualising = false;
    boolean visualiserStopped = true;

    private final View view;
    private final PseudocodeView pseudocodeView;
    private final Graph graph;

    private int delayComplete = 200;
    private int delayNormal = 150;
    private int sleepDelayMilliSeconds = delayNormal;

    private List<AlgorithmState> stateList = new ArrayList<>();

    public Visualiser(View view, PseudocodeView pseudocodeView, Graph graph) {
        this.view = view;
        this.pseudocodeView = pseudocodeView;
        this.graph = graph;

        this.view.addVisualizer(this);
    }

    public void visualizeAlgorithm(int indexToStartAt) {
        visualising = true;
        view.stepBackwards.setEnabled(false);
        view.stepForward.setEnabled(false);

        Thread algo = new Thread(() -> {
            //Looping through the state list providing the visualisation is not paused
            for (currentIndex = indexToStartAt;
                 currentIndex < stateList.size() && visualising;
                 currentIndex++) {

                updateState();

                try {
                    Thread.sleep(sleepDelayMilliSeconds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            currentIndex--;
        });
        algo.start();
    }

    public void resumeVisualizationAtCurrentIndex() {
        visualizeAlgorithm(currentIndex);
    }

    public void stepForwards() {
        //Do not allow stepping when visualising (maybe disable buttons instead)
        if (!visualising) {
            currentIndex++;
            updateState();
        }
    }
    public void stepBackwards() {
        //Do not allow stepping when visualising (maybe disable buttons instead)
        if (!visualising) {
            currentIndex--;
            updateState();
        }
    }

    private void updateState() {
        //Once last state is reached
        if (currentIndex == stateList.size() - 1) {
            view.pauseResume.setText("Restart");
            visualising = false;
            view.stepForward.setEnabled(false);
            view.stepBackwards.setEnabled(true);

        //If loop not stopped
        } else if (visualising) {
            view.pauseResume.setText("Pause");

        //If end has been reached and stepped backwards
        } else if (Objects.equals(view.pauseResume.getText(), "Restart") &&
                currentIndex != stateList.size()) {
            view.stepForward.setEnabled(true);
            view.pauseResume.setText("Resume");
        }


        if (!indexOutOfRange()) {
            AlgorithmState state = stateList.get(currentIndex);

            if (state.complete) {
                sleepDelayMilliSeconds = delayComplete;
                pseudocodeView.changeToBackTracking();
            } else {
                sleepDelayMilliSeconds = delayNormal;
                pseudocodeView.changeToPseudocode();
            }

            pseudocodeView.setCurrentLineIndex(state.lineIndex);
            graph.updateNodes(state.nodes);
            graph.updateEdges(state.edges);

            //Collection contains node information (stack/queue for BFS/DFS)
            //System.out.println("UPDATING STATE: " + state.collection);

            pseudocodeView.repaint();
            view.repaint();
        }
    }

    private boolean indexOutOfRange() {
        if (currentIndex > stateList.size() - 1) {
            currentIndex = stateList.size() - 1;
            return true;

        } else if (currentIndex < 0) {
            currentIndex = 0;
            return true;
        }

        return false;
    }

    public boolean stateListIsEmpty() {
        return stateList.isEmpty();
    }

    public void addStateList(List<AlgorithmState> stateList) {
        this.stateList = stateList;
    }
}
