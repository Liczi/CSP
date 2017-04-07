package si.csp.gc_csp;

import si.csp.utils.Pointer;

/**
 * @author Jakub Licznerski
 *         Created on 07.04.2017.
 */
public class Edge {
    private int firstValue;
    private int secondValue;
    private Pointer firstNode;
    private Pointer secondNode;

    public Edge(int firstValue, int secondValue, Pointer firstNode, Pointer secondNode) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;
        return firstValue == edge.firstValue && secondValue == edge.secondValue ||
                firstValue == edge.secondValue && secondValue == edge.firstValue;
    }

    @Override
    public int hashCode() {
        int result = firstValue;
        result = 31 * result + secondValue;
        return result;
    }
}
