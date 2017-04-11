package si.csp.gc_csp;

import si.csp.utils.GraphIterator;

import java.util.*;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public abstract class CSPStrategy {

    protected int N;
    protected GraphIterator iterator;
    protected ColorPairDuplicateManager pairDuplicateManager;

    public CSPStrategy(int n, GraphIterator iterator) {
        this.iterator = iterator;
        this.N = n;
        iterator.setN(N);
        pairDuplicateManager = new ColorPairDuplicateManager(domainSize);
    }

    protected boolean checkConstraints(Pointer current, int value) {
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

        if(Arrays.stream(neighbourValues).distinct().count() != neighbourValues.length)
            return false;

        if(Arrays.stream(neighbourValues).anyMatch(neighbourValue -> pairDuplicateManager.hasColorsPair(neighbourValue, value)))
            return false;

        Arrays.stream(neighbourValues).forEach(neighbourValue -> pairDuplicateManager.addPair(neighbourValue, value));

        return true;
    }

    protected void deleteEdges(Pointer pointer) {
        int currentValue = getNodeValue(pointer);

        Pointer[] neighbours = getNeighbours(pointer);
        for (Pointer neighbour : neighbours) {
            pairDuplicateManager.deletePair(getNodeValue(neighbour), currentValue);
        }
    }

    public long getCost() {
        return iterator.getCost();
    }

    abstract public List<int[][]> solve();
}

