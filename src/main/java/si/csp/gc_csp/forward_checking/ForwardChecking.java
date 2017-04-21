package si.csp.gc_csp.forward_checking;

import si.csp.gc_csp.CSPStrategy;
import si.csp.gc_csp.ColorPairDuplicateManager;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class ForwardChecking extends CSPStrategy {

    //todo powrot zrobic poprzez usuniecie wartosci
    private List<int[][]> result;
    private NodeF[][] graph;


    public ForwardChecking(int n, GraphIterator iterator) {
        super(n, iterator);

        int domainSize = N % 2 == 0 ? 2 * N : 2 * N + 1;
        int[] domain = new int[domainSize];

        //create domain {1, ... , domainSize}
        for (int i = 1; i <= domainSize; i++)
            domain[i - 1] = i;
        NodeF.setDomain(domain);

        graph = new NodeF[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[j][i] = new NodeF();
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

    private Pointer stepForward(Pointer current) {
        NodeF currentNode = getNodeAt(current);

        //todo implement proper method
//        currentNode.setCurrentAsNextPossible();
        if (iterator.hasNext()) {
            Pointer next = iterator.next();
            Pointer[] successors = getSuccessors(current);
            if (Arrays.stream(successors)
                    .map(neighbour -> updateDomain(currentNode.getCurrent(), neighbour))
                    .reduce(Boolean::logicalAnd)
                    .get()) { //all ok, next
                return stepForward(next);
            } else {//updating domain gives one node no available values
                // reset successors
                for (Pointer successor :
                        successors) {
                    getNodeAt(successor).restoreDomain();
                }
                currentNode.removeCurrentFromDomain();
                currentNode.setCurrent(0);
                return stepForward(current);
            }
        } else { //we are at the last node, save all current domain values to the result

            //todo add results and reset this node
            return iterator.previous();
        }
    }

    private Pointer stepBackward(Pointer pointer) {
        NodeF currentNode = getNodeAt(pointer);

        //todo rethink
        if (currentNode.getAvailableSize() > 0) {
            //
        } else {
            if (iterator.hasPrevious()) {
                //todo reset this node
                return iterator.previous();
            } else { //we are at starting node
                //todo check if there are values to assign (probably no recursion)
                return null;
            }
        }

        //todo what to return here?
        return null;
    }

    //todo consider passing Node objects

    /**
     * Updates the domain considering previous node (from)
     *
     * @param previousValue
     * @param toUpdate
     * @return if the toUpdate node has more values possible
     */
    private boolean updateDomain(int previousValue, Pointer toUpdate) {
        //todo rethink
//        NodeF toUpdateNode = getNodeAt(toUpdate);
//
//        //neighbour
//        toUpdateNode.removeFromDomain(previousValue);
//
//        //edges
//        int[] possible = toUpdateNode.getPossible();
//        for (int i = 0; i < possible.length; i++) {
//            int value = possible[i];
//            if (value > 0) {
//                if (edges.stream()
//                        .anyMatch(edge -> edge.contains(previousValue, value)))
//                    possible[i] = 0;
//            }
//        }
//
//        return toUpdateNode.nextPossible() > -1;
        return false;
    }

    /**
     * Checks constraints for all nodes till current Pointer
     *
     * @param current node
     * @return false if constraints are violated
     */

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

        if (Arrays.stream(neighbourValues).distinct().count() != neighbourValues.length)
            return false;

        if (Arrays.stream(neighbourValues).anyMatch(neighbourValue -> pairDuplicateManager.hasColorsPair(neighbourValue, value)))
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

    private NodeF getNodeAt(Pointer pointer) {
        return graph[pointer.getColIndex()][pointer.getRowIndex()];
    }

    /**
     * @return solution based on the current graph state
     */
    int[][] getCurrentSolution() {
        return Arrays.stream(graph)
                .map(nodes -> Arrays.stream(nodes)
                        .mapToInt(NodeF::getCurrent)
                        .toArray())
                .toArray(int[][]::new);
    }
}
