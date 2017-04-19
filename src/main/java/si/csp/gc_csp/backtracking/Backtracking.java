package si.csp.gc_csp.backtracking;

import si.csp.gc_csp.CSPStrategy;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Backtracking extends CSPStrategy {

    private List<int[][]> result;

    public Backtracking(int n, GraphIterator iterator) {
        super(n, iterator);
    }

    @Override
    public List<int[][]> solve() {
        result = new ArrayList<>();

        Pointer next = iterator.next();
        while (next != null) {
            next = stepForward(next);
            if (next != null)
                next = stepBackward(next);
        }
        return result;
    }

    /**
     * Gets values from the end of domain
     *
     * @param current
     */
    private Pointer stepForward(Pointer current) {
        Node currentNode = getNodeAt(current);

        //there is some possible value for this node
        if (currentNode.getLastPossible() > -1) {
            int newValue = currentNode.getLastPossibleValue();
            if (checkConstraints(current, newValue)) {
                currentNode.setCurrentAsLastPossible();
                if (iterator.hasNext()) {
                    //increase cost (we enter new node)
                    return stepForward(iterator.next());
                } else { //we have a winner!
                    //add current solution to results, decrease domain and run again same node
                    saveSolution(getCurrentSolution());
                    currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                    deleteEdges(current);
                    currentNode.setCurrent(0);
                    return stepForward(current);
                }
            } else { //selected value violates constraints
                currentNode.setLastPossible(currentNode.getLastPossible() - 1);
//                deleteEdges(current); no need to delete edges
                currentNode.setCurrent(0);
                return stepForward(current);
            }
        }
        //there is no possible value to assign to this node
        //restore all possible values, go back
        else {
            currentNode.setCurrent(0);
            currentNode.setLastPossible(currentNode.getDomainSize() - 1);

            //update edges (delete all edges from current pointer)
            return iterator.previous();
        }
    }

    //delete current value and stepForward again
    private Pointer stepBackward(Pointer current) {
        Node currentNode = getNodeAt(current);

        //reject current value, because it doesnt match any further configuration
        currentNode.setLastPossible(currentNode.getLastPossible() - 1);
        if (currentNode.getCurrent() != 0) {
            deleteEdges(current);
            currentNode.setCurrent(0);
        }

        if (currentNode.getLastPossible() > -1) {
            int newValue = currentNode.getLastPossibleValue();
            if (checkConstraints(current, newValue)) {
                currentNode.setCurrentAsLastPossible();
                return iterator.next();
            } else { //constraints not met, next value
//                deleteEdges(current);
                return stepBackward(current);
            }
            //there must be next, because we did step back!
        } //there are no more values to pick, reset this node and stepBackward
        else {
            if (iterator.hasPrevious()) {
                currentNode.setLastPossible(currentNode.getDomainSize() - 1);
                //deleteEdges(current);
                return stepBackward(iterator.previous());
            } //if this condition not met, end of program
        }

        return null;
    }

    private void saveSolution(int[][] solution) {
        result.add(solution);
    }
}
