import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.cos;

public class PHash {

    //Define attributes of the class
    private BufferedImage image = null;
    private String image_path = " ";
    private int hash_size = 8;
    private int size = 32;
    private int highfreq_factor = 4;

    int [][] zigzag = {
        {0, 0},
        {0, 1}, {1, 0},
        {2, 0}, {1, 1}, {0, 2},
        {0, 3}, {1, 2}, {2, 1}, {3, 0},
        {4, 0}, {3, 1}, {2, 2}, {1, 3}, {0, 4},
        {0, 5}, {1, 4}, {2, 3}, {3, 2}, {4, 1}, {5, 0},
        {6, 0}, {5, 1}, {4, 2}, {3, 3}, {2, 4}, {1, 5}, {0, 6},
        {0, 7}, {1, 6}, {2, 5}, {3, 4}, {4, 3}, {5, 2}, {6, 1}, {7, 0},
        {7, 1}, {6, 2}, {5, 3}, {4, 4}, {3, 5}, {2, 6}, {1, 7},
        {2, 7}, {3, 6}, {4, 5}, {5, 4}, {6, 3}, {7, 2},
        {7, 3}, {6, 4}, {5, 5}, {4, 6}, {3, 7},
        {4, 7}, {5, 6}, {6, 5}, {7, 4},
        {7, 5}, {6, 6}, {5, 7},
        {6, 7}, {7, 6},
        {7, 7}
    };


    //Constructor
    public PHash(String image_path , int hash_size, int highfreq_factor) throws IOException {
        this.image_path= image_path;
        this.hash_size= hash_size;
        this.highfreq_factor = highfreq_factor;
        try {
            this.image = ImageIO.read(new File(image_path));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //
    public BufferedImage getImage(){
        return this.image;
    }

    //Reduce size of the image --> method from https://www.baeldung.com/java-resize-image

    public BufferedImage reduceSize (BufferedImage image , int witdh , int height){

        BufferedImage resizedImage = new BufferedImage(witdh,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, witdh, height, null);
        graphics2D.dispose();
        return resizedImage;
    }


    // Change the RGB components of the image to a gray scale
    public BufferedImage changeColor (BufferedImage image) throws  IOException{
        File grey_image = null;
        for(int j=0 ; j< image.getHeight(); j++){
            for (int i = 0; i< image.getWidth() ; i++){

                //Get pixels values and extract its RGB values
                int pixel = image.getRGB(i,j); // i == x ; j == y
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                //Find average and set new pixel value

                int avg  = (red + blue + green) / 3;
                pixel = (alpha <<24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(i , j , pixel);

            }
        }

        //Write the grey image

        grey_image = new File("pHash/TestImages/GreyImage.jpg");
        try {
            ImageIO.write(image,"jpg",grey_image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage newImage = ImageIO.read(grey_image);
        return newImage;

    }

    public  int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    private static BufferedImage covertToImageUsingSetRGB(double[][] imageMatrix) throws  IOException{
        BufferedImage image = new BufferedImage( imageMatrix.length, imageMatrix[0].length, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < imageMatrix.length; x++){
            for (int y = 0; y < imageMatrix[x].length; y++) {
                image.setRGB(x, y, (int) Math.round(imageMatrix[x][y]));
            }
        };
        File output = new File("pHash/TestImages/DCT_2.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void writeImage(double[][] image, int width, int height, String filename){
        // flatten the 2d array
        double[] result = Arrays.stream(image)
                .flatMapToDouble(Arrays::stream)
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

    public static void writeImage(int[][] image, int width, int height, String filename){
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
    public String DCT (int[][] image , int dim) throws  IOException{

        //1º Compute the DCT (into a collection of frequencies and scalars)
    File DCT_image ;
    double [][] DST = new double[dim][dim];
    int N = size ;
    double [] c = new double[size];
        for (int i = 1; i < size; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {

                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (image[i][j]);


                    }

                }
                sum *= (c[u] * c[v]) / 4.0;
                DST[u][v] = sum;
                System.out.print(sum);

            }
        }

        /*
        int i, j, u, v;
        double constU, constV, Cu, Cv, Cuv, tmp;
        double constOp = Math.PI/16.0;
        double opt1= 0.5 /Math.sqrt(2);
        double opt2 = 0.5;

        for (u = 0; u < 8; u++) {
            constU = u * constOp;
            Cu = (u == 0) ? opt1 : opt2;

            for (v = 0; v < 8; v++) {
                constV = v * constOp;
                Cv = (v == 0) ? opt1 : opt2;
                Cuv = Cu * Cv;
                tmp = 0.0;

                for (i = 0; i < 8; i++) {
                    for (j = 0; j < 8; j++) {
                        tmp += Cuv * image[i ][j] * Math.cos((2 * i + 1) * constU) * Math.cos((2 * j + 1) * constV);
                    }

                    DST[u ][v] = (int) tmp;
                    System.out.print(tmp);

                }
            }
        }*/



        //Write the dct image
//        File DCT_image2 = new File("pHash/TestImages/DTC .jpg");
//        try {
//            ImageIO.write(covertToImageUsingSetRGB(DST),"jpg",DCT_image2);
//
//            System.out.println("test DCT");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        writeImage(DST, hash_size, hash_size, "pHash/TestImages/DTC_M.jpg");

        //Reduce the DCT and compute the average value
        double total = 0.0;

        for(int x = 0; x < hash_size; x++){
            for(int y = 0 ; y< hash_size ; y++){
                total += DST[x][y];
            }
        }

        total -= DST[0][0];

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
        System.out.println(hash.toString());

        return hash.toString();

    }
    //Hamming distance
    public int hammingDist(String str1, String str2)
    {
        int i = 0, count = 0;
        while(i < str1.length())
        {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }


    //delete(put to 0) the last n values of each block (8 x8 )
     public void FPB(double[] im, int n, int DimX, int DimY)
    {
        int i, j, k;

        for (i = 0; i < DimX; i += 8)
            for (j = 0; j < DimY; j += 8)
                for (k = 0; k < n; k++)
                    im[(i + zigzag[63 - k][1]) * DimY + (j + zigzag[63 - k][0])] = 0;
    }

    //delete(put to 0) the first n values of each block (8 x8 )
    public void FPA(double[] im,  int n, int DimX,  int DimY)
    {
        int i, j, k;


        for (i = 0; i < DimX; i += 8)
            for (j = 0; j < DimY; j += 8)
                for (k = 0; k < n; k++)
                    im[(i + zigzag[k][0]) * DimY + (j + zigzag[k][1])] = 0;
    }


















}
