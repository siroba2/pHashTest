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
        PHash cat = new PHash(Path.of("Images/TestImages/tree1.jpeg"), 8 ,4 );
        cat.reduceSize(cat.getImage(),8,8);

        BufferedImage file2 = cat.changeColor(cat.getImage());
        String str2 = cat.DCT(cat.convertTo2DUsingGetRGB(file2), file2.getHeight());

       PHash tree1 = new PHash(Path.of("Images/TestImages/Lake.jpeg"), 8 ,4 );
        tree1.reduceSize(tree1.getImage(),8,8);
        BufferedImage file1 = tree1.changeColor(tree1.getImage());
        String str1= tree1.DCT(tree1.convertTo2DUsingGetRGB(file1), file1.getHeight());

        int distance = tree1.hammingDist(str1,str2);

        System.out.println(distance);
    }
}
