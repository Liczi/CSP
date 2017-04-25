package si.csp.utils;

/**
 * Defines the strategy (heuristic) for graph reading
 *
 * @author Jakub Licznerski
 *         Created on 07.04.2017.
 */
public abstract class GraphIterator {


    protected int N;

    protected Pointer current;

    public abstract boolean hasNext();

    public abstract boolean hasPrevious();

    public abstract Pointer next();

    public abstract Pointer previous();

    /**
     *
     * @param pointer
     * @return pointers to the next node and descendant
     */
    public abstract Pointer[] getSuccessors(Pointer pointer);

    public abstract GraphIterator copyFrom(Pointer startingPointer);

    protected long cost;

    private int unitCost;

    public GraphIterator(int unitCost) {
        this.cost = 0;
        this.unitCost = unitCost;
    }


    protected void increaseCost() {
        cost += unitCost;
    }

    public long getCost() {
        return cost;
    }

    public void setN(int N) {
        this.N = N;
    }

    /**
     * Resets the iterator to its initial node and returns new iterator (first before collection)
     *
     * @param N size of NxN array
     * @return
     */
}
