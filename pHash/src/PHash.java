import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class PHash {

    //Define attributes of the class
    private BufferedImage image = null;
    private String image_path = " ";
    private int hash_size = 8;
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
    public File changeColor (BufferedImage image) throws  IOException{
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

        grey_image = new File("pHash/TestImages/GreyImage.jpeg");
        try {
            ImageIO.write(image,"jpg",grey_image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return grey_image;

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

    private static BufferedImage covertToImageUsingSetRGB(int [][] imageMatrix) throws  IOException{
        BufferedImage image = null;
        for(int i=0; i<imageMatrix.length; i++) {
            for(int j=0; j<imageMatrix.length; j++) {
                int a = imageMatrix[i][j];
                Color newColor = new Color(a,a,a);
                image.setRGB(j,i,newColor.getRGB());
            }
        }
        File output = new File("GrayScale.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

     //Compute the DCT (discrete cosine transform), reduce the DCT and compute the average value
    public void DCT (int[][] image , int dim) throws  IOException{

        //1º Compute the DCT (into a collection of frequencies and scalars)
    File DCT_image = null;
    int [][] DST = new int[dim][dim];
        int i, j, u, v;
        double constU, constV, Cu, Cv, Cuv, tmp;
        double constOp = PI/16.0;
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
                        tmp += Cuv * image[i ][j] * cos((2 * i + 1) * constU) * cos((2 * j + 1) * constV);
                    }

                    DST[u ][v] = (int) tmp;
                }
            }
        }

        //Write the dct image


        DCT_image = new File("TestImages/DCTImage.jpeg");
        try {
            ImageIO.write(covertToImageUsingSetRGB(image),"jpg",DCT_image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        



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


    //Construct the hash















}
