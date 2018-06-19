import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class PageReader {
    public static void main(String[] args) {
        try {
            readsquare(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readsquare(String filepath) throws Exception {
        File square = new File(filepath);
        System.out.println("dsf.ds".split("\\.")[0]);
        FileInputStream fielstream = new FileInputStream(square);
        BufferedImage img = ImageIO.read(fielstream);
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);
        int counter = 0;
        for (int i : pixels) {
            if (counter % w == 0) {
                System.out.println();
            }
            if (i == -1) {
                System.out.print("0 ");
            } else if (i == Color.RED.getRGB()) {
                System.out.print("R ");
            } else {
                System.out.print("1 ");
            }
            counter++;
        }
    }

}
