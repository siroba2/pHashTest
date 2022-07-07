import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void main(String args[]) throws IOException  //static method
    {
        PHash image1 = new PHash(Path.of("Images/TestImages/out64.png"));

        String str1= image1.DCT();
        System.out.println("Phash binary: " +str1);
       // System.out.println("Phash binary length : " +str1.length());

        PHash image2 = new PHash(Path.of("Images/TestImages/out65.png"));

        PHash imageURL = new PHash("https://scopely.widen.net/content/tvkl1flyei/original/MSF-17998_Delta-TC-Gameplay--Rogue_EN_1080x1080_C1_V1.jpg?u=3mz6gd&download=true&x.share=t");
        //BufferedImage imageURLReduced = image2.resize(imageURL.getImage(),32,32);
        //BufferedImage greyImageURL = image2.changeColor(imageURLReduced);
        String str2= image2.DCT();
        //System.out.println("Phash binary: " +str1);
        //System.out.println("Phash binary length : " +str1.length());
        int distance = image1.hammingDist(str1,str2);

        System.out.println("Hamming distance " +distance);



    }
}
