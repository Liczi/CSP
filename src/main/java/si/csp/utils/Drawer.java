package si.csp.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * @author Jakub Licznerski
 *         Created on 05.04.2017.
 */
public class Drawer {
    private static final String PATH = "generated/";

    private static final int LENGTH = 20;
    private static final int ARC = LENGTH / 5;
    private static final int SPACING = 5;

    private final int N;
    private final Color[] graph;

    private BufferedImage bufferedImage;
    private Graphics2D graphics;

    public Drawer(Color[] graph) {
        this.N = graph.length;
        this.graph = graph;

        int canvSize = N * (LENGTH + 2 * SPACING) + 25;
        bufferedImage = new BufferedImage(
                canvSize,
                canvSize,
                BufferedImage.TYPE_INT_ARGB
        );
    }

    private void drawRect(int x, int y, Color color) {

        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        graphics = bufferedImage.createGraphics();
        graphics.setPaint(color);
        graphics.fillRoundRect(x, y, LENGTH, LENGTH, ARC, ARC);
    }

    public void drawGraph() {
        drawRect(0, 0, Color.black);

        //drawing to file
        try {
            ImageIO.write(
                    bufferedImage,
                    "gif",
                    new File(PATH + System.currentTimeMillis() + ".gif")
            );
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}