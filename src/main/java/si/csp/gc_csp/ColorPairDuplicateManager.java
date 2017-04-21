package si.csp.gc_csp;

/**
 * @author Jakub Licznerski
 *         Created on 20.04.2017.
 */
public class ColorPairDuplicateManager {

    private boolean[][] colors;


    public ColorPairDuplicateManager(int colorsLength) {
        this.colors = new boolean[colorsLength][colorsLength];
    }

    public boolean addPair(int color1, int color2) {
        if (color1 > color2)
            return addUnifiedPair(color2, color1);
        else
            return addUnifiedPair(color1, color2);
    }

    //color1 < color2
    private boolean addUnifiedPair(int color1, int color2) {
        return !colors[color1 - 1][color2 - 1] && (colors[color1 - 1][color2 - 1] = true);
    }

    public boolean hasColorsPair(int color1, int color2) {
        if (color1 > color2)
            return hasUnifiedPair(color2, color1);
        else
            return hasUnifiedPair(color1, color2);
    }

    //color1 < color2
    private boolean hasUnifiedPair(int color1, int color2) {
        return colors[color1 - 1][color2 - 1];
    }

    public void deletePair(int color1, int color2) {
        if (color1 > color2)
            deleteUnifiedPair(color2, color1);
        else
            deleteUnifiedPair(color1, color2);
    }

    private void deleteUnifiedPair(int color1, int color2) {
        colors[color1 - 1][color2 - 1] = false;
    }
}
