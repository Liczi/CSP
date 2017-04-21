package si.csp.gc_csp.backtracking;

import si.csp.gc_csp.CSPStrategy;
import si.csp.gc_csp.ColorPairDuplicateManager;
import si.csp.gc_csp.forward_checking.NodeF;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Backtracking extends CSPStrategy {

    private List<int[][]> result;
    private NodeB[][] graph;

    public Backtracking(int n, GraphIterator iterator) {
        super(n, iterator);

        int domainSize = N % 2 == 0 ? 2 * N : 2 * N + 1;
        int[] domain = new int[domainSize];

        //create domain {1, ... , domainSize}
        for (int i = 1; i <= domainSize; i++)
            domain[i - 1] = i;
        NodeB.setDomain(domain);

        graph = new NodeB[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[j][i] = new NodeB();
            }
        }

        pairDuplicateManager = new ColorPairDuplicateManager(domainSize);
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
        NodeB currentNode = getNodeAt(current);

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
        NodeB currentNode = getNodeAt(current);

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


    private boolean checkConstraints(Pointer current, int value) {
        return Arrays.stream(getNeighbours(current))
                .map(this::getNodeValue)
                .noneMatch(neighbourValue -> neighbourValue == value)
                && updateEdges(current, value);
    }

    /**
     * Updates the edges Set and checks the uniqueness of edges
     *
     * @param current pointer to the current node
     * @return false if uniqueness of set is violated
     */
    private boolean updateEdges(Pointer current, int value) {
        int[] neighbourValues = Arrays.stream(getNeighbours(current)).mapToInt(this::getNodeValue).toArray();

        if (Arrays.stream(neighbourValues).anyMatch(neighbourValue -> pairDuplicateManager.hasColorsPair(neighbourValue, value)))
            return false;

        if (Arrays.stream(neighbourValues).distinct().count() != neighbourValues.length)
            return false;

        Arrays.stream(neighbourValues).forEach(neighbourValue -> pairDuplicateManager.addPair(neighbourValue, value));

        return true;
    }

    private void deleteEdges(Pointer pointer) {
        int currentValue = getNodeValue(pointer);

        Pointer[] neighbours = getNeighbours(pointer);
        for (Pointer neighbour : neighbours) {
            pairDuplicateManager.deletePair(getNodeValue(neighbour), currentValue);
        }
    }

    private void saveSolution(int[][] solution) {
        result.add(solution);
    }

    /**
     * Return an array of neighboring Nodes, which values are set (non-zero)
     *
     * @param pointer pointer on the Node
     * @return array of neighbours
     */
    Pointer[] getNeighbours(Pointer pointer) {
        Stream.Builder<Pointer> builder = Stream.builder();
        for (int i = -1; i < 2; i += 2) {
            builder.add(Pointer.build(pointer.getColIndex() + i, pointer.getRowIndex(), N));
        }
        for (int i = -1; i < 2; i += 2) {
            builder.add(Pointer.build(pointer.getColIndex(), pointer.getRowIndex() + i, N));
        }

        return builder.build()
                .filter(ptr -> ptr != null && getNodeValue(ptr) > 0)
                .toArray(Pointer[]::new);
    }

    //todo method vulnerable to traversing direction
    Pointer[] getSuccessors(Pointer pointer) {
        return Stream.of(
                Pointer.build(pointer.getColIndex() + 1, pointer.getRowIndex(), N),
                Pointer.build(pointer.getColIndex(), pointer.getRowIndex() + 1, N)
        )
                .filter(Objects::nonNull)
                .toArray(Pointer[]::new);
    }

    private int getNodeValue(Pointer pointer) {
        return getNodeAt(pointer).getCurrent();
    }


    private NodeB getNodeAt(Pointer pointer) {
        return graph[pointer.getColIndex()][pointer.getRowIndex()];
    }

    /**
     * @return solution based on the current graph state
     */
    int[][] getCurrentSolution() {
        return Arrays.stream(graph)
                .map(nodes -> Arrays.stream(nodes)
                        .mapToInt(NodeB::getCurrent)
                        .toArray())
                .toArray(int[][]::new);
    }
}
