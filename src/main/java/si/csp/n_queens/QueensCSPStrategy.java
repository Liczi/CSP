package si.csp.n_queens;

import si.csp.utils.BoardIterator;
import si.csp.utils.Pointer;

import java.util.List;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public abstract class QueensCSPStrategy {
    protected int N;
    protected BoardIterator iterator;
    protected boolean[][] board;
    protected int currentQueens;



    public QueensCSPStrategy(int n, BoardIterator iterator) {
        this.iterator = iterator;
        this.N = n;
        iterator.setN(N);
        currentQueens = 0;

        board = new boolean[N][N];
    }


    protected boolean isQueenAt(Pointer pointer) {
        return board[pointer.getColIndex()][pointer.getRowIndex()];
    }

    protected void setField(Pointer field, boolean value) {
        board[field.getColIndex()][field.getRowIndex()] = value;
    }

    protected void clearRow(int row) {
        for (int i = 0; i < board.length; i++) {
            board[i][row] = false;
        }
    }

    /**
     *
     * @param center
     * @return returns vertical, horizontal and diagonal fields from the given center
     */
    protected Pointer[] getAllInRange(Pointer center) {
        return null;
    }

    public long getCost() {
        return iterator.getCost();
    }

    abstract public List<boolean[][]> solve();
}
