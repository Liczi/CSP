package si.csp.utils;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Pointer {
    private int colIndex;
    private int rowIndex;

    public Pointer(int colIndex, int rowIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    //copy constructor
    public Pointer(Pointer pointer) {
        this.rowIndex = pointer.getRowIndex();
        this.colIndex = pointer.getColIndex();
    }

    public static Pointer build(int colIndex, int rowIndex, int N) {
        if (colIndex > N - 1 ||
                colIndex < 0 ||
                rowIndex > N - 1 ||
                rowIndex < 0) {
            return null;
        }
        return new Pointer(colIndex, rowIndex);
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pointer))
            return false;

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
