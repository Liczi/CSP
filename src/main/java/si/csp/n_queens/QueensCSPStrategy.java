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
    private Boolean[][] board;


    public QueensCSPStrategy(int n, BoardIterator iterator) {
        this.iterator = iterator;
        this.N = n;
        iterator.setN(N);

        board = new Boolean[N][N];
    }


    protected boolean isQueenAt(Pointer pointer) {
        return board[pointer.getColIndex()][pointer.getRowIndex()];
    }

    protected void setQueenAt(Pointer pointer) {
        board[pointer.getColIndex()][pointer.getRowIndex()] = true;
    }

    public long getCost() {
        return iterator.getCost();
    }

    abstract public List<int[][]> solve();
}
