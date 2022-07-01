import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.util.Arrays;

public class PHash {

    //Define attributes of the class
    private BufferedImage image = null;
    //private String image_path = " ";
    private Path image_path;
    private int size = 32;
    private int hash_size = 8;

    private double[] c = new double[size];


    //Constructor
    public PHash(Path image_path, int size) throws IOException {
        this.image_path = image_path;
        this.size = size;
        this.initializeCoefficients();

        try {
            this.image = ImageIO.read(new File(String.valueOf(image_path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCoefficients() {
        for (int i = 1; i < size; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }

    //
    public BufferedImage getImage() {

        return this.image;
    }


    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage tThumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
        tGraphics2D.setBackground(Color.WHITE);
        tGraphics2D.setPaint(Color.WHITE);
        tGraphics2D.fillRect(0, 0, width, height);
        tGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        tGraphics2D.drawImage(image, 0, 0, width, height, null); //draw the image scaled

       /* File output = new File(image_path.subpath(0, 2) + "Resize" + image_path.getName(2));

        try {
            ImageIO.write(tThumbImage, "JPG", output); //write the image to a file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        return tThumbImage;
    }


    public static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bi = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }


    // Change the RGB components of the image to a gray scale
    public BufferedImage changeColor(BufferedImage image) throws IOException {
        File grey_image = null;
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {

                //Get pixels values and extract its RGB values
                int pixel = image.getRGB(i, j); // i == x ; j == y
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                //Find average and set new pixel value

                int avg = (red + blue + green) / 3;
                pixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(i, j, pixel);

            }
        }

        //Write the grey image
        grey_image = new File(image_path.subpath(0, 2) + "GreyImage" + image_path.getName(2));
        try {
            ImageIO.write(image, "jpg", grey_image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage newImage = ImageIO.read(grey_image);
        return newImage;

    }

    public double[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        //int[][] result = new int[height][width];
        double[][] result = new double[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                result[row][col] = image.getRGB(row, col);
            }
        }

        return result;
    }

    public static double[][] transpose(double[][] arr) {
        double[][] transposedMatrix = new double[arr[0].length][arr.length];

        for(int x = 0; x < transposedMatrix.length; x++) {
            for(int y = 0; y < transposedMatrix[0].length; y++) {
                transposedMatrix[x][y] = arr[y][x];
            }
        }

        return transposedMatrix;
    }

    public static void writeImage(double[][] image, int width, int height, String filename) {
        // flatten the 2d array
        double[] result = Arrays.stream(transpose(image))
                .flatMapToDouble(Arrays::stream)
                .toArray();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        // BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        WritableRaster raster = outputImage.getRaster();
        raster.setSamples(0, 0, width, height, 0, result);

        try {
            ImageIO.write(outputImage, "jpg", new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeImage(int[][] image, int width, int height, String filename) {
        // flatten the 2d array
        int[] result = Arrays.stream(image)
                .flatMapToInt(Arrays::stream)
                .toArray();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = outputImage.getRaster();
        raster.setSamples(0, 0, width, height, 0, result);

        try {
            ImageIO.write(outputImage, "jpg", new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // DCT function stolen from http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java

    //Compute the DCT (discrete cosine transform), reduce the DCT and compute the average value
    public String DCT(BufferedImage img, int dim) {

        //1º Compute the DCT (into a collection of frequencies and scalars)
        File DCT_image;
        double[][] DST = new double[dim][dim];
        int N = size;

        double[][] vals;

        vals = convertTo2DUsingGetRGB(img);
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {

                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (vals[i][j]);


                    }

                }

                // sum *= (2 * c[u] * c[v]) / sqrt(size * size) =
                // sum *= (2 * c[u] * c[v]) / size =
                // sum *= (    c[u] * c[v]) / (size/2.0)
                sum *= (c[u] * c[v]) / (size / 2.0);

                DST[u][v] = sum;
                //System.out.println(sum);

            }
        }


        writeImage(DST, size, size, image_path.subpath(0, 2) + "DCT" + image_path.getName(2));


        //IDCT


        double[][] f = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double sum = 0.0;
                for (int u = 0; u < N; u++) {
                    for (int v = 0; v < N; v++) {
                        sum += (c[u] * c[v]) / (size / 2.0) * Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * DST[u][v];
                    }
                }
                f[i][j] = Math.round(sum);
            }
        }

        writeImage(f, size, size, image_path.subpath(0, 2) + "IDCT" + image_path.getName(2));


        //Reduce the DCT and compute the average value
        double total = 0.0;

        for (int x = 0; x < hash_size; x++) {
            for (int y = 0; y < hash_size; y++) {
                total += DST[x][y];
            }
        }

        total -= DST[0][0];
        //System.out.println("total  " + total);

        double avg = total / (double) (hash_size * hash_size - 1);

        // Further reduce the DCT.

        StringBuilder hash = new StringBuilder();

        for (int x = 0; x < hash_size; x++) {
            for (int y = 0; y < hash_size; y++) {
                if (x != 0 && y != 0) {
                    hash.append(DST[x][y] > avg ? "1" : "0");
                }
            }
        }
        String phash = hash.toString();
        System.out.println(binToHex(phash));


        return phash;

    }

    public static String binToHex(String bin) {
        long decimal = Long.parseLong(bin,2);
        return Long.toString(decimal,16);
    }



    private static void convertStringToHex(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }

        System.out.println(stringBuilder.toString());

    }

    //Hamming distance
    public int hammingDist(String str1, String str2) {
        int i = 0, count = 0;
        while (i < str1.length()) {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }


}
