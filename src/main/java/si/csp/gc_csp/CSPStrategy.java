package si.csp.gc_csp;

import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public abstract class CSPStrategy {

    protected Node[][] graph;
    protected GraphIterator iterator;
    protected int N;
    //    private Pointer pointer;
    protected Set<Edge> edges;

    public CSPStrategy(int n, GraphIterator iterator) {
        initialize(n);
        this.iterator = iterator;
    }

    public void initialize(int N) {
        edges = new HashSet<>(); //todo consider sorted set
        this.N = N;
        int domainSize = N % 2 == 0 ? N : N + 1;
        int[] domain = new int[domainSize];

        //create domain {1, ... , domainSize}
        for (int i = 1; i <= domainSize; i++)
            domain[i - 1] = i;
        Node.setDomain(domain);

        graph = new Node[N][N];
    }

    /**
     * Return an array of neighboring Nodes' values
     *
     * @param pointer pointer on the Node
     * @return array of current values of neighbours
     */
//    protected Integer[] getNeighbours(Pointer pointer) {
//        Stream.Builder<Pointer> builder = Stream.builder();
//        for (int i = -1; i < 2; i += 2) {
//            builder.add(new Pointer(pointer.getRowIndex() + i, pointer.getRowIndex()));
//        }
//        for (int i = -1; i < 2; i += 2) {
//            builder.add(new Pointer(pointer.getRowIndex(), pointer.getColIndex() + i));
//        }
//
//        return builder.build()
//                .map(this::getNodeValue)
//                .filter(value -> value > 0)
//                .toArray(Integer[]::new);
//    }

    /**
     * Return an array of neighboring Nodes
     *
     * @param pointer pointer on the Node
     * @return array of neighbours
     */
    protected Pointer[] getNeighbours(Pointer pointer) {
        Stream.Builder<Pointer> builder = Stream.builder();
        for (int i = -1; i < 2; i += 2) {
            builder.add(new Pointer(pointer.getRowIndex() + i, pointer.getRowIndex()));
        }
        for (int i = -1; i < 2; i += 2) {
            builder.add(new Pointer(pointer.getRowIndex(), pointer.getColIndex() + i));
        }

        return builder.build()
                .filter(ptr -> getNodeValue(ptr) > 0)
                .toArray(Pointer[]::new);
    }

    protected int getNodeValue(Pointer pointer) {
        return getNodeAt(pointer).getCurrent();
    }

    /**
     * Checks constraints for all nodes till current Pointer
     *
     * @param current node
     * @return false if constraints are violated
     */

    protected boolean checkConstraints(Pointer current) {
        return updateEdges(current) &&
                Arrays.stream(getNeighbours(current))
                        .map(this::getNodeValue)
                        .noneMatch(value -> value == getNodeValue(current));
    }

    /**
     * Updates the edges Set and checks the uniqueness of edges
     *
     * @param current pointer to the current node
     * @return false if uniqueness of set is violated
     */
    //todo make sure to updateEdges on reseting node (on stepBackward?)
    protected boolean updateEdges(Pointer current) {
        int currentValue = getNodeValue(current);
        //todo delete this check
        if (currentValue == 0) {
            throw new IllegalStateException("Current node value not set");
        }

        Pointer[] neighbours = getNeighbours(current);
        for (Pointer p :
                neighbours) {
            if (!edges.add(new Edge(getNodeValue(p), currentValue, p, current)))
                return false;
        }

        return true;
    }

    protected Node getNodeAt(Pointer pointer) {
        return graph[pointer.getRowIndex()][pointer.getColIndex()];
    }

    /**
     * Saves the current graph state to result
     */
    protected void saveCurrent() {
        //todo implement
        throw new NotImplementedException();
    }

    abstract public List<int[][]> solve();
}

