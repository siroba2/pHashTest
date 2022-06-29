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
        PHash image1 = new PHash(Path.of("Images/TestImages/Original.jpeg"), 32  );
        BufferedImage image1Reduced = image1.resize(image1.getImage(),32,32);

        BufferedImage greyImage1 = image1.changeColor(image1Reduced);
        String str2 = image1.DCT(image1.convertTo2DUsingGetRGB(greyImage1), greyImage1.getHeight());

       /*PHash image2 = new PHash(Path.of("Images/TestImages/tree1.jpeg"), 32);
        BufferedImage image2Reduced = image2.resize(image2.getImage(),32,32);
        BufferedImage greyImage2 = image2.changeColor(image2Reduced);
        String str1= image2.DCT(image2.convertTo2DUsingGetRGB(greyImage2), greyImage2.getHeight());

        int distance = image2.hammingDist(str1,str2);

        System.out.println(distance);

        */
    }
}
