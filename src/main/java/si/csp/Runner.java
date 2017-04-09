package si.csp;


import si.csp.gc_csp.Backtracking;
import si.csp.gc_csp.CSPStrategy;
import si.csp.utils.BaseGraphIterator;
import si.csp.utils.GraphIterator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Runner {


    static public void main(String args[]) {
        //backtracking with standard from first to last heuristic
        GraphIterator baseIterator = new BaseGraphIterator();
        CSPStrategy backtracking = new Backtracking(2, baseIterator);
        List<int[][]> results = backtracking.solve();
        results.forEach(Runner::displayResult);
        results.stream()
                .map(result -> results.stream()
                        .filter(inner -> Arrays.deepEquals(result, inner))
                        .count())
                .forEach(System.out::print);
        System.out.println("Znaleziono " + results.size() + " rozwiązań, koszt: " + backtracking.getCost());

    }

    private static void displayResult(int[][] result) {
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