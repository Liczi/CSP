package si.csp.gc_csp;

import java.util.Arrays;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Node {
    private static int[] domain; //todo set outside

    private int current;
    private int[] possible;
    private int lastPossible;

    public Node() {
        //todo delete
        if (Node.domain == null)
            throw new IllegalStateException("Domain not initialized");

        current = 0;
        possible = Arrays.copyOf(domain, domain.length);
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

    public int[] getPossible() {
        return possible;
    }

//    public void setPossible(int[] possible) {
//        this.possible = possible;
//    }

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
        setCurrent(possible[lastPossible]);
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
