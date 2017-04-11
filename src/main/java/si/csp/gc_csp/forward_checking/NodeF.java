package si.csp.gc_csp.forward_checking;

import si.csp.gc_csp.backtracking.NodeB;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class NodeF {
    protected static int[] domain;

    private int current;
    private boolean[] available;

    public NodeF() {
        available = new boolean[domain.length];
        restoreDomain();
    }

    public static void setDomain(int[] domain) {
        NodeF.domain = domain;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getDomainSize() {
        return domain.length;
    }

    //sets possible all values from domain
    public void restoreDomain() {
        current = 0;
        for (int i = 0; i < available.length; i++) {
            available[i] = true;
        }
    }

    public void removeFromDomain(int index) {
        available[index] = false;
    }

    public void removeCurrentFromDomain() {
        available[current - 1] = false;
    }

    //method vulnerable to not standard domain
    public int nextPossible() {
        for (int i = available.length - 1; i > -1; i++) {
            if (available[i]) {
                return i + 1;
            }
        }
        return -1;
    }

    public int getAvailableSize() {
        int size = 0;
        for (int i = 0; i < available.length; i++) {
            if (available[i])
                size++;
        }
        return size;
    }
}
