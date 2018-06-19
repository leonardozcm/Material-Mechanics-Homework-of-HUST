/**
 * !!Please Don't edit this filehead.
 * Created with IntelliJ IDEA.
 * Created by @author ChangMin Zhao
 * Description:
 * User: ${USER}
 * Date: ${YEAR}-${MONTH}-${DAY}
 * Time: ${TIME}
 * Usage: ${PROJECT_NAME}
 */
package GraphObj;

import GraphObj.Utils.ScaleTrans;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class BitmapHolder extends GraphHolder implements doWork {
    private int[] pixels;
    private BufferedImage img;
    private ScaleTrans scaleTrans;
    private double AxisRad;
    private File bitfile;

    private double AreaCounter = 0;
    private double PerimeterCounter = 0;
    private double CentroidXCounter = 0;
    private double CentroidYCounter = 0;
    private double AxisCounter = 0;//惯性积累加器
    private double MOIYCounter = 0;//主Y惯性矩累加器
    private double MOIZCounter = 0;//主Z惯性矩累加器
    private double LastAxis = 0;

    private int[] corepixels;
    private int[] Axispixels;


    BitmapHolder(File bitmapfile, double mscale) {
        bitfile = bitmapfile;
        try {
            FileInputStream fielstream = new FileInputStream(bitmapfile);
            img = ImageIO.read(fielstream);


            ImageInfo imageInfo = Imaging.getImageInfo(bitmapfile);
            System.out.println(imageInfo.getPhysicalHeightDpi() + ":" + imageInfo.getPhysicalWidthDpi());
            if (imageInfo.getPhysicalHeightDpi() == imageInfo.getPhysicalWidthDpi()) {
                scaleTrans = new ScaleTrans(mscale, imageInfo.getPhysicalHeightDpi());
            } else {
                scaleTrans = new ScaleTrans(1, 120);
            }

            setWidth(img.getWidth());
            setHeight(img.getHeight());
            Axispixels = new int[width * height];
            corepixels = pixels = new int[width * height];


            img.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BitmapHolder(BufferedImage img) {
        setWidth(img.getWidth());
        setHeight(img.getHeight());
        Axispixels = corepixels = pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);
    }

    void bitmapPrint() {
        int counter = 0;
        for (int i : pixels) {
            if (counter % width == 0) {
                System.out.println();
            }
            if (i == Color.WHITE.getRGB()) {
                System.out.print("1 ");
            } else if (i == Color.BLACK.getRGB()) {
                System.out.print("0 ");
            } else {
                System.out.print("@ ");
            }
            counter++;
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    protected void setWidth(int width) {
        super.setWidth(width);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    protected void setHeight(int height) {
        super.setHeight(height);
    }

    public void runCulAPC() {
        System.out.println("正在计算周长面积...");
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                if (pixels[i * width + j] == Color.BLACK.getRGB()) {
                    culPerimeter(j, i);
                } else if (pixels[i * width + j] != Color.BLACK.getRGB() && pixels[i * width + j] != Color.WHITE.getRGB()) {
                    culArea(j, i);
                    culCentroid(j, i);
                }
            }

        CentroidYCounter /= AreaCounter;
        CentroidXCounter /= AreaCounter;
    }

    public void runCulAM() {
        System.out.println("正在计算主轴...");

        double rad = toRadians(-10);
        mainloop:
        for (double step = toRadians(5); step >= toRadians(0.01); step /= 2) {

            for (; rad <= toRadians(100); rad += step) {

                AxisCounter = 0;
//                System.out.println(Math.toDegrees(rad) + " : " + Math.toDegrees(step));
                for (int i = 0; i < height; i++)
                    for (int j = 0; j < width; j++) {

                        if (pixels[i * width + j] != Color.BLACK.getRGB() && pixels[i * width + j] != Color.WHITE.getRGB()) {

                            culAxis(j, i, rad);
                        }
                    }
//                System.out.println("LastAxis = " + LastAxis + " ,AxisCounter = " + AxisCounter);
                if (AxisCounter == 0) {
                    AxisRad = rad;

//                    System.out.println("...1");
                    break mainloop;
                } else if (AxisCounter * LastAxis < 0) {


                    if (step > toRadians(0.02)) {
//                        System.out.println("...2");
                        rad -= step;
                        AxisRad = rad;
                        break;
                    } else {
//                        System.out.println("...");
                        if (Math.abs(AxisCounter) < Math.abs(LastAxis)) {
                            AxisRad = rad;
//                            System.out.println("...3");
                            break mainloop;
                        } else {
                            AxisRad = rad - step;
                            AxisCounter = LastAxis;
//                            System.out.println("...4");
                            break mainloop;
                        }
                    }
                }

                LastAxis = AxisCounter;
            }
        }
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {

                if (pixels[i * width + j] != Color.BLACK.getRGB() && pixels[i * width + j] != Color.WHITE.getRGB()) {

                    culMOI(j, i);
                }
            }
        drawAxis();
    }

    private void drawAxis() {
        int tmp = 0;
        for (int i : pixels) {
            Axispixels[tmp++] = i;
        }

        int x = 0, y = 0;
        System.out.println(Math.tan(AxisRad));
        for (int i = 0; i < width; i++) {

            y = (int) Math.round(CentroidYCounter - Math.tan(AxisRad) * (i - CentroidXCounter));
            if (y >= 0 && y < height) {
                Axispixels[y * width + i] = Color.BLACK.getRGB();
            }

            y = (int) Math.round(CentroidYCounter + (1 / Math.tan(AxisRad)) * (i - CentroidXCounter));
            if (y >= 0 && y < height) {
                Axispixels[y * width + i] = Color.BLACK.getRGB();
            }

        }
        for (int j = 0; j < height; j++) {
            x = (int) Math.round(CentroidXCounter + Math.tan(AxisRad) * (j - CentroidYCounter));
            if (x >= 0 && x < width) {
                Axispixels[j * width + x] = Color.BLACK.getRGB();
            }
            x = (int) Math.round(CentroidXCounter - (1 / Math.tan(AxisRad)) * (j - CentroidYCounter));
            if (x >= 0 && x < width) {
                Axispixels[j * width + x] = Color.BLACK.getRGB();
            }
        }
        try {
            File output = new File(bitfile.getParent() + "\\output_Axis_" + bitfile.getName());
            if (!output.exists()) {
                output.createNewFile();
            }
            BufferedImage outImg = new BufferedImage(width, height, TYPE_INT_RGB);
            outImg.setRGB(0,
                    0,
                    width,
                    height,
                    Axispixels,
                    0,
                    width);
            ImageIO.write(outImg, "bmp", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runCulCore() {
        System.out.println("正在计算截面核心...");
        double k1 = 0;
        double k2 = 0;
        double k = 0;
        double b = 0;

        int percentCounter = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (pixels[i * width + j] != Color.BLACK.getRGB() && pixels[i * width + j] != Color.WHITE.getRGB()) {
                    double rx = j - CentroidXCounter + 0.5;
                    double ry = i - CentroidYCounter + 0.5;

                    k1 = (rx * Math.cos(AxisRad) + ry * Math.sin(AxisRad)) * AreaCounter / MOIZCounter;
                    k2 = (ry * Math.cos(AxisRad) - rx * Math.sin(AxisRad)) * AreaCounter / MOIYCounter;
                    k = (k2 * Math.sin(AxisRad) - k1 * Math.cos(AxisRad)) / (k1 * Math.sin(AxisRad) + k2 * Math.cos(AxisRad));
                    b = -1 / (k1 * Math.sin(AxisRad) + k2 * Math.cos(AxisRad));
                    if (veriCore(k, b)) {
//                        System.out.println("YES");
                        corepixels[i * width + j] = Color.BLACK.getRGB();
                    } else {
//                        System.out.println("NO");
                        corepixels[i * width + j] = pixels[i * width + j];
                    }
                }
            }
            if (Math.round(100 * i / height) > 5 * percentCounter) {
                percentCounter++;
                System.out.println("计算进度..." + Math.round(100 * i / height));
            }
        }

        try {
            File output = new File(bitfile.getParent() + "\\output_Core_" + bitfile.getName());
            if (!output.exists()) {
                output.createNewFile();
            }
            BufferedImage outImg = new BufferedImage(width, height, TYPE_INT_RGB);
            outImg.setRGB(0,
                    0,
                    width,
                    height,
                    pixels,
                    0,
                    width);
            ImageIO.write(outImg, "bmp", output);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void scan() {
        runCulAPC();
        runCulAM();
        runCulCore();
    }

    private void culArea(int x, int y) {
        AreaCounter++;
    }

    private void culPerimeter(int x, int y) {
        PerimeterCounter++;
    }

    private void culCentroid(int x, int y) {
        CentroidXCounter += x + 0.5;
        CentroidYCounter += y + 0.5;
    }

    private void culAxis(int x, int y, double rad) {
        double rx = x - CentroidXCounter + 0.5;
        double ry = y - CentroidYCounter + 0.5;
        AxisCounter += 0.5 * (-2 * rx * ry * Math.cos(2 * rad) - (rx * rx - ry * ry) * Math.sin(2 * rad));
    }

    private void culMOI(int x, int y) {
        double rx = (x - CentroidXCounter + 0.5) * Math.cos(AxisRad) + (y - CentroidYCounter + 0.5) * Math.sin(AxisRad);
        double ry = -(x - CentroidXCounter + 0.5) * Math.sin(AxisRad) + (y - CentroidYCounter + 0.5) * Math.cos(AxisRad);
        MOIZCounter += rx * rx;
        MOIYCounter += ry * ry;
    }

    private boolean veriCore(double k, double b) {
        int y, x;
        boolean flag = true;

        //todo boundary condition
        for (int j = 0; j < width; j++) {

            y = vC_culY(k, b, j);
            if (y >= 0 && y < height && pixels[y * width + j] != Color.WHITE.getRGB())
                flag = false;
        }
        //todo 这里会缩小核心的范围
        if (flag) {
            for (int i = 0; i < height; i++) {
                x = vC_culX(1 / k, b / k, i);
                if (x >= 0 && x < width && pixels[i * height + x] != Color.WHITE.getRGB())
                    flag = false;
            }
        }

        return flag;
    }

    private int vC_culY(double k, double b, int x) {
        return (int) Math.round(k * (x - CentroidXCounter + 0.5) - b + CentroidYCounter - 0.5);
    }

    private int vC_culX(double k, double b, int y) {
        return (int) Math.round(k * (y - CentroidYCounter + 0.5) + b + CentroidXCounter - 0.5);
    }

    public void culBendCen(int x, int y) {

    }

    public void printResult() {
        System.out.println("周长是： " + PerimeterCounter);
        System.out.println("面积是： " + AreaCounter);
        System.out.println("x形心是： " + CentroidXCounter);
        System.out.println("y形心是： " + CentroidYCounter);
        System.out.println("最小惯性积是 " + AxisCounter);
        System.out.println("形心角是 " + Math.toDegrees(AxisRad));
        System.out.println("Y主惯性矩为：" + MOIYCounter + " ,Z主惯性矩为：" + MOIZCounter);
    }

    private double toRadians(double angel) {
        return Math.PI * angel / 180;
    }

}
