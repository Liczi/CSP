package si.csp.utils;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public abstract class BoardIterator {
    private int unitCost;

    protected int N;
    protected Pointer current;
    protected long cost;


    public BoardIterator(int unitCost) {
        this.cost = 0;
        this.unitCost = unitCost;
    }


    public abstract boolean hasNext();

    public abstract boolean hasPrevious();

    public abstract Pointer next();

    public abstract Pointer previous();

    public abstract void resetAt(Pointer pointer);

    public abstract BoardIterator copyFrom(Pointer startingPointer);

    protected void increaseCost() {
        cost += unitCost;
    }

    public long getCost() {
        return cost;
    }

    public void setN(int N) {
        this.N = N;
    }
}
