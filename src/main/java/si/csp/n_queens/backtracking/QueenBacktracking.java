package si.csp.n_queens.backtracking;

import si.csp.n_queens.QueensCSPStrategy;
import si.csp.utils.BoardIterator;
import si.csp.utils.Pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 25.04.2017.
 */
public class QueenBacktracking extends QueensCSPStrategy {
    private List<boolean[][]> result;

    public QueenBacktracking(int n, BoardIterator iterator) {
        super(n, iterator);
    }

    @Override
    public List<boolean[][]> solve() {
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

        if (checkConstraints(current)) {
            setField(current, true);
            if (iterator.hasNext()) { //set and continue
                return stepForward(iterator.next());
            } else { //we are at the last node, add result
                saveSolution(getCurrentSolution());
                setField(current, null);
                return iterator.previous();
            }
        } else { //constraints not met for this field

        }
        return null;
    }

    private Pointer stepBackward(Pointer current) {
        return null;
    }

    private boolean checkConstraints(Pointer current) {
        //horizontal, vertical and diagonal sweep
        for (int i = 0; i < this.board.length; i++) {
            if (current.getColIndex() != i && board[i][current.getRowIndex()]) {
                return false;
            }
            if (current.getRowIndex() != i && board[current.getColIndex()][i]) {
                return false;
            }

            if (Stream.of(
                    Pointer.build(current.getColIndex() - i, current.getRowIndex() - i, N),
                    Pointer.build(current.getColIndex() + i, current.getRowIndex() - i, N),
                    Pointer.build(current.getColIndex() - i, current.getRowIndex() + i, N),
                    Pointer.build(current.getColIndex() + i, current.getRowIndex() + i, N)
            )
                    .filter(Objects::nonNull)
                    .anyMatch(this::isQueenAt)) {
                return false;
            }
        }

        return true;
    }

    private boolean[][] getCurrentSolution() {
        boolean[][] result = new boolean[N][N];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }

    private void saveSolution(boolean[][] solution) {
        result.add(solution);
    }
}
