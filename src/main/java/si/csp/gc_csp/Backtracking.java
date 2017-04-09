package si.csp.gc_csp;

import si.csp.Runner;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Backtracking extends CSPStrategy {

    private final int UNIT_COST = 1;

    private List<int[][]> result;

    public Backtracking(int n, GraphIterator iterator) {
        super(n, iterator);
    }

    @Override
    public List<int[][]> solve() {
        result = new ArrayList<>();
        iterator.initialize(N);
        stepForward(iterator.next());
        return result;
    }

    @Override
    protected int getUnitCost() {
        return UNIT_COST;
    }

    /**
     * Gets values from the end of domain
     *
     * @param current
     */
    private void stepForward(Pointer current) {
        Node currentNode = getNodeAt(current);

        //there is some possible value for this node
        if (currentNode.getLastPossible() > -1) {
            currentNode.setCurrentAsLastPossible();
            //Runner.displayResult(getCurrentSolution());
//            boolean constraints = ;
            if (checkConstraints(current)) {
                if (iterator.hasNext()) {
                    //increase cost (we enter new node)
                    increaseCost();
                    stepForward(iterator.next());
                } else { //we have a winner!
                    //add current solution to results, decrease domain and run again same node
                    saveSolution(getCurrentSolution());
                    currentNode.setCurrent(0);
                    currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                    deleteEdges(current);
                    stepForward(current);
                }
            } else { //selected value violates constraints
                currentNode.setCurrent(0);
                currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                deleteEdges(current);
                stepForward(current);
            }
        }
        //there is no possible value to assign to this node
        //restore all possible values, go back
        else {
            currentNode.setCurrent(0);
            currentNode.setLastPossible(currentNode.getDomainSize() - 1);

            //update edges (delete all edges from current pointer)
            //increase cost (we enter new node)
            increaseCost();
            stepBackward(iterator.previous());
        }
    }

    //delete current value and stepForward again
    private void stepBackward(Pointer current) {
        Node currentNode = getNodeAt(current);

        //reject current value, because it doesnt match any further configuration
        currentNode.setCurrent(0);
        currentNode.setLastPossible(currentNode.getLastPossible() - 1);
        deleteEdges(current);

        if (currentNode.getLastPossible() > - 1) {
            currentNode.setCurrentAsLastPossible();
            if (checkConstraints(current)) {
                stepForward(iterator.next());
            } else { //constraints not met, next value
                deleteEdges(current);
                stepBackward(current);
            }
            //there must be next, because we did step back!
        } //there are no more values to pick, reset this node and stepBackward
        else {
            if (iterator.hasPrevious()) {
                currentNode.setLastPossible(currentNode.getDomainSize() - 1);
                deleteEdges(current);
                stepBackward(iterator.previous());
            } //if this condition not met, end of program
        }
    }

    private void saveSolution(int[][] solution) {
        result.add(solution);
    }
}
