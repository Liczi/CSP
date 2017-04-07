package si.csp.utils;

import org.jetbrains.annotations.Contract;
import si.csp.gc_csp.CSPStrategy;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Pointer {
    private int rowIndex;
    private int colIndex;

    public Pointer(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

//    /**
//     * Moves pointer to next node
//     *
//     * @return
//     */
//    public boolean next() {
//        //todo implement body
//        return false;
//    }
//
//    /**
//     * Moves pointer to previous node
//     *
//     * @return
//     */
//    public boolean previous() {
//        //todo implement body
//        return false;
//    }

//    private boolean isValid() {
//        return !(rowIndex < 0 || rowIndex >= N) && !(colIndex < 0 || colIndex >= N);
//    }


    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pointer)) return false;

        Pointer pointer = (Pointer) o;
        return rowIndex == pointer.rowIndex && colIndex == pointer.colIndex;
    }

    @Override
    public int hashCode() {
        int result = rowIndex;
        result = 31 * result + colIndex;
        return result;
    }
}
