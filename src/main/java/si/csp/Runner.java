package si.csp;


import si.csp.gc_csp.Backtracking;
import si.csp.gc_csp.CSPStrategy;
import si.csp.utils.BaseGraphIterator;
import si.csp.utils.GraphIterator;

import java.util.List;

public class Runner {
    private static final int UNIT_COST = 1;


    static public void main(String args[]) {
        //backtracking with standard from first to last heuristic
        GraphIterator baseIterator = new BaseGraphIterator(UNIT_COST);
        CSPStrategy backtracking = new Backtracking(3, baseIterator);
        long now = System.currentTimeMillis();
        List<int[][]> results = backtracking.solve();
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
}