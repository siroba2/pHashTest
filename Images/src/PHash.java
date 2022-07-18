import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

public class PHash {

    //Define attributes of the class
    private BufferedImage image = null;
    //private String image_path = " ";
    private Path image_path;
    private int size = 32;
    private int hash_size = 32;
    private String hashHex;

    private double[] c = new double[size];

    public String getHashHex(){
        return this.hashHex;
    }


    //Constructor
    public PHash(Path image_path) throws IOException {
        this.image_path = image_path;

        this.initializeCoefficients();

        try {
            this.image = ImageIO.read(new File(String.valueOf(image_path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PHash(String str) throws IOException {
        this.size = size;
        this.initializeCoefficients();
        this.image = readURL(str);
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



    // DCT function stolen from http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java

    //Compute the DCT (discrete cosine transform), reduce the DCT and compute the average value
    public String DCT() {
        BufferedImage img = getImage();
        ImagePropierties auxImage = new ImagePropierties();
        BufferedImage imgResized = auxImage.resize(img, 32, 32);

        BufferedImage greyImg;
        try {
            greyImg = auxImage.changeColor(imgResized);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //1º Compute the DCT (into a collection of frequencies and scalars)
        double[][] DCT = new double[size][size];
        int N = size;
        double[][] vals;
        vals = auxImage.convertTo2DUsingGetRGB(greyImg);
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

                DCT[u][v] = sum;

            }
        }


        //Reduce the DCT and compute the average value
        double total = 0.0;

        for (int x = 0; x < hash_size; x++) {
            for (int y = 0; y < hash_size; y++) {
                total += DCT[x][y];
            }
        }

        total -= DCT[0][0];

       double avg = total / (double) (hash_size * hash_size - 1);
        // Further reduce the DCT.

        StringBuilder hash = new StringBuilder();

        for (int x = 0; x < hash_size; x++) {
            for (int y = 0; y < hash_size; y++) {
                if (x != 0 && y != 0) {
                    hash.append(DCT[x][y] > avg ? "1" : "0");
                }
            }
        }
        hashHex = MathAux.longBinToHex(hash.toString());
        String binary = hash.toString();
        return binary;

    }

    public BufferedImage readURL (String str){
        Image image = null;
        try {
            URL url = new URL(str);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (BufferedImage) image;
    }


}
