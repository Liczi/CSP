package si.csp.gc_csp.forward_checking;

import si.csp.gc_csp.GraphCSPStrategy;
import si.csp.gc_csp.ColorPairDuplicateManager;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class GraphForwardChecking extends GraphCSPStrategy {

    //todo powrot zrobic poprzez usuniecie wartosci
    private List<int[][]> result;
    private NodeF[][] graph;


    public GraphForwardChecking(int n, GraphIterator iterator) {
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

        if (!currentNode.setCurrentAsNextPossible())
            return iterator.previous();

        if (iterator.hasNext()) {
            //add edges for current
            Pointer[] successors = iterator.getSuccessors(current);
            for (Pointer successor :
                    successors) {
                getNodeAt(successor).removeFromDomain(currentNode.getCurrent());
            }
            boolean valid = true;
            for (Pointer neighbour :
                    getNeighbours(current)) {
                int neighbourValue = getNodeValue(neighbour);
                pairDuplicateManager.addPair(currentNode.getCurrent(), neighbourValue);
                valid &= updateConsequentForAdding(current, currentNode.getCurrent(), neighbourValue);
            }

            if (Arrays.stream(successors)
                    .map(neighbour -> updateDomainDeleting(currentNode.getCurrent(), neighbour))
                    .reduce(Boolean::logicalAnd)
                    .get()
                    && valid) {
                return stepForward(iterator.next());
            } else {//updating domain deleted all values from a node
                // reset successors
                for (Pointer successor :
                        successors) {
                    getNodeAt(successor).addToDomain(currentNode.getCurrent());
                    updateDomainAdding(currentNode.getCurrent(), successor);
                }

                deleteEdges(current);
                currentNode.removeCurrentFromDomain();
                currentNode.setCurrent(0);

                if (currentNode.getAvailableSize() > 0)
                    return stepForward(current);
                else {
                    return current;
                }
            }
        } else { //we are at the last node, save all current domain values to the result
            boolean[] available = currentNode.getAvailable();

            for (int i = 0; i < available.length; i++) {
                if (available[i]) {
                    currentNode.setCurrent(i + 1);
                    saveSolution(getCurrentSolution());
                }
            }
            currentNode.setCurrent(0);
            return iterator.previous();
        }
    }

    private Pointer stepBackward(Pointer current) {
        NodeF currentNode = getNodeAt(current);

        if (currentNode.getCurrent() != 0) {
            deleteEdges(current);
            currentNode.removeCurrentFromDomain();
            currentNode.setCurrent(0);

            for (Pointer successor :
                    iterator.getSuccessors(current)) {
                restoreValidDomain(successor);
            }
        }
        if (currentNode.getAvailableSize() > 0) {
            //same as in stepforward
            return current;
        } else {
            if (iterator.hasPrevious()) {
                restoreValidDomain(current);
                return stepBackward(iterator.previous());
            } else { //we are at starting node and no values to assign
                return null;
            }
        }
    }

    /**
     * @param start  node which we added
     * @param value1 left node value
     * @param value2 right node value
     * @return
     */

    private void updateConsequentForDeleting(Pointer start, int value1, int value2) {
        GraphIterator iterator = this.iterator.copyFrom(start);

        while (iterator.hasNext()) {
            Pointer current = iterator.next();

            Pointer[] neighbours = getNeighbours(current);

            for (Pointer neighbour : neighbours) {
                int neighbourValue = getNodeValue(neighbour);

                if (value1 == neighbourValue) {
                    getNodeAt(current).addToDomain(value2);
                } else if (value2 == neighbourValue) {
                    getNodeAt(current).addToDomain(value1);
                }
            }

        }
    }

    private boolean updateConsequentForAdding(Pointer start, int value1, int value2) {
        GraphIterator iterator = this.iterator.copyFrom(start);

        while (iterator.hasNext()) { //todo check alse if there is are possible neighbours
            Pointer current = iterator.next();

            Pointer[] neighbours = getNeighbours(current);

            for (Pointer neighbour : neighbours) {
                int neighbourValue = getNodeValue(neighbour);

                if (value1 == neighbourValue) {
                    NodeF nodeAt = getNodeAt(current);
                    nodeAt.removeFromDomain(value2);
                    if (nodeAt.nextPossible() < 0)
                        return false;
                } else if (value2 == neighbourValue) {
                    NodeF nodeAt = getNodeAt(current);
                    nodeAt.removeFromDomain(value1);
                    if (nodeAt.nextPossible() < 0)
                        return false;
                }
            }

        }

        return true;
    }

    private void restoreValidDomain(Pointer current) {
        NodeF currentNode = getNodeAt(current);

        currentNode.restoreDomain();
        Pointer[] neighbours = getNeighbours(current);
        for (Pointer neighbour :
                neighbours) {
            currentNode.removeFromDomain(getNodeValue(neighbour));
        }
        boolean[] available = currentNode.getAvailable();
        for (Pointer neighbour :
                neighbours) {
            int neighbourValue = getNodeValue(neighbour);
            for (int i = 0; i < available.length; i++) {
                if (available[i])
                    if (pairDuplicateManager.hasColorsPair(neighbourValue, i + 1))
                        currentNode.removeFromDomain(i + 1);
            }
        }
    }

    /**
     * Updates the domain considering previous node (from)
     *
     * @param value
     * @param toUpdate
     * @return if the toUpdate node has more values possible
     */
    private boolean updateDomainDeleting(int value, Pointer toUpdate) {
        NodeF toUpdateNode = getNodeAt(toUpdate);
        Pointer upper = Pointer.build(toUpdate.getColIndex(), toUpdate.getRowIndex() - 1, N);

        //edges
        boolean[] available = toUpdateNode.getAvailable();
        for (int i = 0; i < available.length; i++) {
            if (available[i]) {
                if (pairDuplicateManager.hasColorsPair(value, i + 1))
                    toUpdateNode.removeFromDomain(i + 1);
                if (upper != null) {
                    if (pairDuplicateManager.hasColorsPair(getNodeValue(upper), i + 1))
                        toUpdateNode.removeFromDomain(i + 1);
                }

            }
        }

        return toUpdateNode.nextPossible() > -1;
    }

    private void updateDomainAdding(int value, Pointer toUpdate) {
        NodeF toUpdateNode = getNodeAt(toUpdate);
        Pointer upper = Pointer.build(toUpdate.getColIndex(), toUpdate.getRowIndex() - 1, N);

        //edges
        boolean[] available = toUpdateNode.getAvailable();
        for (int i = 0; i < available.length; i++) {
            if (!available[i]) {
                if (pairDuplicateManager.hasColorsPair(value, i + 1))
                    toUpdateNode.addToDomain(i + 1);
                if (upper != null) {
                    if (pairDuplicateManager.hasColorsPair(getNodeValue(upper), i + 1))
                        toUpdateNode.addToDomain(i + 1);
                }

            }
        }
    }

//    /**
//     * Checks constraints for all nodes till current Pointer
//     *
//     * @param current node
//     * @return false if constraints are violated
//     */
//
//    private boolean checkConstraints(Pointer current, int value) {
//        return Arrays.stream(getNeighbours(current))
//                .map(this::getNodeValue)
//                .noneMatch(neighbourValue -> neighbourValue == value)
//                && updateEdges(current, value);
//    }

    private void deleteEdges(Pointer pointer) {
        int currentValue = getNodeValue(pointer);

        Pointer[] neighbours = getNeighbours(pointer);
        for (Pointer neighbour : neighbours) {
            int neighbourValue = getNodeValue(neighbour);
            pairDuplicateManager.deletePair(neighbourValue, currentValue);
            updateConsequentForDeleting(pointer, neighbourValue, currentValue);
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
    private Pointer[] getNeighbours(Pointer pointer) {
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
