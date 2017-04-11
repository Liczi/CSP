package si.csp.gc_csp;

import si.csp.utils.BaseGraphIterator;
import si.csp.utils.GraphIterator;
import si.csp.utils.Pointer;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public abstract class CSPStrategy {

    protected Node[][] graph;
    protected GraphIterator iterator;
    protected int N;
    protected Set<Edge> edges;


    public CSPStrategy(int n, GraphIterator iterator) {
        this.iterator = iterator;
        initialize(n);
    }

    private void initialize(int N) {
        edges = new HashSet<>();
        this.N = N;
        iterator.setN(N);

        int domainSize = N % 2 == 0 ? 2 * N : 2 * N + 1;
        int[] domain = new int[domainSize];

        //create domain {1, ... , domainSize}
        for (int i = 1; i <= domainSize; i++)
            domain[i - 1] = i;
        Node.setDomain(domain);

        graph = new Node[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[j][i] = new Node();
            }
        }
    }

    /**
     * Return an array of neighboring Nodes, which values are set (non-zero)
     *
     * @param pointer pointer on the Node
     * @return array of neighbours
     */
    protected Pointer[] getNeighbours(Pointer pointer) {
        Stream.Builder<Pointer> builder = Stream.builder();
        for (int i = -1; i < 2; i += 2) {
            builder.add(Pointer.build(pointer.getColIndex() + i, pointer.getRowIndex(), N));
        }
        for (int i = -1; i < 2; i += 2) {
            builder.add(Pointer.build(pointer.getColIndex(), pointer.getRowIndex() + i, N));
        }

        return builder.build()
                .filter(ptr -> ptr != null && getNodeValue(ptr) > 0)
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
        return Arrays.stream(getNeighbours(current))
                .map(this::getNodeValue)
                .noneMatch(value -> value == getNodeValue(current))
                && updateEdges(current);
    }

    /**
     * Updates the edges Set and checks the uniqueness of edges
     *
     * @param current pointer to the current node
     * @return false if uniqueness of set is violated
     */
    private boolean updateEdges(Pointer current) {
        int currentValue = getNodeValue(current);

        Pointer[] neighbours = getNeighbours(current);
        Set<Edge> newEdges = new HashSet<>();

        for (Pointer p : neighbours) {
            if (!newEdges.add(new Edge(getNodeValue(p), currentValue, p, current)))
                return false;
        }

        if (newEdges.stream().noneMatch(newEdge -> edges.contains(newEdge))) {
            edges.addAll(newEdges);
            return true;
        }
        return false;
    }

    protected void deleteEdges(Pointer pointer) {
        edges.removeIf(edge -> edge.contains(pointer));
    }

    protected Node getNodeAt(Pointer pointer) {
        return graph[pointer.getColIndex()][pointer.getRowIndex()];
    }

    /**
     * @return solution based on the current graph state
     */
    protected int[][] getCurrentSolution() {
        return Arrays.stream(graph)
                .map(nodes -> Arrays.stream(nodes)
                        .mapToInt(Node::getCurrent)
                        .toArray())
                .toArray(int[][]::new);
    }

    public long getCost() {
        return iterator.getCost();
    }

    abstract public List<int[][]> solve();
}

