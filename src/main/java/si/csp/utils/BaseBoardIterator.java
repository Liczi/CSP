package si.csp.utils;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public class BaseBoardIterator extends BoardIterator {

    public BaseBoardIterator(int unitCost) {
        super(unitCost);
        this.N = -1; //to avoid initialization errors
        this.cost = 0;
        this.current = new Pointer(-1, 0);
    }

    @Override
    public boolean hasNext() {
        return current.getColIndex() < N - 1;
    }

    @Override
    public boolean hasPrevious() {
        return current.getColIndex() > 0;
    }

    @Override
    public boolean hasNextLevel() {
        return current.getRowIndex() < N - 1;
    }

    @Override
    public boolean hasPreviousLevel() {
        return current.getRowIndex() > 0;
    }

    @Override
    public Pointer nextLevel() {
        if (current.getRowIndex() >= N - 1) {
            throw new IllegalStateException("Called nextLevel on the last level");
        }
        increaseCost();
        current.setRowIndex(current.getRowIndex() + 1);
        current.setColIndex(0);
        return current;
    }

    @Override
    public Pointer previousLevel() {
        if (current.getRowIndex() <= 0) {
            throw new IllegalStateException("Called previousLevel on the first level");
        }
        increaseCost();
        current.setRowIndex(current.getRowIndex() - 1);
        current.setColIndex(0);
        return current;
    }


    @Override
    public Pointer next() {
        if (current.getColIndex() < N - 1) {
            current.setColIndex(current.getColIndex() + 1);
            increaseCost();
            return current;
        }
        throw new IllegalStateException("Called next on the last element");
    }

    @Override
    public Pointer previous() {
        if (current.getColIndex() > 0) {
            current.setColIndex(current.getColIndex() - 1);
            increaseCost();
            return current;
        }
        throw new IllegalStateException("Called previous on the first element");
    }

    @Override
    public void resetAt(Pointer pointer) {
        this.cost = 0;
        this.current = pointer;
    }

    @Override
    public BoardIterator copyFrom(Pointer startingPointer) {
        BaseBoardIterator toReturn = new BaseBoardIterator((int) this.getCost());
        toReturn.setN(N);
        toReturn.current = new Pointer(startingPointer);
        return toReturn;
    }
}
