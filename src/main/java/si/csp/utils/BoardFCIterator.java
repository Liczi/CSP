package si.csp.utils;

import sun.plugin.dom.exception.InvalidStateException;

/**
 * @author Jakub Licznerski
 *         Created on 26.04.2017.
 */
public class BoardFCIterator extends BoardIterator {
    private int[][] boardMask;


    public BoardFCIterator(int unitCost) {
        super(unitCost);
        this.N = -1; //to avoid initialization errors
        this.cost = 0;
        this.current = new Pointer(-1, 0);
    }


    @Override
    public boolean hasNext() {
        return current.getColIndex() < N - 1 && hasNextInRow(current);
    }

    @Override
    public boolean hasNextLevel() {
        return current.getRowIndex() < N - 1;
    }

    @Override
    public boolean hasPreviousLevel() {
        return current.getRowIndex() > 0;
    }

    /**
     * sets pointer at the position previous to first node
     *
     * @return
     */
    @Override
    public Pointer nextLevel() {
        if (current.getRowIndex() >= N - 1) {
            throw new IllegalStateException("Called nextLevel on the last level");
        }
        current.setRowIndex(current.getRowIndex() + 1);
        current.setColIndex(-1);
        return current; //todo check if it doesnt ever return null
    }

    @Override
    public Pointer previousLevel() {
        if (current.getRowIndex() <= 0) {
            throw new IllegalStateException("Called previousLevel on the first level");
        }
        current.setRowIndex(current.getRowIndex() - 1);
        current.setColIndex(-1);
        return current;
    }


    @Override
    public Pointer next() {
        if (current.getColIndex() < N - 1) {
            current.setColIndex(current.getColIndex() + 1);
            increaseCost();
            return nextInRow(current);
        }
        throw new IllegalStateException("Called next on the last element");
    }

    @Override
    public Pointer step() {
        throw new UnsupportedOperationException("Unsupported");
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

    @Override
    public void setBoardMask(int[][] boardMask) {
        this.boardMask = boardMask;
    }

    /**
     * Goes to the next allowed field of board in the row or returns null
     *
     * @param pointer
     * @return
     */
    private Pointer nextInRow(Pointer pointer) {
        for (int i = pointer.getColIndex(); i < boardMask.length; i++) {
            if (boardMask[i][pointer.getRowIndex()] <= 0) { //there is no collision
                current.setColIndex(i);
                current.setRowIndex(pointer.getRowIndex());
                return current;
            }
        }
        throw new InvalidStateException("Called next row without check");
    }

    private boolean hasNextInRow(Pointer current) {
        for (int i = current.getColIndex() + 1; i < boardMask.length; i++) {
            if (boardMask[i][current.getRowIndex()] <= 0) { //there is no collision
                return true;
            }
        }
        return false;
    }
}
