package si.csp.gc_csp;

import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class ForwardChecking extends CSPStrategy {

    //todo powrot zrobic poprzez usuniecie wartosci

    public ForwardChecking(int n, GraphIterator iterator) {
        super(n, iterator);
    }

    @Override
    public List<int[][]> solve() {
        return null;
    }

    private Pointer stepForward(Pointer current) {
        Node currentNode = getNodeAt(current);

        currentNode.setCurrentAsLastPossible();
        if (iterator.hasNext()) {
            Pointer next = iterator.next();
            Arrays.stream(getSuccessors(current))
                    .forEach(neighbour -> updateDomain(currentNode.getCurrent(), neighbour));

            return stepForward(next);
        } else { //we are at the last node, save all current domain values to the result

        }
    }

    //todo consider passing Node objects

    /**
     * Updates the domain considering previous node (from)
     *
     * @param previousValue
     * @param toUpdate
     */
    private void updateDomain(int previousValue, Pointer toUpdate) {
        Node toUpdateNode = getNodeAt(toUpdate);

        toUpdateNode.removeFromDomain(previousValue);
        int[] possible = toUpdateNode.getPossible();
        for (int i = 0; i < possible.length; i++) {
            int value = possible[i];
            if (value > 0) {
                if (edges.stream()
                        .anyMatch(edge -> edge.contains(previousValue, value)))
                    possible[i] = 0;
            }
        }
    }


//    private Pointer stepForward(Pointer current) {
//        Node currentNode = getNodeAt(current);
//
//        //there is some possible value for this node
//        if (currentNode.getLastPossible() > -1) {
//            currentNode.setCurrentAsLastPossible();
//            if (checkConstraints(current)) {
//                if (iterator.hasNext()) {
//                    //increase cost (we enter new node)
//                    return stepForward(iterator.next());
//                } else { //we have a winner!
//                    //add current solution to results, decrease domain and run again same node
//                    saveSolution(getCurrentSolution());
//                    currentNode.setCurrent(0);
//                    currentNode.setLastPossible(currentNode.getLastPossible() - 1);
//                    deleteEdges(current);
//                    return stepForward(current);
//                }
//            } else { //selected value violates constraints
//                currentNode.setCurrent(0);
//                currentNode.setLastPossible(currentNode.getLastPossible() - 1);
//                deleteEdges(current);
//                return stepForward(current);
//            }
//        }
//        //there is no possible value to assign to this node
//        //restore all possible values, go back
//        else {
//            currentNode.setCurrent(0);
//            currentNode.setLastPossible(currentNode.getDomainSize() - 1);
//
//            //update edges (delete all edges from current pointer)
//            return iterator.previous();
//        }
//    }
//
//    //delete current value and stepForward again
//    private Pointer stepBackward(Pointer current) {
//        Node currentNode = getNodeAt(current);
//
//        //reject current value, because it doesnt match any further configuration
//        currentNode.setCurrent(0);
//        currentNode.setLastPossible(currentNode.getLastPossible() - 1);
//        deleteEdges(current);
//
//        if (currentNode.getLastPossible() > -1) {
//            currentNode.setCurrentAsLastPossible();
//            if (checkConstraints(current)) {
//                return iterator.next();
//            } else { //constraints not met, next value
//                deleteEdges(current);
//                return stepBackward(current);
//            }
//            //there must be next, because we did step back!
//        } //there are no more values to pick, reset this node and stepBackward
//        else {
//            if (iterator.hasPrevious()) {
//                currentNode.setLastPossible(currentNode.getDomainSize() - 1);
//                deleteEdges(current);
//                return stepBackward(iterator.previous());
//            } //if this condition not met, end of program
//        }
//
//        return null;
//    }

}
