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
       /* Phash2 image1 = new Phash2(32 , 8 ,Path.of("Images/TestImages/Original.jpeg"));
        String str2= image1.getHash(Path.of("Images/TestImages/Original.jpeg"));
        */
       PHash image2 = new PHash(Path.of("Images/TestImages/Original.jpeg"), 32);
       BufferedImage image2Reduced = image2.resize(image2.getImage(),32,32);
        BufferedImage greyImage2 = image2.changeColor(image2Reduced);
        String str1= image2.DCT(greyImage2, greyImage2.getHeight());
        System.out.println(str1);
       /* int distance = image2.hammingDist(str1,str2);

        System.out.println(distance);*/

        PHash imageURL = new PHash("https://scopely.widen.net/content/tvkl1flyei/original/MSF-17998_Delta-TC-Gameplay--Rogue_EN_1080x1080_C1_V1.jpg?u=3mz6gd&download=true&x.share=t", 32);
        BufferedImage imageURLReduced = image2.resize(imageURL.getImage(),32,32);
        BufferedImage greyImageURL = image2.changeColor(imageURLReduced);
        String str2= image2.DCT(greyImageURL, greyImageURL.getHeight());
        System.out.println(str2);

    }
}
