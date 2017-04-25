package si.csp.n_queens.forward_checking;

import si.csp.n_queens.QueensCSPStrategy;
import si.csp.utils.BoardIterator;

import java.util.List;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public class QueenForwardChecking extends QueensCSPStrategy {
    private int[][] boardMask;

    public QueenForwardChecking(int n, BoardIterator iterator) {
        super(n, iterator);

        boardMask = new int[N][N];
    }

    @Override
    public List<int[][]> solve() {
        return null;
    }
}
