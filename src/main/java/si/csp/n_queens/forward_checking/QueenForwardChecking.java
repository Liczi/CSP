package si.csp.n_queens.forward_checking;

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
public class QueenForwardChecking extends QueensCSPStrategy {
    private int[][] boardMask;

    public QueenForwardChecking(int n, BoardIterator iterator) {
        super(n, iterator);

        boardMask = new int[N][N];
        iterator.setBoardMask(boardMask);

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
        setField(current, true);
        currentQueens++;
        if (currentQueens >= N) { //mamy rozwiazanie
            saveSolution(getCurrentSolution());
            clearRow(current.getRowIndex());
            currentQueens--;
            return iterator.previousLevel(); //doesnt need check
        } else { //to jeszcze nie rozwiązanie
            updateCollisionMask(current, 1);
            iterator.nextLevel(); //todo czy mozna byc exception ?

            if (iterator.hasNext()) {
                return stepForward(iterator.next());
            } else { //no possible position in this row
                return current;
            }
        }
    }

    private Pointer stepBackward(Pointer current) {
        if (iterator.hasNext()) {
            current = iterator.next();
            setField(current, false);
            currentQueens--;
            updateCollisionMask(current, -1);
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return stepBackward(current);
            }
        } else if (iterator.hasPreviousLevel()) {
            return stepBackward(iterator.previousLevel());
        } else
            return null; //todo inspect
    }

    private void updateCollisionMask(Pointer current, int value) {
        //horizontal, vertical and diagonal sweep
        for (int i = 0; i < this.board.length; i++) {
            //it checks also the current pointer (if there is already a queen, constraints not met)
            if (current.getColIndex() != i) {
                boardMask[i][current.getRowIndex()] += value;
            }
            if (current.getRowIndex() != i) {
                boardMask[current.getColIndex()][i] += value;
            }

            if (i > 0) {
                Stream.of(
                        Pointer.build(current.getColIndex() - i, current.getRowIndex() - i, N),
                        Pointer.build(current.getColIndex() + i, current.getRowIndex() - i, N),
                        Pointer.build(current.getColIndex() - i, current.getRowIndex() + i, N),
                        Pointer.build(current.getColIndex() + i, current.getRowIndex() + i, N)
                )
                        .filter(Objects::nonNull)
                        .forEach(pointer -> boardMask[pointer.getColIndex()][pointer.getRowIndex()] += value);

            }
        }
    }
}
