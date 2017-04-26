package si.csp;


import si.csp.gc_csp.backtracking.GraphBacktracking;
import si.csp.gc_csp.GraphCSPStrategy;
import si.csp.n_queens.QueensCSPStrategy;
import si.csp.n_queens.backtracking.QueensBacktracking;
import si.csp.utils.BaseBoardIterator;
import si.csp.utils.BaseGraphIterator;
import si.csp.utils.BoardIterator;
import si.csp.utils.GraphIterator;

import java.util.List;

public class Runner {
    private static final int UNIT_COST = 1;


    static public void main(String args[]) {

        runQueensBacktrackingStandard(15);

    }

    private static void runGraphBacktrackingStandard(int N) {
        //backtracking with standard from first to last heuristic
        GraphIterator baseIterator = new BaseGraphIterator(UNIT_COST);
        GraphCSPStrategy backtracking = new GraphBacktracking(N, baseIterator);
        long now = System.currentTimeMillis();
        List<int[][]> results = backtracking.solve();
        System.out.println("Rozwiązania znaleziono w: " + ((double)(System.currentTimeMillis() - now))/1000 + " sekund");
        System.out.println("Znaleziono " + results.size() + " rozwiązań, koszt: " + backtracking.getCost());
    }

    private static void runQueensBacktrackingStandard(int N) {
        //level by level heuristic (down - col:0, up - col: N-1)
        BoardIterator baseIterator = new BaseBoardIterator(UNIT_COST);
        QueensCSPStrategy backtracking = new QueensBacktracking(N, baseIterator);
        long now = System.currentTimeMillis();
        List<boolean[][]> results = backtracking.solve();
        System.out.println("Rozwiązania znaleziono w: " + ((double)(System.currentTimeMillis() - now))/1000 + " sekund");
        System.out.println("Znaleziono " + results.size() + " rozwiązań, koszt: " + backtracking.getCost());
    }

    public static void displayResults(int[][] result) {
        int N = result.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j == N - 1)
                    System.out.print(result[j][i]);
                else
                    System.out.print(result[j][i] + "-");
            }
            System.out.print("\n");
            if (i < N - 1) {
                for (int[] aResult : result) {
                    System.out.print("| ");
                }
                System.out.print("\n");
            }
        }

        for (int i = 0; i < 2 * N + 1; i++) {
            System.out.print("_");
        }
        System.out.println("\n");
    }

    public static void displayResults(boolean[][] result) {
        int N = result.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j == N - 1)
                    System.out.print(result[j][i]?1:0);
                else
                    System.out.print((result[j][i]?1:0) + "-");
            }
            System.out.print("\n");
            if (i < N - 1) {
                for (boolean[] aResult : result) {
                    System.out.print("| ");
                }
                System.out.print("\n");
            }
        }

        for (int i = 0; i < 2 * N + 1; i++) {
            System.out.print("_");
        }
        System.out.println("\n");
    }
}