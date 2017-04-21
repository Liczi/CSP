package si.csp.gc_csp.backtracking;

import java.util.Arrays;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class NodeB {
    private static int[] domain;

    private int current;
    private int[] possible;
    private int lastPossible;

    public NodeB() {
        current = 0;
        possible = Arrays.copyOf(domain, domain.length);
        lastPossible = domain.length - 1;
    }

    public static void setDomain(int[] domain) {
        NodeB.domain = domain;
    }

    public int getLastPossible() {
        return lastPossible;
    }

    public int getLastPossibleValue() {
        return domain[lastPossible];
    }

    public void setLastPossible(int lastPossible) {
        this.lastPossible = lastPossible;
    }

    public int getDomainSize() {
        return domain.length;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    void setCurrentAsLastPossible() {
        current = possible[lastPossible];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeB)) return false;

        NodeB node = (NodeB) o;
        return current == node.current;
    }

    @Override
    public int hashCode() {
        return current;
    }
}
