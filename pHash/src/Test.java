import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String args[]) throws IOException  //static method
    {
        PHash tree2 = new PHash("pHash/TestImages/tree2.jpeg", 8 ,4 );
        tree2.reduceSize(tree2.getImage(),8,8);
        BufferedImage file2 = tree2.changeColor(tree2.getImage());
        String str2 = tree2.DCT(tree2.convertTo2DUsingGetRGB(file2), file2.getHeight());

        PHash tree1 = new PHash("pHash/TestImages/tree1.jpeg", 8 ,4 );
        tree1.reduceSize(tree1.getImage(),8,8);
        BufferedImage file1 = tree1.changeColor(tree1.getImage());
        String str1= tree1.DCT(tree1.convertTo2DUsingGetRGB(file1), file1.getHeight());

        int distance = tree1.hammingDist(str1,str2);

        System.out.println(distance);
    }
}
