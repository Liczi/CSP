package si.csp.gc_csp.backtracking;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Node {
    private static int[] domain;

    private int current;
    private int lastPossible;

    public Node() {
        current = 0;
        lastPossible = domain.length - 1;
    }

    public static void setDomain(int[] domain) {
        Node.domain = domain;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLastPossible() {
        return lastPossible;
    }

    public void setLastPossible(int lastPossible) {
        this.lastPossible = lastPossible;
    }

    public int getDomainSize() {
        return domain.length;
    }

    void setCurrentAsLastPossible() {
        setCurrent(domain[lastPossible]);
    }

    int getLastPossibleValue() {
        return domain[lastPossible];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;
        return current == node.current;
    }

    @Override
    public int hashCode() {
        return current;
    }
}
