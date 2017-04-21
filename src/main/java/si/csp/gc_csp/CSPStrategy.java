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
    }



    public long getCost() {
        return iterator.getCost();
    }

    abstract public List<int[][]> solve();
}

