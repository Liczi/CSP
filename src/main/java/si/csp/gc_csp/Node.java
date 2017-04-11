package si.csp.gc_csp;

import java.util.Arrays;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Node {
    private static int[] domain;

    private int current;
    private int[] possible;
    private int lastPossible;

    public Node() {
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

    public int getLastPossible() {
        return lastPossible;
    }

    public void setLastPossible(int lastPossible) {
        this.lastPossible = lastPossible;
    }

    public int getDomainSize() {
        return domain.length;
    }

    //sets possible all values from domain
    void restoreDomain() {
        current = 0;
        for (int i = 0; i < domain.length; i++) {
            possible[i] = domain[i];
        }
        lastPossible = possible.length - 1;
    }

    void removeFromDomain(int value) {
        for (int i = 0; i < possible.length; i++) {
            if (possible[i] == value) {
                possible[i] = 0;
                if (lastPossible == i)
                    lastPossible--;
                return;
            }
        }
    }

    void decrementLastPossible() {
        for (int i = lastPossible - 1; i > -1; i--) {
            if (possible[i] > 0) {
                lastPossible = i;
                return;
            }

        }
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
