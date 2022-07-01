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
        Phash2 image1 = new Phash2(32 , 8 ,Path.of("Images/TestImages/Original.jpeg"));
        String str2= image1.getHash(Path.of("Images/TestImages/Original.jpeg"));


       PHash image2 = new PHash(Path.of("Images/TestImages/Original.jpeg"), 32);
        BufferedImage image2Reduced = image2.resize(image2.getImage(),32,32);
        BufferedImage greyImage2 = image2.changeColor(image2Reduced);
        String str1= image2.DCT(greyImage2, greyImage2.getHeight());
        System.out.println(str1);
        int distance = image2.hammingDist(str1,str2);

        System.out.println(distance);

    }
}
