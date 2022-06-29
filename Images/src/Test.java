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
        PHash image1 = new PHash(Path.of("Images/TestImages/tree2.jpeg"), 8 ,4 );
        image1.reduceSize(image1.getImage(),8,8);

        BufferedImage file2 = image1.changeColor(image1.getImage());
        String str2 = image1.DCT(image1.convertTo2DUsingGetRGB(file2), file2.getHeight());

       PHash image2 = new PHash(Path.of("Images/TestImages/tree1.jpeg"), 8 ,4 );
        image2.reduceSize(image2.getImage(),8,8);
        BufferedImage file1 = image2.changeColor(image2.getImage());
        String str1= image2.DCT(image2.convertTo2DUsingGetRGB(file1), file1.getHeight());

        int distance = image2.hammingDist(str1,str2);

        System.out.println(distance);
    }
}
