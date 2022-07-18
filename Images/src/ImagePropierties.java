import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImagePropierties {

    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage tThumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
        tGraphics2D.setBackground(Color.WHITE);
        tGraphics2D.setPaint(Color.WHITE);
        tGraphics2D.fillRect(0, 0, width, height);
        tGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        tGraphics2D.drawImage(image, 0, 0, width, height, null); //draw the image scaled

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

        return image;

    }

    public double[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        //int[][] result = new int[height][width];
        double[][] result = new double[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                result[row][col] = image.getRGB(row, col) & 0xff;
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


}
