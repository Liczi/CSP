package si.csp.gc_csp;

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
        iterator.initialize(N);
        stepForward(iterator.next());

        //todo implement body
        return result;
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
            if (checkConstraints(current)) {
                if (iterator.hasNext()) {
                    //todo increase cost (we enter new node)
                    stepForward(iterator.next());
                } else { //we have a winner!
                    //todo add current configuration to results, decrease domain and run again same node
                    saveCurrent();
                    currentNode.setCurrent(0);
                    currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                    stepForward(current);
                }
            } else { //selected value violates constraints
                currentNode.setCurrent(0);
                currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                stepForward(current);
            }
        }
        //there is no possible value to assign to this node
        //restore all possible values, go back
        else {
            currentNode.setCurrent(0);
            currentNode.setLastPossible(currentNode.getDomainSize() - 1);
            if (!iterator.hasPrevious()) {
                return; //todo verify if return needed
            } else {
                //todo update edges (delete all edges from current pointer)
                //todo increase cost (we enter new node)
                stepBackward(iterator.previous());
            }
        }
    }

    //delete current value and stepForward again
    private void stepBackward(Pointer current) {
        Node currentNode = getNodeAt(current);

        //reject current value, because it doesnt match any further configuration
        currentNode.setCurrent(0);
        currentNode.setLastPossible(currentNode.getLastPossible() - 1);

        if (currentNode.getLastPossible() > -1) {
            currentNode.setCurrentAsLastPossible();
            if (checkConstraints(current)) { //ok, go on
                stepForward(iterator.next());
            } else { //constraints not met, next value
                stepBackward(current);
            }
            //there must be next, because we did step back!
        } //there are no more values to pick, reset this node and stepBackward
        else {
            if (iterator.hasPrevious()) {
                currentNode.setLastPossible(currentNode.getDomainSize() - 1);
                stepBackward(iterator.previous());
            } //if this condition not met, end of program
        }
        //todo consider other options
    }
}
