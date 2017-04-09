package si.csp.gc_csp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import si.csp.Runner;
import si.csp.utils.Pointer;


/**
 * @author Jakub Licznerski
 *         Created on 08.04.2017.
 */
public class CSPStrategyTest {

    private final int N = 3;

    private CSPStrategy strategy;

    @Before
    public void init() {
        strategy = new Backtracking(N, null);

        strategy.graph = prepareGraph(N);
//        displayResult(strategy.getCurrentSolution());
    }

    @Test
    public void getCurrentSolutionTest() {
        int[][] expected = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};

        Assert.assertArrayEquals(expected, strategy.getCurrentSolution());
    }

    @Test
    public void getNeighboursTestLeftUpperCorner() {
        Pointer[] expected = {
                new Pointer(1, 0),
                new Pointer(0, 1)
        };

        Assert.assertArrayEquals(expected, strategy.getNeighbours(new Pointer(0, 0)));
    }

    @Test
    public void getNeighboursTestCenter() {
        Pointer[] expected = {
                new Pointer(0, 1),
                new Pointer(2, 1),
                new Pointer(1, 0),
                new Pointer(1, 2)
        };

        Assert.assertArrayEquals(expected, strategy.getNeighbours(new Pointer(1, 1)));
    }

    private Node[][] prepareGraph(int n) {
        Node[][] result = new Node[n][n];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Node current = new Node();
                current.setCurrent(i + j + 1);
                result[j][i] = current;
            }
        }
        return result;
    }


//    private static void displayResult(int[][] result) {
//        int N = result.length;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                if (j == N - 1)
//                    System.out.print(result[j][i]);
//                else
//                    System.out.print(result[j][i] + "-");
//            }
//            System.out.print("\n");
//            if (i < N - 1) {
//                for (int j = 0; j < N; j++) {
//                    System.out.print("| ");
//                }
//                System.out.print("\n");
//            }
//        }
//
//        for (int i = 0; i < 2 * N + 1; i++) {
//            System.out.print("_");
//        }
//        System.out.println("\n");
//    }
}