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
        this.firstNode = new Pointer(firstNode);
        this.secondNode = new Pointer(secondNode);

//        Pointer left = new Pointer(0,0);
//        Pointer right = new Pointer(1,1);
//
//        if(left.equals(firstNode) && right.equals(secondNode) || left.equals(secondNode) &&  right.equals(firstNode))
//            this.secondNode = secondNode;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public Pointer getFirstNode() {
        return firstNode;
    }

    public Pointer getSecondNode() {
        return secondNode;
    }

    public boolean contains(Pointer pointer) {
        return firstNode.equals(pointer) || secondNode.equals(pointer);
    }

    /**
     * Used for Set purposes
     *
     * @param o other
     * @return true if the edge represents the same pair of values as <e>o</e>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;
        return (firstValue == edge.firstValue && secondValue == edge.secondValue) ||
                (firstValue == edge.secondValue && secondValue == edge.firstValue);
    }

    @Override
    public int hashCode() {
        return firstValue + secondValue;
    }
}
