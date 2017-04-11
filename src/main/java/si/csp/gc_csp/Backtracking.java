package si.csp.gc_csp;

import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.*;

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
            currentNode.setCurrentAsLastPossible();
            if (checkConstraints(current)) {
                if (iterator.hasNext()) {
                    //increase cost (we enter new node)
                    return stepForward(iterator.next());
                } else { //we have a winner!
                    //add current solution to results, decrease domain and run again same node
                    saveSolution(getCurrentSolution());
                    currentNode.setCurrent(0);
                    currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                    deleteEdges(current);
                    return stepForward(current);
                }
            } else { //selected value violates constraints
                currentNode.setCurrent(0);
                currentNode.setLastPossible(currentNode.getLastPossible() - 1);
                deleteEdges(current);
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
        currentNode.setCurrent(0);
        currentNode.setLastPossible(currentNode.getLastPossible() - 1);
        deleteEdges(current);

        if (currentNode.getLastPossible() > -1) {
            currentNode.setCurrentAsLastPossible();
            if (checkConstraints(current)) {
                return iterator.next();
            } else { //constraints not met, next value
                deleteEdges(current);
                return stepBackward(current);
            }
            //there must be next, because we did step back!
        } //there are no more values to pick, reset this node and stepBackward
        else {
            if (iterator.hasPrevious()) {
                currentNode.setLastPossible(currentNode.getDomainSize() - 1);
                deleteEdges(current);
                return stepBackward(iterator.previous());
            } //if this condition not met, end of program
        }

        return null;
    }

    /**
     * Checks constraints for all nodes till current Pointer
     *
     * @param current node
     * @return false if constraints are violated
     */

    private boolean checkConstraints(Pointer current) {
        return Arrays.stream(getNeighbours(current))
                .map(this::getNodeValue)
                .noneMatch(value -> value == getNodeValue(current))
                && updateEdges(current);
    }

    /**
     * Updates the edges Set and checks the uniqueness of edges
     *
     * @param current pointer to the current node
     * @return false if uniqueness of set is violated
     */
    private boolean updateEdges(Pointer current) {
        int currentValue = getNodeValue(current);

        Pointer[] neighbours = getNeighbours(current);
        Set<Edge> newEdges = new HashSet<>();

        for (Pointer p : neighbours) {
            if (!newEdges.add(new Edge(getNodeValue(p), currentValue, p, current)))
                return false;
        }

        if (newEdges.stream().noneMatch(newEdge -> edges.contains(newEdge))) {
            edges.addAll(newEdges);
            return true;
        }
        return false;
    }

    private void saveSolution(int[][] solution) {
        result.add(solution);
    }
}
